package com.opc.ai;

import com.opc.ai.service.AiChatService;
import com.opc.common.core.domain.AjaxResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

/**
 * AI 对话控制器
 */
@Tag(name = "AI对话", description = "AI智能对话相关接口")
@RestController
@RequestMapping("/ai/chat")
public class AiChatController {

    @Autowired
    private AiChatService aiChatService;

    /**
     * 普通对话
     */
    @Operation(summary = "普通对话", description = "发送消息与AI进行普通对话")
    @PostMapping("/send")
    public AjaxResult chat(@RequestBody ChatRequest request) {
        String response = aiChatService.chat(request.getMessage());
        return AjaxResult.success(response);
    }

    /**
     * 带系统提示的对话
     */
    @Operation(summary = "带系统提示的对话", description = "携带系统提示词与AI进行对话")
    @PostMapping("/send-with-prompt")
    public AjaxResult chatWithSystemPrompt(@RequestBody ChatWithPromptRequest request) {
        String response = aiChatService.chatWithSystemPrompt(
                request.getSystemPrompt(),
                request.getUserMessage()
        );
        return AjaxResult.success(response);
    }

    /**
     * 流式对话 (SSE)
     */
    @Operation(summary = "流式对话", description = "使用SSE流式方式与AI对话")
    @PostMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamChat(@RequestBody ChatRequest request) {
        return aiChatService.streamChat(request.getMessage());
    }

    /**
     * 带系统提示的流式对话 (SSE)
     */
    @Operation(summary = "带系统提示的流式对话", description = "携带系统提示词使用SSE流式方式与AI对话")
    @PostMapping(value = "/stream-with-prompt", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamChatWithSystemPrompt(@RequestBody ChatWithPromptRequest request) {
        return aiChatService.streamChatWithSystemPrompt(
                request.getSystemPrompt(),
                request.getUserMessage()
        );
    }

    // ========== 请求DTO ==========

    @Schema(description = "普通对话请求")
    public static class ChatRequest {
        @Schema(description = "用户消息内容", example = "你好，请介绍一下自己")
        private String message;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    @Schema(description = "带系统提示的对话请求")
    public static class ChatWithPromptRequest {
        @Schema(description = "系统提示词，用于设定AI角色和行为", example = "你是一位专业的Java开发工程师")
        private String systemPrompt;

        @Schema(description = "用户消息内容", example = "请解释Spring Boot的核心特性")
        private String userMessage;

        public String getSystemPrompt() {
            return systemPrompt;
        }

        public void setSystemPrompt(String systemPrompt) {
            this.systemPrompt = systemPrompt;
        }

        public String getUserMessage() {
            return userMessage;
        }

        public void setUserMessage(String userMessage) {
            this.userMessage = userMessage;
        }
    }
}
