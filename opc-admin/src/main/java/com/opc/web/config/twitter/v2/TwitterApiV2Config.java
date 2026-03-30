package com.opc.web.config.twitter.v2;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Twitter API v2 配置类
 * <p>
 * 启用配置属性，使用 Unirest 进行 HTTP 请求
 * </p>
 *
 * @author opc
 * @since 3.9.1
 */
@Configuration
@EnableConfigurationProperties(TwitterApiV2Properties.class)
public class TwitterApiV2Config {
    // Unirest 配置已移至 TwitterApiV2ServiceImpl
}
