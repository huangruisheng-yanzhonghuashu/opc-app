package com.opc.web.controller.core;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.opc.common.annotation.Log;
import com.opc.common.core.controller.BaseController;
import com.opc.common.core.domain.AjaxResult;
import com.opc.common.core.page.TableDataInfo;
import com.opc.common.enums.BusinessType;
import com.opc.core.domain.CoreCustomerService;
import com.opc.core.service.ICoreCustomerServiceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 客服配置Controller
 * 
 * @author opc
 */
@Tag(name = "客服配置", description = "客服配置相关操作")
@RestController
@RequestMapping("/core/customerService")
public class CoreCustomerServiceController extends BaseController
{
    @Autowired
    private ICoreCustomerServiceService customerServiceService;

    /**
     * 查询客服配置列表
     */
    @Operation(summary = "查询客服配置列表", description = "分页查询客服配置列表")
    @PreAuthorize("@ss.hasPermi('core:customerService:list')")
    @GetMapping("/list")
    public TableDataInfo list(CoreCustomerService customerService)
    {
        startPage();
        List<CoreCustomerService> list = customerServiceService.selectCustomerServiceList(customerService);
        return getDataTable(list);
    }

    /**
     * 根据ID获取客服配置详细信息
     */
    @Operation(summary = "获取客服配置详情", description = "根据客服ID获取详细信息")
    @Parameter(name = "id", description = "客服ID", required = true)
    @PreAuthorize("@ss.hasPermi('core:customerService:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(customerServiceService.selectCustomerServiceById(id));
    }

    /**
     * 获取默认客服配置
     */
    @Operation(summary = "获取默认客服", description = "获取默认客服配置信息")
    @PreAuthorize("@ss.hasPermi('core:customerService:query')")
    @GetMapping("/default")
    public AjaxResult getDefault()
    {
        return success(customerServiceService.selectDefaultCustomerService());
    }

    /**
     * 新增客服配置
     */
    @Operation(summary = "新增客服配置", description = "添加新的客服配置")
    @PreAuthorize("@ss.hasPermi('core:customerService:add')")
    @Log(title = "客服配置", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody CoreCustomerService customerService)
    {
        customerService.setCreateBy(getUsername());
        return toAjax(customerServiceService.insertCustomerService(customerService));
    }

    /**
     * 修改客服配置
     */
    @Operation(summary = "修改客服配置", description = "修改客服配置信息")
    @PreAuthorize("@ss.hasPermi('core:customerService:edit')")
    @Log(title = "客服配置", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody CoreCustomerService customerService)
    {
        customerService.setUpdateBy(getUsername());
        return toAjax(customerServiceService.updateCustomerService(customerService));
    }

    /**
     * 删除客服配置
     */
    @Operation(summary = "删除客服配置", description = "根据ID删除客服配置")
    @Parameter(name = "id", description = "客服ID", required = true)
    @PreAuthorize("@ss.hasPermi('core:customerService:remove')")
    @Log(title = "客服配置", businessType = BusinessType.DELETE)
    @DeleteMapping("/{id}")
    public AjaxResult remove(@PathVariable Long id)
    {
        return toAjax(customerServiceService.deleteCustomerServiceById(id));
    }

    /**
     * 批量删除客服配置
     */
    @Operation(summary = "批量删除客服配置", description = "批量删除客服配置")
    @PreAuthorize("@ss.hasPermi('core:customerService:remove')")
    @Log(title = "客服配置", businessType = BusinessType.DELETE)
    @DeleteMapping("/batch/{ids}")
    public AjaxResult removeBatch(@PathVariable Long[] ids)
    {
        return toAjax(customerServiceService.deleteCustomerServiceByIds(ids));
    }
}
