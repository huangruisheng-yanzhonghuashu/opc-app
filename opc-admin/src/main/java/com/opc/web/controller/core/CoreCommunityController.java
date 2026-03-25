package com.opc.web.controller.core;

import java.util.List;
import jakarta.servlet.http.HttpServletResponse;
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
import com.opc.common.utils.poi.ExcelUtil;
import com.opc.core.domain.CoreCommunity;
import com.opc.core.service.ICoreCommunityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "社区管理", description = "看世界社区相关操作")
@RestController
@RequestMapping("/core/community")
public class CoreCommunityController extends BaseController
{
    @Autowired
    private ICoreCommunityService communityService;

    @Operation(summary = "获取社区列表", description = "分页查询社区列表")
    @PreAuthorize("@ss.hasPermi('core:community:list')")
    @GetMapping("/list")
    public TableDataInfo list(CoreCommunity community)
    {
        startPage();
        List<CoreCommunity> list = communityService.selectCommunityList(community);
        return getDataTable(list);
    }

    @Log(title = "社区管理", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('core:community:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, CoreCommunity community)
    {
        List<CoreCommunity> list = communityService.selectCommunityList(community);
        ExcelUtil<CoreCommunity> util = new ExcelUtil<CoreCommunity>(CoreCommunity.class);
        util.exportExcel(response, list, "社区数据");
    }

    @Operation(summary = "获取社区详情", description = "根据社区ID获取详细信息")
    @Parameter(name = "id", description = "社区ID", required = true)
    @PreAuthorize("@ss.hasPermi('core:community:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable Long id)
    {
        return success(communityService.selectCommunityById(id));
    }

    @Operation(summary = "新增社区", description = "新增社区信息")
    @PreAuthorize("@ss.hasPermi('core:community:add')")
    @Log(title = "社区管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody CoreCommunity community)
    {
        if (!communityService.checkCommunityNameUnique(community))
        {
            return error("新增社区'" + community.getName() + "'失败，社区名称已存在");
        }
        community.setCreateBy(getUsername());
        return toAjax(communityService.insertCommunity(community));
    }

    @Operation(summary = "修改社区", description = "修改社区信息")
    @PreAuthorize("@ss.hasPermi('core:community:edit')")
    @Log(title = "社区管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody CoreCommunity community)
    {
        if (!communityService.checkCommunityNameUnique(community))
        {
            return error("修改社区'" + community.getName() + "'失败，社区名称已存在");
        }
        community.setUpdateBy(getUsername());
        return toAjax(communityService.updateCommunity(community));
    }

    @Operation(summary = "删除社区", description = "根据社区ID删除社区")
    @Parameter(name = "ids", description = "社区ID数组", required = true)
    @PreAuthorize("@ss.hasPermi('core:community:remove')")
    @Log(title = "社区管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(communityService.deleteCommunityByIds(ids));
    }
}
