package com.opc.core.service.impl;

import com.opc.common.utils.DateUtils;
import com.opc.core.domain.CorePublishConfig;
import com.opc.core.mapper.CorePublishConfigMapper;
import com.opc.core.service.ICorePublishConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 发布配置Service业务层处理
 *
 * @author opc
 * @date 2024-01-01
 */
@Service
public class CorePublishConfigServiceImpl implements ICorePublishConfigService {

    @Autowired
    private CorePublishConfigMapper corePublishConfigMapper;

    /**
     * 查询发布配置
     *
     * @param id 发布配置主键
     * @return 发布配置
     */
    @Override
    public CorePublishConfig selectCorePublishConfigById(Long id) {
        return corePublishConfigMapper.selectCorePublishConfigById(id);
    }

    /**
     * 查询发布配置列表
     *
     * @param corePublishConfig 发布配置
     * @return 发布配置
     */
    @Override
    public List<CorePublishConfig> selectCorePublishConfigList(CorePublishConfig corePublishConfig) {
        return corePublishConfigMapper.selectCorePublishConfigList(corePublishConfig);
    }

    /**
     * 新增发布配置
     *
     * @param corePublishConfig 发布配置
     * @return 结果
     */
    @Override
    public int insertCorePublishConfig(CorePublishConfig corePublishConfig) {
        corePublishConfig.setCreateTime(DateUtils.getNowDate());
        return corePublishConfigMapper.insertCorePublishConfig(corePublishConfig);
    }

    /**
     * 修改发布配置
     *
     * @param corePublishConfig 发布配置
     * @return 结果
     */
    @Override
    public int updateCorePublishConfig(CorePublishConfig corePublishConfig) {
        corePublishConfig.setUpdateTime(DateUtils.getNowDate());
        return corePublishConfigMapper.updateCorePublishConfig(corePublishConfig);
    }

    /**
     * 批量删除发布配置
     *
     * @param ids 需要删除的发布配置主键
     * @return 结果
     */
    @Override
    public int deleteCorePublishConfigByIds(Long[] ids) {
        return corePublishConfigMapper.deleteCorePublishConfigByIds(ids);
    }

    /**
     * 删除发布配置信息
     *
     * @param id 发布配置主键
     * @return 结果
     */
    @Override
    public int deleteCorePublishConfigById(Long id) {
        return corePublishConfigMapper.deleteCorePublishConfigById(id);
    }
}
