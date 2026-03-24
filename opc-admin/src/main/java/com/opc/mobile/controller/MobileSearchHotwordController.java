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
import com.opc.core.domain.CoreSearchHotword;
import com.opc.mobile.dto.SearchHotwordQueryDTO;
import com.opc.core.service.ICoreSearchHotwordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 移动端搜索热词接口
 *
 * @author opc
 */
@Tag(name = "搜索热词", description = "移动端搜索热词相关接口")
@RestController
@RequestMapping("/mobile/searchHotword")
public class MobileSearchHotwordController extends BaseController
{
    @Autowired
    private ICoreSearchHotwordService searchHotwordService;

    /**
     * 分页查询搜索热词列表
     *
     * @param queryDTO 查询参数
     * @return 分页数据
     */
    @Operation(summary = "获取搜索热词列表", description = "分页查询搜索热词列表，支持按名称模糊查询")
    @PostMapping("/list")
    public TableDataInfo list(@RequestBody SearchHotwordQueryDTO queryDTO)
    {
        CoreSearchHotword searchHotword = new CoreSearchHotword();
        searchHotword.setKeyword(queryDTO.getKeyword());

        PageHelper.startPage(queryDTO.getPageNum(), queryDTO.getPageSize());
        List<CoreSearchHotword> list = searchHotwordService.selectSearchHotwordList(searchHotword);
        return getDataTable(list);
    }
}
