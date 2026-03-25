package com.opc.web.controller.ai;

import com.opc.ai.service.AiChatService;
import com.opc.common.core.controller.BaseController;
import com.opc.common.core.domain.AjaxResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

/**
 * AI 对话控制器
 *
 * @author opc
 */
@Tag(name = "AI对话", description = "AI聊天对话相关接口")
@RestController
@RequestMapping("/ai")
public class AiController extends BaseController {

    @Autowired
    private AiChatService aiChatService;

    /**
     * 普通对话
     *
     * @param message 用户消息
     * @return AI 回复
     */
    @Operation(summary = "普通对话", description = "发送消息获取AI回复")
    @PostMapping("/chat")
    public AjaxResult chat(
            @Parameter(description = "用户消息", required = true) @RequestParam String message) {
        String response = aiChatService.chat(message);
        return success(response);
    }

    /**
     * 带系统提示的对话
     *
     * @param systemPrompt 系统提示词
     * @param userMessage  用户消息
     * @return AI 回复
     */
    @Operation(summary = "带系统提示的对话", description = "使用自定义系统提示词进行对话")
    @PostMapping("/chatWithSystem")
    public AjaxResult chatWithSystem(
            @Parameter(description = "系统提示词", required = true) @RequestParam String systemPrompt,
            @Parameter(description = "用户消息", required = true) @RequestParam String userMessage) {
        String response = aiChatService.chatWithSystemPrompt(systemPrompt, userMessage);
        return success(response);
    }

    /**
     * 流式对话 (SSE)
     *
     * @param message 用户消息
     * @return 流式响应
     */
    @Operation(summary = "流式对话", description = "使用SSE流式返回AI回复")
    @GetMapping(value = "/streamChat", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamChat(
            @Parameter(description = "用户消息", required = true) @RequestParam String message) {
        return aiChatService.streamChat(message);
    }

    /**
     * 带系统提示的流式对话
     *
     * @param systemPrompt 系统提示词
     * @param userMessage  用户消息
     * @return 流式响应
     */
    @Operation(summary = "带系统提示的流式对话", description = "使用自定义系统提示词进行流式对话")
    @GetMapping(value = "/streamChatWithSystem", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamChatWithSystem(
            @Parameter(description = "系统提示词", required = true) @RequestParam String systemPrompt,
            @Parameter(description = "用户消息", required = true) @RequestParam String userMessage) {
        return aiChatService.streamChatWithSystemPrompt(systemPrompt, userMessage);
    }
}
