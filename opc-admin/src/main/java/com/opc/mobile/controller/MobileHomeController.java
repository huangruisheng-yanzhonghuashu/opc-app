package com.opc.mobile.controller;

import java.util.List;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.opc.common.core.controller.BaseController;
import com.opc.common.core.domain.AjaxResult;
import com.opc.common.core.page.TableDataInfo;
import com.opc.core.domain.CoreBanner;
import com.opc.core.domain.CoreMaterial;
import com.opc.core.domain.CoreTag;
import com.opc.core.service.ICoreBannerService;
import com.opc.core.service.ICoreMaterialService;
import com.opc.core.service.ICoreTagService;
import com.opc.mobile.dto.BannerQueryDTO;
import com.opc.mobile.dto.MaterialByTagQueryDTO;
import com.opc.mobile.dto.TopMaterialQueryDTO;
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

    @Autowired
    private ICoreMaterialService materialService;

    @Autowired
    private ICoreTagService tagService;

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

    /**
     * 分页查询置顶素材列表
     *
     * @param queryDTO 查询参数
     * @return 分页数据
     */
    @Operation(summary = "获取置顶内容列表", description = "查询置顶素材列表，按发布时间倒序排序")
    @Parameter(name = "queryDTO", description = "置顶素材查询参数")
    @PostMapping("/material/top/list")
    public TableDataInfo topMaterialList(@RequestBody TopMaterialQueryDTO queryDTO)
    {
        CoreMaterial material = new CoreMaterial();
        // 只查询置顶的素材
        material.setIsTop("1");
        // 只查询上线的素材
        material.setStatus("0");

        PageHelper.startPage(queryDTO.getPageNum(), queryDTO.getPageSize());
        PageHelper.orderBy("publish_time desc");
        List<CoreMaterial> list = materialService.selectMaterialList(material);
        return getDataTable(list);
    }

    /**
     * 查询标签列表
     *
     * @return 标签列表
     */
    @Operation(summary = "获取标签列表", description = "查询启用的标签列表，按sortOrder升序排序")
    @GetMapping("/tag/list")
    public AjaxResult tagList()
    {
        CoreTag tag = new CoreTag();
        // 只查询启用的标签
        tag.setStatus("0");

        List<CoreTag> list = tagService.selectTagList(tag);
        return success(list);
    }

    /**
     * 根据标签查询素材列表
     *
     * @param queryDTO 查询参数
     * @return 分页数据
     */
    @Operation(summary = "根据标签查询内容列表", description = "根据标签ID查询素材列表，按发布时间倒序排序")
    @PostMapping("/material/byTag/list")
    public TableDataInfo materialListByTag(@RequestBody MaterialByTagQueryDTO queryDTO)
    {
        PageHelper.startPage(queryDTO.getPageNum(), queryDTO.getPageSize());
        List<CoreMaterial> list = materialService.selectMaterialListByTagId(queryDTO.getTagId(), "0");
        return getDataTable(list);
    }
}
