package com.opc.common.utils.translate;

/**
 * 翻译服务接口
 *
 * @author opc
 */
public interface TranslationService {

    /**
     * 翻译文本
     *
     * @param text       待翻译文本
     * @param sourceLang 源语言代码（如：EN、ZH、JA等）
     * @param targetLang 目标语言代码（如：EN、ZH、JA等）
     * @return 翻译后的文本
     */
    String translate(String text, String sourceLang, String targetLang);

    /**
     * 翻译文本（自动检测源语言）
     *
     * @param text       待翻译文本
     * @param targetLang 目标语言代码
     * @return 翻译后的文本
     */
    String translate(String text, String targetLang);

    /**
     * 批量翻译文本
     *
     * @param texts      待翻译文本数组
     * @param sourceLang 源语言代码
     * @param targetLang 目标语言代码
     * @return 翻译后的文本数组
     */
    String[] translate(String[] texts, String sourceLang, String targetLang);
}
