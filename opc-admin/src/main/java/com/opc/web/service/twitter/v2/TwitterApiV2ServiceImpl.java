package com.opc.web.service.twitter.v2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.opc.web.config.twitter.v2.TwitterApiV2Properties;
import com.opc.web.dto.twitter.v2.TwitterSearchRequestDTO;
import com.opc.web.dto.twitter.v2.TwitterSearchResponseDTO;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * Twitter API v2 服务实现类
 * <p>
 * 使用 Unirest 实现 Twitter API v2 相关操作，包括搜索推文等功能
 * </p>
 *
 * @author opc
 * @since 3.9.1
 */
@Service
public class TwitterApiV2ServiceImpl implements TwitterApiV2Service {

    private static final Logger log = LoggerFactory.getLogger(TwitterApiV2ServiceImpl.class);

    private static final String SEARCH_RECENT_ENDPOINT = "/tweets/search/recent";

    @Autowired
    private TwitterApiV2Properties twitterApiV2Properties;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostConstruct
    public void init() {
        // 配置 Unirest
        Unirest.config()
                .connectTimeout(twitterApiV2Properties.getTimeout())
                .socketTimeout(twitterApiV2Properties.getTimeout())
                .concurrency(100, 20);
        log.info("Unirest 初始化完成");
    }

    @PreDestroy
    public void shutdown() {
        Unirest.shutDown();
        log.info("Unirest 已关闭");
    }

    @Override
    public TwitterSearchResponseDTO searchRecentTweets(TwitterSearchRequestDTO request) {
        try {
            // 构建请求 URL
            String url = buildSearchUrl(request);
            log.info("调用 Twitter API v2 搜索接口: {}", url);

            // 发送请求
            HttpResponse<String> response = Unirest.get(url)
                    .header("Authorization", "Bearer " + twitterApiV2Properties.getBearerToken())
                    .asString();

            if (response.getStatus() == 200) {
                TwitterSearchResponseDTO result = objectMapper.readValue(
                        response.getBody(),
                        TwitterSearchResponseDTO.class
                );
                log.info("Twitter API 搜索成功，获取 {} 条推文",
                        result.getMeta() != null ? result.getMeta().getResultCount() : 0);
                return result;
            } else {
                log.error("Twitter API 返回非成功状态码: {}，响应: {}", response.getStatus(), response.getBody());
                throw new RuntimeException("Twitter API 返回非成功状态码: " + response.getStatus());
            }

        } catch (IOException e) {
            log.error("解析 Twitter API 响应失败", e);
            throw new RuntimeException("解析 Twitter API 响应失败: " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("调用 Twitter API v2 搜索接口失败", e);
            throw new RuntimeException("调用 Twitter API v2 搜索接口失败: " + e.getMessage(), e);
        }
    }

    @Override
    public String buildTweetUrl(String tweetId, String authorUsername) {
        if (authorUsername != null && !authorUsername.isEmpty()) {
            return String.format("https://x.com/%s/status/%s", authorUsername, tweetId);
        }
        return String.format("https://x.com/i/status/%s", tweetId);
    }

    /**
     * 构建搜索请求 URL
     *
     * @param request 搜索请求参数
     * @return 构建好的 URL
     */
    private String buildSearchUrl(TwitterSearchRequestDTO request) {
        StringBuilder urlBuilder = new StringBuilder();
        urlBuilder.append(twitterApiV2Properties.getBaseUrl())
                .append(SEARCH_RECENT_ENDPOINT);

        // 添加查询参数
        urlBuilder.append("?query=").append(encodeValue(request.getQuery()));

        // 添加可选参数
        if (request.getMaxResults() != null) {
            urlBuilder.append("&max_results=").append(request.getMaxResults());
        }

        if (request.getNextToken() != null && !request.getNextToken().isEmpty()) {
            urlBuilder.append("&next_token=").append(encodeValue(request.getNextToken()));
        }

        if (request.getTweetFields() != null && !request.getTweetFields().isEmpty()) {
            urlBuilder.append("&tweet.fields=").append(encodeValue(request.getTweetFields()));
        } else {
            // 默认字段
            urlBuilder.append("&tweet.fields=").append(encodeValue("created_at,author_id,public_metrics,source,lang"));
        }

        if (request.getExpansions() != null && !request.getExpansions().isEmpty()) {
            urlBuilder.append("&expansions=").append(encodeValue(request.getExpansions()));
        }

        if (request.getStartTime() != null && !request.getStartTime().isEmpty()) {
            urlBuilder.append("&start_time=").append(encodeValue(request.getStartTime()));
        }

        if (request.getEndTime() != null && !request.getEndTime().isEmpty()) {
            urlBuilder.append("&end_time=").append(encodeValue(request.getEndTime()));
        }

        return urlBuilder.toString();
    }

    /**
     * URL 编码
     */
    private String encodeValue(String value) {
        try {
            return URLEncoder.encode(value, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("URL 编码失败", e);
        }
    }
}
