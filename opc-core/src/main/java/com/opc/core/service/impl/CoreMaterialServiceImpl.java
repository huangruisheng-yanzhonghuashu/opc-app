package com.opc.core.service.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.opc.core.domain.CoreMaterial;
import com.opc.core.domain.CoreMaterialTag;
import com.opc.core.domain.CoreMaterialUserAction;
import com.opc.core.domain.CoreTag;
import com.opc.core.mapper.CoreMaterialMapper;
import com.opc.core.mapper.CoreMaterialTagMapper;
import com.opc.core.mapper.CoreMaterialUserActionMapper;
import com.opc.core.mapper.CoreTagMapper;
import com.opc.core.service.ICoreMaterialService;

@Service
public class CoreMaterialServiceImpl implements ICoreMaterialService
{
    @Autowired
    private CoreMaterialMapper materialMapper;

    @Autowired
    private CoreMaterialTagMapper materialTagMapper;

    @Autowired
    private CoreMaterialUserActionMapper userActionMapper;

    @Autowired
    private CoreTagMapper tagMapper;

    @Autowired
    private CoreMaterialAsyncService asyncService;

    @Override
    public List<CoreMaterial> selectMaterialList(CoreMaterial material)
    {
        return materialMapper.selectMaterialList(material);
    }

    @Override
    public CoreMaterial selectMaterialById(Long id)
    {
        CoreMaterial material = materialMapper.selectMaterialById(id);
        if (material != null)
        {
            List<CoreTag> tags = materialTagMapper.selectTagsByMaterialId(id);
            material.setTags(tags);
        }
        return material;
    }

    @Override
    @Transactional
    public int insertMaterial(CoreMaterial material)
    {
        // 上线时间默认为创建时间（当前时间）
        if (material.getOnlineTime() == null)
        {
            material.setOnlineTime(Instant.now());
        }
        int result = materialMapper.insertMaterial(material);
        if (result > 0)
        {
            // 处理原有标签关联
            if (material.getTagIds() != null && !material.getTagIds().isEmpty())
            {
                materialTagMapper.deleteMaterialTagByMaterialId(material.getId());
                for (Long tagId : material.getTagIds())
                {
                    materialMapper.insertMaterialTag(material.getId(), tagId);
                }
            }
            // 自动匹配内容中的标签
            autoMatchTagsForMaterial(material);
        }
        return result;
    }

    @Override
    @Transactional
    public int updateMaterial(CoreMaterial material)
    {
        int result = materialMapper.updateMaterial(material);
        if (result > 0)
        {
            // 处理原有标签关联
            if (material.getTagIds() != null)
            {
                materialTagMapper.deleteMaterialTagByMaterialId(material.getId());
                for (Long tagId : material.getTagIds())
                {
                    materialMapper.insertMaterialTag(material.getId(), tagId);
                }
            }
            // 自动匹配内容中的标签
            autoMatchTagsForMaterial(material);
        }
        return result;
    }

    @Override
    @Transactional
    public int deleteMaterialById(Long id)
    {
        materialTagMapper.deleteMaterialTagByMaterialId(id);
        return materialMapper.deleteMaterialById(id);
    }

    @Override
    @Transactional
    public int deleteMaterialByIds(Long[] ids)
    {
        for (Long id : ids)
        {
            materialTagMapper.deleteMaterialTagByMaterialId(id);
        }
        return materialMapper.deleteMaterialByIds(ids);
    }

    @Override
    public int changeStatus(Long id, String status)
    {
        CoreMaterial material = new CoreMaterial();
        material.setId(id);
        material.setStatus(status);
        // 上线时记录上线时间，下线时记录下线时间
        if ("0".equals(status))
        {
            material.setOnlineTime(Instant.now());
        }
        else if ("1".equals(status))
        {
            material.setOfflineTime(Instant.now());
        }
        return materialMapper.changeStatus(material);
    }

    @Override
    public int changeTop(Long id, String isTop)
    {
        CoreMaterial material = new CoreMaterial();
        material.setId(id);
        material.setIsTop(isTop);
        // 置顶时记录置顶时间，取消置顶时记录取消置顶时间
        if ("1".equals(isTop))
        {
            material.setTopTime(Instant.now());
        }
        else if ("0".equals(isTop))
        {
            material.setUntopTime(Instant.now());
        }
        return materialMapper.changeTop(material);
    }

    @Override
    public List<CoreMaterial> selectMaterialListByTagId(Long tagId, String status, Integer maxPackageType)
    {
        return materialMapper.selectMaterialListByTagId(tagId, status, maxPackageType);
    }

    @Override
    public int incrementViewCount(Long id)
    {
        // 异步增加查看数
        asyncService.incrementViewCount(id);
        return 1;
    }

    @Override
    @Transactional
    public boolean likeMaterial(Long materialId, Long userId, boolean isLike)
    {
        CoreMaterial material = materialMapper.selectMaterialById(materialId);
        if (material == null)
        {
            return false;
        }

        int hasLiked = userActionMapper.checkUserAction(materialId, userId, "like");

        if (isLike)
        {
            // 目标是点赞状态
            if (hasLiked > 0)
            {
                // 已经点赞了，幂等返回成功
                return true;
            }

            // 先删除可能的不喜欢记录
            int hasDisliked = userActionMapper.checkUserAction(materialId, userId, "dislike");
            if (hasDisliked > 0)
            {
                userActionMapper.deleteUserAction(materialId, userId, "dislike");
                // 异步切换：减少不喜欢数，增加点赞数
                asyncService.switchToLike(materialId);
            }
            else
            {
                // 异步增加点赞数
                asyncService.incrementLikeCount(materialId);
            }

            // 添加点赞记录
            CoreMaterialUserAction action = new CoreMaterialUserAction();
            action.setMaterialId(materialId);
            action.setUserId(userId);
            action.setActionType("like");
            userActionMapper.insertUserAction(action);
            return true;
        }
        else
        {
            // 目标是取消点赞状态
            if (hasLiked == 0)
            {
                // 已经取消点赞了，幂等返回成功
                return true;
            }

            // 取消点赞
            userActionMapper.deleteUserAction(materialId, userId, "like");

            // 异步减少点赞数
            asyncService.decrementLikeCount(materialId);
            return true;
        }
    }

    @Override
    @Transactional
    public boolean dislikeMaterial(Long materialId, Long userId, boolean isDislike)
    {
        CoreMaterial material = materialMapper.selectMaterialById(materialId);
        if (material == null)
        {
            return false;
        }

        int hasDisliked = userActionMapper.checkUserAction(materialId, userId, "dislike");

        if (isDislike)
        {
            // 目标是不喜欢状态
            if (hasDisliked > 0)
            {
                // 已经不喜欢了，幂等返回成功
                return true;
            }

            // 先删除可能的点赞记录
            int hasLiked = userActionMapper.checkUserAction(materialId, userId, "like");
            if (hasLiked > 0)
            {
                userActionMapper.deleteUserAction(materialId, userId, "like");
                // 异步切换：减少点赞数，增加不喜欢数
                asyncService.switchToDislike(materialId);
            }
            else
            {
                // 异步增加不喜欢数
                asyncService.incrementDislikeCount(materialId);
            }

            // 添加不喜欢记录
            CoreMaterialUserAction action = new CoreMaterialUserAction();
            action.setMaterialId(materialId);
            action.setUserId(userId);
            action.setActionType("dislike");
            userActionMapper.insertUserAction(action);
            return true;
        }
        else
        {
            // 目标是取消不喜欢状态
            if (hasDisliked == 0)
            {
                // 已经取消不喜欢了，幂等返回成功
                return true;
            }

            // 取消不喜欢
            userActionMapper.deleteUserAction(materialId, userId, "dislike");

            // 异步减少不喜欢数
            asyncService.decrementDislikeCount(materialId);
            return true;
        }
    }

    @Override
    public String getUserActionStatus(Long materialId, Long userId)
    {
        if (userActionMapper.checkUserAction(materialId, userId, "like") > 0)
        {
            return "like";
        }
        if (userActionMapper.checkUserAction(materialId, userId, "dislike") > 0)
        {
            return "dislike";
        }
        return "none";
    }

    @Override
    public CoreMaterial selectMaterialByOriginalId(String originalId)
    {
        return materialMapper.selectMaterialByOriginalId(originalId);
    }

    @Override
    public CoreMaterial selectLatestMaterialByCategoryId(Long categoryId, String status)
    {
        return materialMapper.selectLatestMaterialByCategoryId(categoryId, status);
    }

    @Override
    public List<CoreMaterial> selectMaterialListByCategoryIdExcludeLatest(Long categoryId, String status)
    {
        return materialMapper.selectMaterialListByCategoryIdExcludeLatest(categoryId, status);
    }

    /**
     * 自动匹配内容中的标签并建立关联
     * 从 core_tag 表获取标签，匹配素材标题、正文、总结中的标签名称
     * 
     * @param material 素材对象
     */
    private void autoMatchTagsForMaterial(CoreMaterial material)
    {
        if (material.getId() == null)
        {
            return;
        }

        // 获取所有启用的标签
        List<CoreTag> allTags = tagMapper.selectAllActiveTags();
        if (allTags == null || allTags.isEmpty())
        {
            return;
        }

        // 构建需要匹配的内容文本（标题 + 正文 + 总结 + 原标题 + 原始内容）
        StringBuilder contentBuilder = new StringBuilder();
        if (material.getTitle() != null)
        {
            contentBuilder.append(material.getTitle()).append(" ");
        }
        if (material.getContent() != null)
        {
            contentBuilder.append(material.getContent()).append(" ");
        }
        if (material.getSummary() != null)
        {
            contentBuilder.append(material.getSummary()).append(" ");
        }
        if (material.getOriginalTitle() != null)
        {
            contentBuilder.append(material.getOriginalTitle()).append(" ");
        }
        if (material.getOriginalContent() != null)
        {
            contentBuilder.append(material.getOriginalContent());
        }
        String contentText = contentBuilder.toString();

        if (contentText.trim().isEmpty())
        {
            return;
        }

        // 查找匹配的标签
        Set<Long> matchedTagIds = new HashSet<>();
        for (CoreTag tag : allTags)
        {
            if (tag.getTagName() != null && !tag.getTagName().trim().isEmpty())
            {
                // 检查内容中是否包含标签名称（不区分大小写）
                if (contentText.toLowerCase().contains(tag.getTagName().toLowerCase()))
                {
                    matchedTagIds.add(tag.getId());
                }
            }
        }

        // 保存匹配的标签关联
        if (!matchedTagIds.isEmpty())
        {
            List<CoreMaterialTag> tagList = new ArrayList<>();
            for (Long tagId : matchedTagIds)
            {
                // 检查是否已存在关联，避免重复
                if (!materialTagMapper.checkMaterialTagExists(material.getId(), tagId))
                {
                    CoreMaterialTag materialTag = new CoreMaterialTag();
                    materialTag.setMaterialId(material.getId());
                    materialTag.setTagId(tagId);
                    materialTag.setCreateBy(material.getCreateBy());
                    tagList.add(materialTag);
                }
            }

            if (!tagList.isEmpty())
            {
                materialTagMapper.batchInsertMaterialTag(tagList);
            }
        }
    }
}
