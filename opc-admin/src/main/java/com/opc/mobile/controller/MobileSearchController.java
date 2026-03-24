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
import com.opc.core.domain.CoreMaterial;
import com.opc.core.domain.CoreSearchHotword;
import com.opc.core.service.ICoreMaterialService;
import com.opc.core.service.ICoreSearchHotwordService;
import com.opc.mobile.dto.MaterialSearchDTO;
import com.opc.mobile.dto.SearchHotwordQueryDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 移动端搜索接口
 *
 * @author opc
 */
@Tag(name = "会员搜索", description = "移动端会员搜索相关接口")
@RestController
@RequestMapping("/mobile/search")
public class MobileSearchController extends BaseController
{
    @Autowired
    private ICoreSearchHotwordService searchHotwordService;

    @Autowired
    private ICoreMaterialService materialService;

    /**
     * 分页查询搜索热词列表
     *
     * @param queryDTO 查询参数
     * @return 分页数据
     */
    @Operation(summary = "获取搜索热词列表", description = "分页查询搜索热词列表，支持按名称模糊查询")
    @PostMapping("/hotword/list")
    public TableDataInfo hotwordList(@RequestBody SearchHotwordQueryDTO queryDTO)
    {
        CoreSearchHotword searchHotword = new CoreSearchHotword();
        searchHotword.setKeyword(queryDTO.getKeyword());
        searchHotword.setStatus("0");

        PageHelper.startPage(queryDTO.getPageNum(), queryDTO.getPageSize());
        List<CoreSearchHotword> list = searchHotwordService.selectSearchHotwordList(searchHotword);
        return getDataTable(list);
    }

    /**
     * 根据标题模糊搜索素材
     *
     * @param searchDTO 搜索参数
     * @return 分页数据
     */
    @Operation(summary = "全局搜索列表", description = "根据关键字模糊搜索素材标题，分页返回结果")
    @PostMapping("")
    public TableDataInfo searchMaterial(@RequestBody MaterialSearchDTO searchDTO)
    {
        CoreMaterial material = new CoreMaterial();
        material.setTitle(searchDTO.getKeyword());
        // 只查询上线的素材
        material.setStatus("0");

        PageHelper.startPage(searchDTO.getPageNum(), searchDTO.getPageSize());
        // 处理排序
        String orderBy = searchDTO.getOrderByColumn() + " " + (searchDTO.getIsAsc() ? "asc" : "desc");
        PageHelper.orderBy(orderBy);
        List<CoreMaterial> list = materialService.selectMaterialList(material);
        return getDataTable(list);
    }
}
