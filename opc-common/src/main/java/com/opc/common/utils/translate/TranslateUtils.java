package com.opc.common.utils.translate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

/**
 * 翻译工具类
 *
 * @author opc
 */
@Component
public class TranslateUtils {

    private static TranslationService staticTranslationService;

    @Autowired
    private TranslationService translationService;

    @PostConstruct
    public void init() {
        staticTranslationService = translationService;
    }

    /**
     * 翻译文本
     *
     * @param text       待翻译文本
     * @param sourceLang 源语言代码
     * @param targetLang 目标语言代码
     * @return 翻译后的文本
     */
    public static String translate(String text, String sourceLang, String targetLang) {
        if (staticTranslationService == null) {
            return text;
        }
        return staticTranslationService.translate(text, sourceLang, targetLang);
    }

    /**
     * 翻译文本（自动检测源语言）
     *
     * @param text       待翻译文本
     * @param targetLang 目标语言代码
     * @return 翻译后的文本
     */
    public static String translate(String text, String targetLang) {
        if (staticTranslationService == null) {
            return text;
        }
        return staticTranslationService.translate(text, targetLang);
    }

    /**
     * 翻译为中文
     *
     * @param text 待翻译文本
     * @return 中文翻译结果
     */
    public static String toChinese(String text) {
        return translate(text, LanguageCode.CHINESE_SIMPLIFIED);
    }

    /**
     * 翻译为英文
     *
     * @param text 待翻译文本
     * @return 英文翻译结果
     */
    public static String toEnglish(String text) {
        return translate(text, LanguageCode.ENGLISH);
    }

    /**
     * 翻译为日语
     *
     * @param text 待翻译文本
     * @return 日语翻译结果
     */
    public static String toJapanese(String text) {
        return translate(text, LanguageCode.JAPANESE);
    }
}
