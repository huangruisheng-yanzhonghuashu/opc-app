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
import com.opc.core.domain.CorePackageOrder;
import com.opc.core.service.ICorePackageOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "套餐订单管理", description = "套餐订单相关操作")
@RestController
@RequestMapping("/core/packageOrder")
public class CorePackageOrderController extends BaseController
{
    @Autowired
    private ICorePackageOrderService orderService;

    @Operation(summary = "获取订单列表", description = "分页查询订单列表")
    @PreAuthorize("@ss.hasPermi('core:packageOrder:list')")
    @GetMapping("/list")
    public TableDataInfo list(CorePackageOrder order)
    {
        startPage();
        List<CorePackageOrder> list = orderService.selectOrderList(order);
        return getDataTable(list);
    }

    @Operation(summary = "获取订单详情", description = "根据订单ID获取详细信息")
    @Parameter(name = "id", description = "订单ID", required = true)
    @PreAuthorize("@ss.hasPermi('core:packageOrder:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable Long id)
    {
        return success(orderService.selectOrderById(id));
    }

    @Operation(summary = "新增订单", description = "新增订单信息")
    @PreAuthorize("@ss.hasPermi('core:packageOrder:add')")
    @Log(title = "套餐订单管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody CorePackageOrder order)
    {
        order.setCreateBy(getUsername());
        return toAjax(orderService.insertOrder(order));
    }

    @Operation(summary = "修改订单", description = "修改订单信息")
    @PreAuthorize("@ss.hasPermi('core:packageOrder:edit')")
    @Log(title = "套餐订单管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody CorePackageOrder order)
    {
        order.setUpdateBy(getUsername());
        return toAjax(orderService.updateOrder(order));
    }

    @Operation(summary = "删除订单", description = "根据订单ID删除订单")
    @Parameter(name = "id", description = "订单ID", required = true)
    @PreAuthorize("@ss.hasPermi('core:packageOrder:remove')")
    @Log(title = "套餐订单管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{id}")
    public AjaxResult remove(@PathVariable Long id)
    {
        return toAjax(orderService.deleteOrderById(id));
    }

    @Operation(summary = "批量删除订单", description = "批量删除订单")
    @Parameter(name = "ids", description = "订单ID数组", required = true)
    @PreAuthorize("@ss.hasPermi('core:packageOrder:remove')")
    @Log(title = "套餐订单管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/batch/{ids}")
    public AjaxResult batchRemove(@PathVariable Long[] ids)
    {
        return toAjax(orderService.deleteOrderByIds(ids));
    }
}
