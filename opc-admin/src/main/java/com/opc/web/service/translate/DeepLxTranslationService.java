package com.opc.web.service.translate;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * DeepLX 翻译服务实现（已禁用，使用 OllamaTranslationService 替代）
 *
 * @author opc
 */
// @Service - 已禁用，只使用 OllamaTranslationService
public class DeepLxTranslationService implements TranslationService {

    private static final Logger log = LoggerFactory.getLogger(DeepLxTranslationService.class);

    @Autowired
    private DeepLxProperties properties;

    private final RestTemplate restTemplate = new RestTemplate();

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

        try {
            String url = properties.getUrl() + "/translate";

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("text", text);
            requestBody.put("source_lang", normalizeLangCode(sourceLang));
            requestBody.put("target_lang", normalizeLangCode(targetLang));

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            // 如果配置了访问令牌，添加到请求头
            if (properties.getToken() != null && !properties.getToken().isEmpty()) {
                headers.set("Authorization", "Bearer " + properties.getToken());
            }

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    entity,
                    String.class
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                JSONObject jsonResponse = JSON.parseObject(response.getBody());
                
                // 处理响应结果
                if (jsonResponse.containsKey("data")) {
                    return jsonResponse.getString("data");
                } else if (jsonResponse.containsKey("alternatives")) {
                    JSONArray alternatives = jsonResponse.getJSONArray("alternatives");
                    if (alternatives != null && !alternatives.isEmpty()) {
                        return alternatives.getString(0);
                    }
                }
                
                // 直接返回data字段
                return jsonResponse.getString("data");
            }

            log.error("DeepLX 翻译请求失败，状态码：{}，响应：{}", 
                    response.getStatusCode(), response.getBody());
            return text;

        } catch (Exception e) {
            log.error("DeepLX 翻译异常：", e);
            return text;
        }
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
     * 标准化语言代码
     *
     * @param langCode 语言代码
     * @return 标准化后的语言代码
     */
    private String normalizeLangCode(String langCode) {
        if (langCode == null || langCode.isEmpty()) {
            return "auto";
        }

        // 转换为 DeepLX 支持的语言代码格式
        String upper = langCode.toUpperCase();
        switch (upper) {
            case "ZH-CN":
            case "ZH_CN":
                return "ZH";
            case "ZH-TW":
            case "ZH_TW":
                return "ZH-HANT";
            case "EN-US":
            case "EN-GB":
            case "EN_US":
            case "EN_GB":
                return "EN";
            default:
                return upper;
        }
    }
}
