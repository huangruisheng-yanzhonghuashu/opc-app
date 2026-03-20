package com.opc.web.controller.core;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.opc.common.annotation.Log;
import com.opc.common.core.controller.BaseController;
import com.opc.common.core.domain.AjaxResult;
import com.opc.common.core.page.TableDataInfo;
import com.opc.common.enums.BusinessType;
import com.opc.core.domain.CoreMemberConfig;
import com.opc.core.service.ICoreMemberConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "会员页面配置", description = "会员页面配置相关操作")
@RestController
@RequestMapping("/core/memberConfig")
public class CoreMemberConfigController extends BaseController
{
    @Autowired
    private ICoreMemberConfigService configService;

    @Operation(summary = "获取会员页面配置列表", description = "分页查询配置列表")
    @PreAuthorize("@ss.hasPermi('core:memberConfig:list')")
    @GetMapping("/list")
    public TableDataInfo list(CoreMemberConfig config)
    {
        startPage();
        List<CoreMemberConfig> list = configService.selectConfigList(config);
        return getDataTable(list);
    }

    @Operation(summary = "获取会员页面配置详情", description = "根据配置ID获取详细信息")
    @Parameter(name = "id", description = "配置ID", required = true)
    @PreAuthorize("@ss.hasPermi('core:memberConfig:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable Long id)
    {
        return success(configService.selectConfigById(id));
    }

    @Operation(summary = "根据类型获取配置", description = "根据配置类型获取配置信息")
    @Parameter(name = "type", description = "配置类型(banner/vip_guide)", required = true)
    @PreAuthorize("@ss.hasPermi('core:memberConfig:query')")
    @GetMapping("/type/{type}")
    public AjaxResult getByType(@PathVariable String type)
    {
        return success(configService.selectConfigByType(type));
    }

    @Operation(summary = "保存会员页面配置", description = "保存或更新配置信息")
    @PreAuthorize("@ss.hasPermi('core:memberConfig:save')")
    @Log(title = "会员页面配置", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult save(@Validated @RequestBody CoreMemberConfig config)
    {
        config.setUpdateBy(getUsername());
        return toAjax(configService.saveConfig(config));
    }
}
