package com.opc.mobile.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.opc.common.core.controller.BaseController;
import com.opc.common.core.page.TableDataInfo;
import com.opc.core.domain.CoreSearchHotword;
import com.opc.core.service.ICoreSearchHotwordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 移动端搜索热词接口
 *
 * @author opc
 */
@Tag(name = "移动端搜索热词", description = "移动端搜索热词相关接口")
@RestController
@RequestMapping("/mobile/searchHotword")
public class MobileSearchHotwordController extends BaseController
{
    @Autowired
    private ICoreSearchHotwordService searchHotwordService;

    /**
     * 分页查询搜索热词列表
     *
     * @param keyword 热词名称（可选，支持模糊查询）
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @return 分页数据
     */
    @Operation(summary = "获取搜索热词列表", description = "分页查询启用的搜索热词列表，支持按名称模糊查询")
    @Parameter(name = "keyword", description = "热词名称，支持模糊查询", required = false)
    @Parameter(name = "pageNum", description = "页码，默认为1", required = false)
    @Parameter(name = "pageSize", description = "每页大小，默认为10", required = false)
    @GetMapping("/list")
    public TableDataInfo list(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize)
    {
        CoreSearchHotword searchHotword = new CoreSearchHotword();
        searchHotword.setKeyword(keyword);
        // 移动端只查询启用的热词
        searchHotword.setStatus("0");

        // 设置分页参数
        if (pageNum != null && pageNum > 0) {
            // 使用BaseController的startPage方法
            startPage();
        }

        List<CoreSearchHotword> list = searchHotwordService.selectSearchHotwordList(searchHotword);
        return getDataTable(list);
    }
}
