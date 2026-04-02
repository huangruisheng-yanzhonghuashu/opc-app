package com.opc.common.utils;

/**
 * HTML内容包装工具类
 * 用于将内容包装在完整的HTML文档结构中，适配移动端显示
 *
 * @author opc
 */
public class HtmlWrapperUtils {

    /**
     * HTML模板
     */
    private static final String HTML_TEMPLATE = """
            <!DOCTYPE html>
            <html>
            <head>
            <meta name="viewport" content="width=device-width, initial-scale=1">
            <style>
            html { font-size: 16px; }
            body { margin: 0; padding: 10px; }
            img { max-width: 100%%; height: auto; }
            </style>
            </head>
            <body>
            %s
            </body>
            </html>
            """;

    /**
     * 将内容包装在完整的HTML文档中
     *
     * @param content 原始内容
     * @return 包装后的完整HTML
     */
    public static String wrapContent(String content) {
        if (content == null) {
            content = "";
        }
        return String.format(HTML_TEMPLATE, content);
    }
}
