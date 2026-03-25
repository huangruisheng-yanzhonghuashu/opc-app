package com.opc.ai.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

/**
 * AI 对话服务
 */
@Service
public class AiChatService {

    @Autowired(required = false)
    @Qualifier("xAiChatClient")
    //@Qualifier("minimaxChatClient")
    private ChatClient chatClient;



    private void checkChatClient() {
        if (chatClient == null) {
            throw new IllegalStateException("AI服务未配置，请在配置文件中设置 spring.ai.minimax.api-key");
        }
    }

    /**
     * 普通对话
     *
     * @param message 用户消息
     * @return AI 回复
     */
    public String chat(String message) {
        checkChatClient();
        return chatClient.prompt(message).call().content();
    }

    /**
     * 带系统提示的对话
     *
     * @param systemPrompt 系统提示词
     * @param userMessage  用户消息
     * @return AI 回复
     */
    public String chatWithSystemPrompt(String systemPrompt, String userMessage) {
        checkChatClient();
        return chatClient.prompt()
                .system(systemPrompt)
                .user(userMessage)
                .call()
                .content();
    }

    /**
     * 流式对话 (SSE)
     *
     * @param message 用户消息
     * @return 流式响应
     */
    public Flux<String> streamChat(String message) {
        checkChatClient();
        return chatClient.prompt(message).stream().content();
    }

    /**
     * 带系统提示的流式对话
     *
     * @param systemPrompt 系统提示词
     * @param userMessage  用户消息
     * @return 流式响应
     */
    public Flux<String> streamChatWithSystemPrompt(String systemPrompt, String userMessage) {
        checkChatClient();
        return chatClient.prompt()
                .system(systemPrompt)
                .user(userMessage)
                .stream()
                .content();
    }
}
