package com.opc.web.service.core;

import com.opc.common.core.domain.AjaxResult;
import com.opc.web.enums.SourceType;
import com.opc.web.service.reddit.RedditFetchService;
import com.opc.web.service.twitter.TwitterFetchService;
import com.opc.web.service.youtube.YoutubeFetchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
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

    /**
     * 异步获取数据（在后台执行，不阻塞前端）
     *
     * @param sourceType 来源类型
     * @param sourceUrl  信息源URL
     * @param keyword    关键词
     */
    @Async
    public void fetchDataAsync(String sourceType, String sourceUrl, String keyword) {
        try {
            log.info("开始异步获取数据, sourceType: {}, keyword: {}", sourceType, keyword);

            SourceType type = SourceType.fromValue(sourceType);
            if (type == null) {
                log.error("不支持的来源类型: {}", sourceType);
                return;
            }

            switch (type) {
                case TWITTER:
                    handleTwitterFetchAsync(sourceUrl, keyword);
                    break;
                case REDDIT:
                    redditFetchService.fetchRedditData(keyword);
                    break;
                case YOUTUBE:
                    handleYoutubeFetchAsync(sourceUrl, keyword);
                    break;
                default:
                    log.error("暂不支持的来源类型: {}", sourceType);
            }

            log.info("异步获取数据完成, sourceType: {}, keyword: {}", sourceType, keyword);
        } catch (Exception e) {
            log.error("异步获取数据失败, sourceType: {}, keyword: {}", sourceType, keyword, e);
        }
    }

    /**
     * 异步处理 Twitter 数据获取
     */
    private void handleTwitterFetchAsync(String sourceUrl, String keyword) {
        String TWITTER_API_V2_SEARCH_RECENT = "https://api.x.com/2/tweets/search/recent";

        if (TWITTER_API_V2_SEARCH_RECENT.equals(sourceUrl)) {
            twitterFetchService.fetchTwitterDataByApiV2(keyword);
        } else if (isTwitterUserUrl(sourceUrl)) {
            String userName = extractUserNameFromUrl(sourceUrl);
            if (userName != null && !userName.isEmpty()) {
                twitterFetchService.fetchTwitterDataByUserName(userName);
            } else {
                log.error("无法从 URL 提取用户名: {}", sourceUrl);
            }
        } else {
            twitterFetchService.fetchTwitterData(keyword);
        }
    }

    /**
     * 异步处理 YouTube 数据获取
     */
    private void handleYoutubeFetchAsync(String sourceUrl, String keyword) {
        youtubeFetchService.fetchYoutubeData(keyword);
    }

    /**
     * 判断是否是 Twitter 用户主页 URL
     */
    private boolean isTwitterUserUrl(String sourceUrl) {
        if (sourceUrl == null || sourceUrl.isEmpty()) {
            return false;
        }
        String twitterUserPattern = "^https://x\\.com/[a-zA-Z0-9_]{1,15}$";
        return sourceUrl.matches(twitterUserPattern);
    }

    /**
     * 从 Twitter 用户主页 URL 中提取用户名
     */
    private String extractUserNameFromUrl(String sourceUrl) {
        if (sourceUrl == null || sourceUrl.isEmpty()) {
            return null;
        }
        String twitterUserPattern = "^https://x\\.com/([a-zA-Z0-9_]{1,15})$";
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(twitterUserPattern);
        java.util.regex.Matcher matcher = pattern.matcher(sourceUrl);
        if (matcher.matches()) {
            return matcher.group(1);
        }
        return null;
    }
}
