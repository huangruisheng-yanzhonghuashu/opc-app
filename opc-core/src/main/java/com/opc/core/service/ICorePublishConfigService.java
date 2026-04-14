package com.opc.core.service;

import com.opc.core.domain.CorePublishConfig;

import java.util.List;

/**
 * 发布配置Service接口
 *
 * @author opc
 * @date 2024-01-01
 */
public interface ICorePublishConfigService {

    /**
     * 查询发布配置
     *
     * @param id 发布配置主键
     * @return 发布配置
     */
    CorePublishConfig selectCorePublishConfigById(Long id);

    /**
     * 查询发布配置列表
     *
     * @param corePublishConfig 发布配置
     * @return 发布配置集合
     */
    List<CorePublishConfig> selectCorePublishConfigList(CorePublishConfig corePublishConfig);

    /**
     * 新增发布配置
     *
     * @param corePublishConfig 发布配置
     * @return 结果
     */
    int insertCorePublishConfig(CorePublishConfig corePublishConfig);

    /**
     * 修改发布配置
     *
     * @param corePublishConfig 发布配置
     * @return 结果
     */
    int updateCorePublishConfig(CorePublishConfig corePublishConfig);

    /**
     * 批量删除发布配置
     *
     * @param ids 需要删除的发布配置主键集合
     * @return 结果
     */
    int deleteCorePublishConfigByIds(Long[] ids);

    /**
     * 删除发布配置信息
     *
     * @param id 发布配置主键
     * @return 结果
     */
    int deleteCorePublishConfigById(Long id);
}
