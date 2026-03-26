package com.opc.core.service;

import java.util.List;
import com.opc.core.domain.CoreMaterial;

public interface ICoreMaterialService
{
    public List<CoreMaterial> selectMaterialList(CoreMaterial material);

    public CoreMaterial selectMaterialById(Long id);

    public int insertMaterial(CoreMaterial material);

    public int updateMaterial(CoreMaterial material);

    public int deleteMaterialById(Long id);

    public int deleteMaterialByIds(Long[] ids);

    public int changeStatus(Long id, String status);

    public int changeTop(Long id, String isTop);

    /**
     * 根据标签ID查询素材列表
     *
     * @param tagId 标签ID
     * @param status 状态
     * @param maxPackageType 最大套餐类型（会员只能看到packageType小于等于此值的素材）
     * @return 素材列表
     */
    public List<CoreMaterial> selectMaterialListByTagId(Long tagId, String status, Integer maxPackageType);

    /**
     * 原子增加查看数
     *
     * @param id 素材ID
     * @return 影响行数
     */
    int incrementViewCount(Long id);

    /**
     * 点赞/取消点赞
     *
     * @param materialId 素材ID
     * @param userId 用户ID
     * @param isLike true-点赞, false-取消点赞
     * @return 是否操作成功
     */
    boolean likeMaterial(Long materialId, Long userId, boolean isLike);

    /**
     * 不喜欢/取消不喜欢
     *
     * @param materialId 素材ID
     * @param userId 用户ID
     * @param isDislike true-不喜欢, false-取消不喜欢
     * @return 是否操作成功
     */
    boolean dislikeMaterial(Long materialId, Long userId, boolean isDislike);

    /**
     * 获取用户对素材的行为状态
     *
     * @param materialId 素材ID
     * @param userId 用户ID
     * @return actionType: like/dislike/none
     */
    String getUserActionStatus(Long materialId, Long userId);
}
