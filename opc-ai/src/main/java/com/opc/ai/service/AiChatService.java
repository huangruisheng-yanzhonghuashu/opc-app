package com.opc.ai.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;

/**
 * AI 对话服务
 */
@Service
public class AiChatService {

    @Autowired
    @Qualifier("xAiChatClient")
    private ChatClient chatClient;

    /**
     * 普通对话
     *
     * @param message 用户消息
     * @return AI 回复
     */
    public String chat(String message) {
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
        return chatClient.prompt()
                .system(systemPrompt)
                .user(userMessage)
                .stream()
                .content();
    }
}
