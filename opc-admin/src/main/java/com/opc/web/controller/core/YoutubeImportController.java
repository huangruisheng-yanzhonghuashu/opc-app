package com.opc.web.controller.core;

import com.opc.common.core.domain.AjaxResult;
import com.opc.web.service.youtube.YoutubeFetchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * YouTube 数据导入控制器
 * <p>
 * 提供 YouTube 视频搜索和导入的 API 接口
 * </p>
 *
 * @author opc
 * @since 3.9.1
 */
@RestController
@RequestMapping("/core/youtube")
public class YoutubeImportController {

    @Autowired
    private YoutubeFetchService youtubeFetchService;

    /**
     * 搜索 YouTube 视频并导入
     *
     * @param keyword 搜索关键词
     * @return 导入结果
     */
    @PreAuthorize("@ss.hasPermi('core:material:add')")
    @GetMapping("/import")
    public AjaxResult importYoutubeData(@RequestParam("keyword") String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return AjaxResult.error("搜索关键词不能为空");
        }
        return youtubeFetchService.fetchYoutubeData(keyword.trim());
    }
}
