package com.opc.core.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.opc.core.domain.CoreCustomerService;
import com.opc.core.mapper.CoreCustomerServiceMapper;
import com.opc.core.service.ICoreCustomerServiceService;

/**
 * 客服配置Service业务层处理
 * 
 * @author opc
 */
@Service
public class CoreCustomerServiceServiceImpl implements ICoreCustomerServiceService
{
    @Autowired
    private CoreCustomerServiceMapper customerServiceMapper;

    /**
     * 查询客服配置列表
     */
    @Override
    public List<CoreCustomerService> selectCustomerServiceList(CoreCustomerService customerService)
    {
        return customerServiceMapper.selectCustomerServiceList(customerService);
    }

    /**
     * 根据ID查询客服配置
     */
    @Override
    public CoreCustomerService selectCustomerServiceById(Long id)
    {
        return customerServiceMapper.selectCustomerServiceById(id);
    }

    /**
     * 查询默认客服配置
     */
    @Override
    public CoreCustomerService selectDefaultCustomerService()
    {
        return customerServiceMapper.selectDefaultCustomerService();
    }

    /**
     * 新增客服配置
     */
    @Override
    public int insertCustomerService(CoreCustomerService customerService)
    {
        // 如果设置为默认，取消其他默认
        if ("0".equals(customerService.getIsDefault()))
        {
            customerServiceMapper.cancelAllDefault();
        }
        return customerServiceMapper.insertCustomerService(customerService);
    }

    /**
     * 修改客服配置
     */
    @Override
    public int updateCustomerService(CoreCustomerService customerService)
    {
        // 如果设置为默认，取消其他默认
        if ("0".equals(customerService.getIsDefault()))
        {
            customerServiceMapper.cancelAllDefault();
        }
        return customerServiceMapper.updateCustomerService(customerService);
    }

    /**
     * 删除客服配置
     */
    @Override
    public int deleteCustomerServiceById(Long id)
    {
        return customerServiceMapper.deleteCustomerServiceById(id);
    }

    /**
     * 批量删除客服配置
     */
    @Override
    public int deleteCustomerServiceByIds(Long[] ids)
    {
        return customerServiceMapper.deleteCustomerServiceByIds(ids);
    }
}
