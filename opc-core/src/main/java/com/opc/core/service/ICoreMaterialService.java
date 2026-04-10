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

    /**
     * 根据 originalId 查询素材
     *
     * @param originalId 原ID
     * @return 素材对象
     */
    CoreMaterial selectMaterialByOriginalId(String originalId);

    /**
     * 根据二级分类查询最新期数的素材（期数最大的记录）
     *
     * @param categoryId 二级分类ID
     * @param status 状态
     * @return 最新期数素材
     */
    CoreMaterial selectLatestMaterialByCategoryId(Long categoryId, String status);

    /**
     * 根据二级分类查询素材列表（排除期数最大的记录）
     *
     * @param categoryId 二级分类ID
     * @param status 状态
     * @return 素材列表（排除最新期数）
     */
    List<CoreMaterial> selectMaterialListByCategoryIdExcludeLatest(Long categoryId, String status);

    /**
     * 批量上架素材
     *
     * @param ids 素材ID数组
     * @return 影响行数
     */
    int batchOnlineMaterial(Long[] ids);

    /**
     * 批量下架素材
     *
     * @param ids 素材ID数组
     * @return 影响行数
     */
    int batchOfflineMaterial(Long[] ids);
}
