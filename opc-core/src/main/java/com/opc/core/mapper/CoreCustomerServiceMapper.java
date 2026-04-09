package com.opc.core.mapper;

import java.util.List;
import com.opc.core.domain.CoreCustomerService;

/**
 * 客服配置Mapper接口
 * 
 * @author opc
 */
public interface CoreCustomerServiceMapper
{
    /**
     * 查询客服配置列表
     * 
     * @param customerService 客服配置
     * @return 客服配置集合
     */
    public List<CoreCustomerService> selectCustomerServiceList(CoreCustomerService customerService);

    /**
     * 根据ID查询客服配置
     * 
     * @param id 客服ID
     * @return 客服配置
     */
    public CoreCustomerService selectCustomerServiceById(Long id);

    /**
     * 查询默认客服配置
     * 
     * @return 客服配置
     */
    public CoreCustomerService selectDefaultCustomerService();

    /**
     * 新增客服配置
     * 
     * @param customerService 客服配置
     * @return 结果
     */
    public int insertCustomerService(CoreCustomerService customerService);

    /**
     * 修改客服配置
     * 
     * @param customerService 客服配置
     * @return 结果
     */
    public int updateCustomerService(CoreCustomerService customerService);

    /**
     * 删除客服配置
     * 
     * @param id 客服ID
     * @return 结果
     */
    public int deleteCustomerServiceById(Long id);

    /**
     * 批量删除客服配置
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteCustomerServiceByIds(Long[] ids);

    /**
     * 取消所有默认客服
     * 
     * @return 结果
     */
    public int cancelAllDefault();
}
