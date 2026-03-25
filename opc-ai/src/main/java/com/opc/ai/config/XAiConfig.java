package com.opc.ai.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * xAI (Grok) 配置类
 * xAI API 与 OpenAI API 兼容，因此使用 OpenAI 客户端
 */
@Configuration
public class XAiConfig {

    @Value("${spring.ai.xai.api-key}")
    private String apiKey;

    @Value("${spring.ai.xai.base-url:https://api.x.ai/v1}")
    private String baseUrl;

    @Value("${spring.ai.xai.model:grok-2-latest}")
    private String model;

    @Value("${spring.ai.xai.temperature:0.7}")
    private Double temperature;

    /**
     * 创建 xAI ChatClient 实例
     */
    @Bean
    public ChatClient xAiChatClient() {
        OpenAiApi openAiApi = OpenAiApi.builder()
                .baseUrl(baseUrl)
                .apiKey(apiKey)
                .build();
        OpenAiChatOptions options = OpenAiChatOptions.builder()
                .model(model)
                .temperature(temperature)
                .build();
        OpenAiChatModel chatModel = new OpenAiChatModel(openAiApi, options);
        return ChatClient.builder(chatModel).build();
    }
}
