package com.opc.common.utils.translate;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * DeepLX 翻译配置属性
 *
 * @author opc
 */
@Component
@ConfigurationProperties(prefix = "deeplx")
public class DeepLxProperties {

    /**
     * DeepLX 服务地址，默认为本地服务
     */
    private String url = "http://localhost:1188";

    /**
     * 访问令牌（可选，如果 DeepLX 服务配置了访问控制）
     */
    private String token;

    /**
     * 连接超时时间（毫秒）
     */
    private int connectTimeout = 5000;

    /**
     * 读取超时时间（毫秒）
     */
    private int readTimeout = 10000;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }
}
