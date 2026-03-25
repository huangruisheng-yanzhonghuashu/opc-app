package com.opc.ai.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Minimax AI 配置类
 * Minimax API 与 OpenAI API 兼容，因此使用 OpenAI 客户端
 */
@Configuration
public class MinimaxConfig {

    @Value("${spring.ai.minimax.api-key:}")
    private String apiKey;

    @Value("${spring.ai.minimax.base-url:https://api.minimax.chat}")
    private String baseUrl;

    @Value("${spring.ai.minimax.model:MiniMax-Text-01}")
    private String model;

    @Value("${spring.ai.minimax.temperature:0.7}")
    private Double temperature;

    /**
     * 创建 Minimax ChatClient 实例
     * 仅在配置了 API key 时才创建
     */
    @Bean
    @ConditionalOnProperty(name = "spring.ai.minimax.api-key")
    public ChatClient minimaxChatClient() {
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
