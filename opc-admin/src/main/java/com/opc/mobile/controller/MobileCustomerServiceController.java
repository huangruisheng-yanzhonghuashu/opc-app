package com.opc.mobile.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.opc.common.core.controller.BaseController;
import com.opc.common.core.domain.AjaxResult;
import com.opc.core.domain.CoreCustomerService;
import com.opc.core.service.ICoreCustomerServiceService;
import com.opc.mobile.vo.CoreCustomerServiceVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 移动端客服接口
 * 
 * @author opc
 */
@Tag(name = "APP客服", description = "移动端客服相关接口")
@RestController
@RequestMapping("/mobile/customerService")
public class MobileCustomerServiceController extends BaseController
{
    @Autowired
    private ICoreCustomerServiceService customerServiceService;

    /**
     * 获取默认客服配置
     * 
     * @return 默认客服配置信息
     */
    @Operation(summary = "获取默认客服", description = "获取默认启用的客服配置信息，无需登录")
    @ApiResponse(responseCode = "200", description = "成功", content = @Content(schema = @Schema(implementation = CoreCustomerServiceVO.class)))
    @GetMapping("/default")
    public AjaxResult getDefaultCustomerService()
    {
        CoreCustomerService customerService = customerServiceService.selectDefaultCustomerService();
        if (customerService == null)
        {
            return AjaxResult.success("暂无客服配置", null);
        }
        CoreCustomerServiceVO vo = new CoreCustomerServiceVO();
        BeanUtils.copyProperties(customerService, vo);
        return AjaxResult.success(vo);
    }

    /**
     * 获取所有启用的客服列表
     * 
     * @return 客服列表
     */
    @Operation(summary = "获取客服列表", description = "获取所有启用的客服配置列表，无需登录")
    @ApiResponse(responseCode = "200", description = "成功", content = @Content(schema = @Schema(implementation = CoreCustomerServiceVO.class)))
    @GetMapping("/list")
    public AjaxResult listCustomerService()
    {
        CoreCustomerService query = new CoreCustomerService();
        query.setStatus("0"); // 只查询启用的
        List<CoreCustomerService> list = customerServiceService.selectCustomerServiceList(query);
        List<CoreCustomerServiceVO> voList = list.stream().map(item -> {
            CoreCustomerServiceVO vo = new CoreCustomerServiceVO();
            BeanUtils.copyProperties(item, vo);
            return vo;
        }).collect(Collectors.toList());
        return AjaxResult.success(voList);
    }
}
