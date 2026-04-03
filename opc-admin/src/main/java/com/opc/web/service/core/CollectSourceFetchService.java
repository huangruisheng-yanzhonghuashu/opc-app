package com.opc.web.service.core;

import com.opc.common.core.domain.AjaxResult;
import com.opc.web.service.reddit.RedditFetchService;
import com.opc.web.service.twitter.TwitterFetchService;
import com.opc.web.service.youtube.YoutubeFetchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 采集信息源数据获取服务
 * <p>
 * 委托 Twitter 和 Reddit 的数据获取和导入逻辑到各自的专用服务
 * </p>
 *
 * @author opc
 * @since 3.9.1
 */
@Service
public class CollectSourceFetchService {

    private static final Logger log = LoggerFactory.getLogger(CollectSourceFetchService.class);

    @Autowired
    private TwitterFetchService twitterFetchService;

    @Autowired
    private RedditFetchService redditFetchService;

    @Autowired
    private YoutubeFetchService youtubeFetchService;

    /**
     * 使用 Twitter API v2 获取数据
     *
     * @param keyword 搜索关键词
     * @return 导入结果
     */
    public AjaxResult fetchTwitterDataByApiV2(String keyword) {
        return twitterFetchService.fetchTwitterDataByApiV2(keyword);
    }

    /**
     * 获取 Twitter 数据并导入（使用 opencli）
     *
     * @param keyword 搜索关键词
     * @return 导入结果
     */
    public AjaxResult fetchTwitterData(String keyword) {
        return twitterFetchService.fetchTwitterData(keyword);
    }

    /**
     * 根据用户名获取 Twitter 数据并导入（使用 opencli）
     * <p>
     * 执行命令: opencli twitter search "from:userName" --limit 10 -f json
     * </p>
     *
     * @param userName Twitter 用户名
     * @return 导入结果
     */
    public AjaxResult fetchTwitterDataByUserName(String userName) {
        return twitterFetchService.fetchTwitterDataByUserName(userName);
    }

    /**
     * 获取 Reddit 数据并导入
     *
     * @param keyword 搜索关键词
     * @return 导入结果
     */
    public AjaxResult fetchRedditData(String keyword) {
        return redditFetchService.fetchRedditData(keyword);
    }

    /**
     * 搜索 YouTube 视频并导入
     *
     * @param keyword 搜索关键词
     * @return 导入结果
     */
    public AjaxResult fetchYoutubeData(String keyword) {
        return youtubeFetchService.fetchYoutubeData(keyword);
    }

    /**
     * 根据视频 URL 导入单个 YouTube 视频
     * @param keyword
     * @return
     */
    public AjaxResult fetchYoutubeVideoByUrl(String keyword) {
        return youtubeFetchService.fetchYoutubeData(keyword);
    }
}
