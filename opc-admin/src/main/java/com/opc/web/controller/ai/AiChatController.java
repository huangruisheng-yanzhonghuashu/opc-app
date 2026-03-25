package com.opc.web.controller.ai;

import com.opc.ai.service.AiChatService;
import com.opc.common.core.domain.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

/**
 * AI 对话控制器
 */
@RestController
@RequestMapping("/ai/chat")
public class AiChatController {

    @Autowired
    private AiChatService aiChatService;

    /**
     * 普通对话
     */
    @PostMapping("/send")
    public AjaxResult chat(@RequestBody ChatRequest request) {
        String response = aiChatService.chat(request.getMessage());
        return AjaxResult.success(response);
    }

    /**
     * 带系统提示的对话
     */
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
    @PostMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamChat(@RequestBody ChatRequest request) {
        return aiChatService.streamChat(request.getMessage());
    }

    /**
     * 带系统提示的流式对话 (SSE)
     */
    @PostMapping(value = "/stream-with-prompt", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamChatWithSystemPrompt(@RequestBody ChatWithPromptRequest request) {
        return aiChatService.streamChatWithSystemPrompt(
                request.getSystemPrompt(),
                request.getUserMessage()
        );
    }

    // ========== 请求DTO ==========

    public static class ChatRequest {
        private String message;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    public static class ChatWithPromptRequest {
        private String systemPrompt;
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
