package com.opc.core.service;

import java.util.List;
import com.opc.core.domain.CoreMaterial;
import com.opc.core.domain.CoreMaterialMedia;

/**
 * 素材媒体文件Service接口
 * 
 * @author opc
 */
public interface ICoreMaterialMediaService {

    /**
     * 查询素材媒体文件
     * 
     * @param id 素材媒体文件主键
     * @return 素材媒体文件
     */
    public CoreMaterialMedia selectMaterialMediaById(Long id);

    /**
     * 查询素材媒体文件列表
     * 
     * @param materialMedia 素材媒体文件
     * @return 素材媒体文件集合
     */
    public List<CoreMaterialMedia> selectMaterialMediaList(CoreMaterialMedia materialMedia);

    /**
     * 根据素材ID查询媒体文件列表
     * 
     * @param materialId 素材ID
     * @return 素材媒体文件集合
     */
    public List<CoreMaterialMedia> selectMaterialMediaByMaterialId(Long materialId);

    /**
     * 新增素材媒体文件
     * 
     * @param materialMedia 素材媒体文件
     * @return 结果
     */
    public int insertMaterialMedia(CoreMaterialMedia materialMedia);

    /**
     * 修改素材媒体文件
     * 
     * @param materialMedia 素材媒体文件
     * @return 结果
     */
    public int updateMaterialMedia(CoreMaterialMedia materialMedia);

    /**
     * 批量删除素材媒体文件
     * 
     * @param ids 需要删除的素材媒体文件主键集合
     * @return 结果
     */
    public int deleteMaterialMediaByIds(Long[] ids);

    /**
     * 删除素材媒体文件信息
     * 
     * @param id 素材媒体文件主键
     * @return 结果
     */
    public int deleteMaterialMediaById(Long id);

    /**
     * 异步下载素材中的媒体文件（图片/视频）
     *
     * @param material 素材对象
     */
    void downloadMediaAsync(CoreMaterial material);

    /**
     * 保存素材媒体文件
     * 
     * @param materialId 素材ID
     * @param mediaType 媒体类型（image/video）
     * @param fileUrl 文件服务器URL
     * @param sortOrder 排序号
     * @return 结果
     */
    public int saveMaterialMedia(Long materialId, String mediaType, String fileUrl, Integer sortOrder);

    /**
     * 批量保存素材媒体文件
     * 
     * @param materialId 素材ID
     * @param mediaList 媒体文件列表
     * @return 结果
     */
    public int batchSaveMaterialMedia(Long materialId, List<CoreMaterialMedia> mediaList);
}
