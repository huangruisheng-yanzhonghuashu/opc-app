package com.opc.web.controller.common;

import com.opc.common.core.controller.BaseController;
import com.opc.common.core.domain.AjaxResult;
import com.opc.common.utils.translate.LanguageCode;
import com.opc.common.utils.translate.TranslationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 翻译接口 Controller
 *
 * @author opc
 */
@RestController
@RequestMapping("/translate")
@Tag(name = "翻译服务", description = "基于 DeepLX 的翻译接口")
public class TranslateController extends BaseController {

    @Autowired
    private TranslationService translationService;

    /**
     * 翻译文本
     *
     * @param text       待翻译文本
     * @param sourceLang 源语言代码（默认 auto 自动检测）
     * @param targetLang 目标语言代码（默认 ZH 中文）
     * @return 翻译结果
     */
    @PostMapping("/text")
    @Operation(summary = "翻译文本", description = "将文本从源语言翻译为目标语言")
    public AjaxResult translate(
            @Parameter(description = "待翻译文本", required = true) @RequestParam String text,
            @Parameter(description = "源语言代码，默认 auto") @RequestParam(defaultValue = "auto") String sourceLang,
            @Parameter(description = "目标语言代码，默认 ZH") @RequestParam(defaultValue = "ZH") String targetLang) {

        if (text == null || text.trim().isEmpty()) {
            return AjaxResult.error("待翻译文本不能为空");
        }

        String result = translationService.translate(text, sourceLang, targetLang);
        
        Map<String, Object> data = new HashMap<>();
        data.put("sourceText", text);
        data.put("translatedText", result);
        data.put("sourceLang", sourceLang);
        data.put("targetLang", targetLang);
        
        return AjaxResult.success(data);
    }

    /**
     * 翻译为中文
     *
     * @param text 待翻译文本
     * @return 翻译结果
     */
    @PostMapping("/toChinese")
    @Operation(summary = "翻译为中文", description = "自动检测语言并翻译为简体中文")
    public AjaxResult toChinese(
            @Parameter(description = "待翻译文本", required = true) @RequestParam String text) {
        return translate(text, "auto", LanguageCode.CHINESE_SIMPLIFIED);
    }

    /**
     * 翻译为英文
     *
     * @param text 待翻译文本
     * @return 翻译结果
     */
    @PostMapping("/toEnglish")
    @Operation(summary = "翻译为英文", description = "自动检测语言并翻译为英文")
    public AjaxResult toEnglish(
            @Parameter(description = "待翻译文本", required = true) @RequestParam String text) {
        return translate(text, "auto", LanguageCode.ENGLISH);
    }

    /**
     * 批量翻译
     *
     * @param texts      待翻译文本数组
     * @param sourceLang 源语言代码
     * @param targetLang 目标语言代码
     * @return 翻译结果数组
     */
    @PostMapping("/batch")
    @Operation(summary = "批量翻译", description = "批量翻译多个文本")
    public AjaxResult translateBatch(
            @Parameter(description = "待翻译文本数组", required = true) @RequestParam String[] texts,
            @Parameter(description = "源语言代码，默认 auto") @RequestParam(defaultValue = "auto") String sourceLang,
            @Parameter(description = "目标语言代码，默认 ZH") @RequestParam(defaultValue = "ZH") String targetLang) {

        if (texts == null || texts.length == 0) {
            return AjaxResult.error("待翻译文本数组不能为空");
        }

        String[] results = translationService.translate(texts, sourceLang, targetLang);
        
        Map<String, Object> data = new HashMap<>();
        data.put("sourceTexts", texts);
        data.put("translatedTexts", results);
        data.put("sourceLang", sourceLang);
        data.put("targetLang", targetLang);
        
        return AjaxResult.success(data);
    }

    /**
     * 获取支持的语言列表
     *
     * @return 语言列表
     */
    @GetMapping("/languages")
    @Operation(summary = "获取支持的语言", description = "获取所有支持的语言代码列表")
    public AjaxResult getLanguages() {
        Map<String, String> languages = new HashMap<>();
        languages.put("auto", "自动检测");
        languages.put(LanguageCode.CHINESE_SIMPLIFIED, "简体中文");
        languages.put(LanguageCode.CHINESE_TRADITIONAL, "繁体中文");
        languages.put(LanguageCode.ENGLISH, "英语");
        languages.put(LanguageCode.JAPANESE, "日语");
        languages.put(LanguageCode.KOREAN, "韩语");
        languages.put(LanguageCode.FRENCH, "法语");
        languages.put(LanguageCode.GERMAN, "德语");
        languages.put(LanguageCode.SPANISH, "西班牙语");
        languages.put(LanguageCode.ITALIAN, "意大利语");
        languages.put(LanguageCode.PORTUGUESE, "葡萄牙语");
        languages.put(LanguageCode.RUSSIAN, "俄语");
        languages.put(LanguageCode.DUTCH, "荷兰语");
        languages.put(LanguageCode.POLISH, "波兰语");
        languages.put(LanguageCode.TURKISH, "土耳其语");
        languages.put(LanguageCode.ARABIC, "阿拉伯语");
        languages.put(LanguageCode.SWEDISH, "瑞典语");
        languages.put(LanguageCode.INDONESIAN, "印尼语");
        languages.put(LanguageCode.HINDI, "印地语");
        languages.put(LanguageCode.VIETNAMESE, "越南语");
        languages.put(LanguageCode.THAI, "泰语");
        
        return AjaxResult.success(languages);
    }
}
