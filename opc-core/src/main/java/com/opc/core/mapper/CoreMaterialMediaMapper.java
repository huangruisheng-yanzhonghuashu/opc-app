package com.opc.core.mapper;

import java.util.List;
import com.opc.core.domain.CoreMaterialMedia;

/**
 * 素材媒体文件Mapper接口
 * 
 * @author opc
 */
public interface CoreMaterialMediaMapper 
{
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
     * 删除素材媒体文件
     * 
     * @param id 素材媒体文件主键
     * @return 结果
     */
    public int deleteMaterialMediaById(Long id);

    /**
     * 批量删除素材媒体文件
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteMaterialMediaByIds(Long[] ids);

    /**
     * 根据素材ID删除媒体文件
     * 
     * @param materialId 素材ID
     * @return 结果
     */
    public int deleteMaterialMediaByMaterialId(Long materialId);
}
