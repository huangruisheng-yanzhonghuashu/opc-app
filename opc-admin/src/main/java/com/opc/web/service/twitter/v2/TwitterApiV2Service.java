package com.opc.web.service.twitter.v2;

import com.opc.web.dto.twitter.v2.TwitterSearchRequestDTO;
import com.opc.web.dto.twitter.v2.TwitterSearchResponseDTO;

/**
 * Twitter API v2 服务接口
 * <p>
 * 提供 Twitter API v2 相关操作的接口定义
 * </p>
 *
 * @author opc
 * @since 3.9.1
 */
public interface TwitterApiV2Service {

    /**
     * 搜索最近推文
     * <p>
     * 调用 Twitter API v2 /tweets/search/recent 接口
     * </p>
     *
     * @param request 搜索请求参数
     * @return 搜索响应结果
     */
    TwitterSearchResponseDTO searchRecentTweets(TwitterSearchRequestDTO request);

    /**
     * 构建 Twitter 推文 URL
     *
     * @param tweetId 推文ID
     * @param authorUsername 作者用户名（可选）
     * @return 推文 URL
     */
    String buildTweetUrl(String tweetId, String authorUsername);
}
