package com.opc.core.service.impl;

import java.time.Instant;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.opc.core.domain.CoreMaterial;
import com.opc.core.domain.CoreMaterialUserAction;
import com.opc.core.domain.CoreTag;
import com.opc.core.mapper.CoreMaterialMapper;
import com.opc.core.mapper.CoreMaterialTagMapper;
import com.opc.core.mapper.CoreMaterialUserActionMapper;
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
        if (result > 0 && material.getTagIds() != null && !material.getTagIds().isEmpty())
        {
            materialTagMapper.deleteMaterialTagByMaterialId(material.getId());
            for (Long tagId : material.getTagIds())
            {
                materialMapper.insertMaterialTag(material.getId(), tagId);
            }
        }
        return result;
    }

    @Override
    @Transactional
    public int updateMaterial(CoreMaterial material)
    {
        int result = materialMapper.updateMaterial(material);
        if (result > 0 && material.getTagIds() != null)
        {
            materialTagMapper.deleteMaterialTagByMaterialId(material.getId());
            for (Long tagId : material.getTagIds())
            {
                materialMapper.insertMaterialTag(material.getId(), tagId);
            }
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
        return materialMapper.incrementViewCount(id);
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

        if (isLike && hasLiked == 0)
        {
            // 先删除可能的不喜欢记录
            int hasDisliked = userActionMapper.checkUserAction(materialId, userId, "dislike");
            if (hasDisliked > 0)
            {
                userActionMapper.deleteUserAction(materialId, userId, "dislike");
                material.setDislikeCount(Math.max(0, (material.getDislikeCount() == null ? 0 : material.getDislikeCount()) - 1));
            }

            // 添加点赞记录
            CoreMaterialUserAction action = new CoreMaterialUserAction();
            action.setMaterialId(materialId);
            action.setUserId(userId);
            action.setActionType("like");
            userActionMapper.insertUserAction(action);

            // 增加点赞数
            material.setLikeCount((material.getLikeCount() == null ? 0 : material.getLikeCount()) + 1);
            materialMapper.updateMaterial(material);
            return true;
        }
        else if (!isLike && hasLiked > 0)
        {
            // 取消点赞
            userActionMapper.deleteUserAction(materialId, userId, "like");

            // 减少点赞数
            material.setLikeCount(Math.max(0, (material.getLikeCount() == null ? 0 : material.getLikeCount()) - 1));
            materialMapper.updateMaterial(material);
            return true;
        }
        return false;
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

        if (isDislike && hasDisliked == 0)
        {
            // 先删除可能的点赞记录
            int hasLiked = userActionMapper.checkUserAction(materialId, userId, "like");
            if (hasLiked > 0)
            {
                userActionMapper.deleteUserAction(materialId, userId, "like");
                material.setLikeCount(Math.max(0, (material.getLikeCount() == null ? 0 : material.getLikeCount()) - 1));
            }

            // 添加不喜欢记录
            CoreMaterialUserAction action = new CoreMaterialUserAction();
            action.setMaterialId(materialId);
            action.setUserId(userId);
            action.setActionType("dislike");
            userActionMapper.insertUserAction(action);

            // 增加不喜欢数
            material.setDislikeCount((material.getDislikeCount() == null ? 0 : material.getDislikeCount()) + 1);
            materialMapper.updateMaterial(material);
            return true;
        }
        else if (!isDislike && hasDisliked > 0)
        {
            // 取消不喜欢
            userActionMapper.deleteUserAction(materialId, userId, "dislike");

            // 减少不喜欢数
            material.setDislikeCount(Math.max(0, (material.getDislikeCount() == null ? 0 : material.getDislikeCount()) - 1));
            materialMapper.updateMaterial(material);
            return true;
        }
        return false;
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
}
