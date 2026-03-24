package com.opc.mobile.controller;

import java.util.List;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.opc.common.core.controller.BaseController;
import com.opc.common.core.page.TableDataInfo;
import com.opc.core.domain.CoreBanner;
import com.opc.core.service.ICoreBannerService;
import com.opc.mobile.dto.BannerQueryDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 移动端首页接口
 *
 * @author opc
 */
@Tag(name = "会员首页", description = "移动端首页相关接口")
@RestController
@RequestMapping("/mobile/home")
public class MobileHomeController extends BaseController
{
    @Autowired
    private ICoreBannerService bannerService;

    /**
     * 分页查询Banner列表
     *
     * @param queryDTO 查询参数
     * @return 分页数据
     */
    @Operation(summary = "获取Banner列表", description = "分页查询Banner列表，按sortOrder升序排序")
    @Parameter(name = "queryDTO", description = "Banner查询参数")
    @PostMapping("/banner/list")
    public TableDataInfo bannerList(@RequestBody BannerQueryDTO queryDTO)
    {
        CoreBanner banner = new CoreBanner();

        PageHelper.startPage(queryDTO.getPageNum(), queryDTO.getPageSize());
        List<CoreBanner> list = bannerService.selectBannerList(banner);
        return getDataTable(list);
    }
}
