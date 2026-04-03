package com.opc.web.service.translate;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Ollama 翻译配置属性
 *
 * @author opc
 */
@Component
@ConfigurationProperties(prefix = "ollama.translation")
public class OllamaProperties {

    /**
     * Ollama 服务地址，默认为本地
     */
    private String url = "http://localhost:11434";

    /**
     * 使用的模型名称
     */
    private String model = "translategemma:4b";

    /**
     * 请求超时时间（秒）
     */
    private int requestTimeoutSeconds = 60;

    /**
     * 温度参数（创造性程度，0-1）
     */
    private float temperature = 0.3f;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getRequestTimeoutSeconds() {
        return requestTimeoutSeconds;
    }

    public void setRequestTimeoutSeconds(int requestTimeoutSeconds) {
        this.requestTimeoutSeconds = requestTimeoutSeconds;
    }

    public float getTemperature() {
        return temperature;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }
}
