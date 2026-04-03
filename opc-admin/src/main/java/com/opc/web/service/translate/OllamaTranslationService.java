package com.opc.web.service.translate;

import io.github.ollama4j.Ollama;
import io.github.ollama4j.models.generate.OllamaGenerateRequest;
import io.github.ollama4j.models.generate.OllamaGenerateStreamObserver;
import io.github.ollama4j.models.response.OllamaResult;
import io.github.ollama4j.utils.OptionsBuilder;
import io.github.ollama4j.utils.PromptBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Ollama 翻译服务实现（使用 Ollama4j 客户端）
 *
 * @author opc
 */
@Service
@Primary
public class OllamaTranslationService implements TranslationService {

    private static final Logger log = LoggerFactory.getLogger(OllamaTranslationService.class);

    @Autowired
    private OllamaProperties properties;

    private volatile Ollama ollamaClient;
    private volatile boolean serverAvailable = true;
    private volatile long lastCheckTime = 0;
    private static final long CHECK_INTERVAL_MS = 60000; // 1分钟检查一次

    /**
     * 获取 Ollama 客户端（单例模式）
     */
    private Ollama getOllamaClient() {
        if (ollamaClient == null) {
            synchronized (this) {
                if (ollamaClient == null) {
                    ollamaClient = new Ollama(properties.getUrl());
                    ollamaClient.setRequestTimeoutSeconds(properties.getRequestTimeoutSeconds());
                }
            }
        }
        return ollamaClient;
    }

    /**
     * 检查服务器是否可用
     */
    private boolean isServerAvailable() {
        long now = System.currentTimeMillis();
        if (now - lastCheckTime < CHECK_INTERVAL_MS) {
            return serverAvailable;
        }
        lastCheckTime = now;

        try {
            Ollama client = getOllamaClient();
            // 简单检查连接
            client.listModels();
            serverAvailable = true;
            return true;
        } catch (Exception e) {
            log.error("Ollama 服务器连接检查失败: {}, 错误: {}", properties.getUrl(), e.getMessage(), e);
            serverAvailable = false;
            return false;
        }
    }

    /**
     * 翻译文本
     *
     * @param text       待翻译文本
     * @param sourceLang 源语言代码
     * @param targetLang 目标语言代码
     * @return 翻译后的文本
     */
    @Override
    public String translate(String text, String sourceLang, String targetLang) {
        if (text == null || text.trim().isEmpty()) {
            return text;
        }

        // 检查服务器是否可用
        if (!isServerAvailable()) {
            log.warn("Ollama 服务器不可用，跳过翻译，返回原文");
            return text;
        }

        // 重试机制
        int maxRetries = 2;
        for (int attempt = 1; attempt <= maxRetries; attempt++) {
            try {
                return doTranslate(text, sourceLang, targetLang);
            } catch (Exception e) {
                log.error("Ollama 翻译失败（尝试 {}/{}），原文: {}", attempt, maxRetries, text, e);
                if (attempt < maxRetries) {
                    // 重置客户端，下次重试重新创建
                    ollamaClient = null;
                    try {
                        Thread.sleep(1000); // 等待1秒后重试
                    } catch (InterruptedException ignored) {
                    }
                }
            }
        }

        // 所有重试都失败，返回原文
        return text;
    }

    /**
     * 执行翻译
     */
    private String doTranslate(String text, String sourceLang, String targetLang) throws Exception {
        Ollama ollama = getOllamaClient();

        String prompt = buildPrompt(text, sourceLang, targetLang);

        OllamaGenerateRequest request = new OllamaGenerateRequest(properties.getModel(), prompt);

        OptionsBuilder optionsBuilder = new OptionsBuilder();
        optionsBuilder.setTemperature(properties.getTemperature());
        request.setOptions(optionsBuilder.build().getOptionsMap());

        OllamaResult result = ollama.generate(request, (OllamaGenerateStreamObserver) null);

        String translatedText = result.getResponse();

        if (translatedText != null) {
            translatedText = translatedText.trim();
            if ((translatedText.startsWith("\"") && translatedText.endsWith("\"")) ||
                (translatedText.startsWith("'") && translatedText.endsWith("'"))) {
                translatedText = translatedText.substring(1, translatedText.length() - 1);
            }
        }

        log.debug("Ollama 翻译成功: {} -> {}", text, translatedText);
        return translatedText != null ? translatedText : text;
    }

    /**
     * 翻译文本（自动检测源语言）
     *
     * @param text       待翻译文本
     * @param targetLang 目标语言代码
     * @return 翻译后的文本
     */
    @Override
    public String translate(String text, String targetLang) {
        return translate(text, "auto", targetLang);
    }

    /**
     * 批量翻译文本
     *
     * @param texts      待翻译文本数组
     * @param sourceLang 源语言代码
     * @param targetLang 目标语言代码
     * @return 翻译后的文本数组
     */
    @Override
    public String[] translate(String[] texts, String sourceLang, String targetLang) {
        if (texts == null || texts.length == 0) {
            return texts;
        }

        String[] results = new String[texts.length];
        for (int i = 0; i < texts.length; i++) {
            results[i] = translate(texts[i], sourceLang, targetLang);
        }
        return results;
    }

    /**
     * 自动识别语言并翻译成中文
     *
     * @param text 待翻译文本
     * @return 中文翻译结果
     */
    @Override
    public String translateToChinese(String text) {
        return translate(text, "auto", LanguageCode.CHINESE_SIMPLIFIED);
    }

    /**
     * 构建翻译提示词
     */
    private String buildPrompt(String text, String sourceLang, String targetLang) {
        String targetLangName = getLanguageName(targetLang);

        PromptBuilder promptBuilder;
        if (sourceLang == null || "auto".equalsIgnoreCase(sourceLang)) {
            // 自动识别源语言
            promptBuilder = new PromptBuilder()
                    .addLine("你是一个专业的翻译助手。")
                    .addLine("请自动识别以下文本的语言，并将其翻译成" + targetLangName + "。")
                    .addLine("只返回翻译结果，不要添加任何解释、引号或额外内容。")
                    .addSeparator()
                    .addLine("原文：")
                    .addLine(text)
                    .addSeparator()
                    .addLine("译文：");
        } else {
            String sourceLangName = getLanguageName(sourceLang);
            promptBuilder = new PromptBuilder()
                    .addLine("你是一个专业的翻译助手。")
                    .addLine("请将以下文本从" + sourceLangName + "翻译成" + targetLangName + "。")
                    .addLine("只返回翻译结果，不要添加任何解释、引号或额外内容。")
                    .addSeparator()
                    .addLine("原文：")
                    .addLine(text)
                    .addSeparator()
                    .addLine("译文：");
        }

        return promptBuilder.build();
    }

    /**
     * 获取语言名称
     */
    private String getLanguageName(String langCode) {
        if (langCode == null || "auto".equalsIgnoreCase(langCode)) {
            return "原文语言";
        }

        Map<String, String> langMap = new HashMap<>();
        langMap.put("ZH", "中文");
        langMap.put("ZH-HANT", "繁体中文");
        langMap.put("EN", "英语");
        langMap.put("JA", "日语");
        langMap.put("KO", "韩语");
        langMap.put("FR", "法语");
        langMap.put("DE", "德语");
        langMap.put("ES", "西班牙语");
        langMap.put("IT", "意大利语");
        langMap.put("PT", "葡萄牙语");
        langMap.put("RU", "俄语");
        langMap.put("NL", "荷兰语");
        langMap.put("PL", "波兰语");
        langMap.put("TR", "土耳其语");
        langMap.put("AR", "阿拉伯语");
        langMap.put("SV", "瑞典语");
        langMap.put("ID", "印尼语");
        langMap.put("HI", "印地语");
        langMap.put("VI", "越南语");
        langMap.put("TH", "泰语");

        return langMap.getOrDefault(langCode.toUpperCase(), langCode);
    }
}
