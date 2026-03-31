package com.opc.web.config.twitter.v2;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Twitter API v2 配置属性类
 * <p>
 * 用于读取 application-twitter.yml 中的配置项
 * </p>
 *
 * @author opc
 * @since 3.9.1
 */
@ConfigurationProperties(prefix = "twitter.api")
public class TwitterApiV2Properties {

    /**
     * Bearer Token 用于 API 认证
     */
    private String bearerToken;

    /**
     * API 基础 URL
     */
    private String baseUrl = "https://api.x.com/2";

    /**
     * 请求超时时间（毫秒）
     */
    private int timeout = 30000;

    /**
     * 是否启用代理
     */
    private boolean proxyEnabled = false;

    /**
     * 代理主机
     */
    private String proxyHost = "localhost";

    /**
     * 代理端口
     */
    private int proxyPort = 11301;

    public String getBearerToken() {
        return bearerToken;
    }

    public void setBearerToken(String bearerToken) {
        this.bearerToken = bearerToken;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public boolean isProxyEnabled() {
        return proxyEnabled;
    }

    public void setProxyEnabled(boolean proxyEnabled) {
        this.proxyEnabled = proxyEnabled;
    }

    public String getProxyHost() {
        return proxyHost;
    }

    public void setProxyHost(String proxyHost) {
        this.proxyHost = proxyHost;
    }

    public int getProxyPort() {
        return proxyPort;
    }

    public void setProxyPort(int proxyPort) {
        this.proxyPort = proxyPort;
    }
}
