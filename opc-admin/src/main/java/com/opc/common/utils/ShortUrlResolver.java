package com.opc.common.utils;

import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;

public class ShortUrlResolver {

    /**
     * 代理配置
     */
    private static final boolean PROXY_ENABLED = true;
    private static final String PROXY_HOST = "localhost";
    private static final int PROXY_PORT = 7899;

    /**
     * 将短链接展开为最终的长链接
     *
     * @param shortUrl 短链接 URL 字符串
     * @return 最终重定向后的长链接 URL 字符串
     * @throws Exception 网络错误、无效 URL 或重定向次数过多
     */
    public static String expandShortUrl(String shortUrl) throws Exception {
        int maxRedirects = 10;        // 防止无限循环
        int redirectCount = 0;
        String currentUrl = shortUrl;

        while (redirectCount < maxRedirects) {
            HttpURLConnection connection = null;
            try {
                URL url = new URL(currentUrl);
                
                // 根据是否启用代理创建连接
                if (PROXY_ENABLED) {
                    Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(PROXY_HOST, PROXY_PORT));
                    connection = (HttpURLConnection) url.openConnection(proxy);
                } else {
                    connection = (HttpURLConnection) url.openConnection();
                }
                
                // 禁止自动重定向，手动获取 Location 头
                connection.setInstanceFollowRedirects(false);
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);

                int statusCode = connection.getResponseCode();
                String location = connection.getHeaderField("Location");

                // 判断是否为重定向响应
                if (statusCode >= 300 && statusCode < 400 && location != null) {
                    // 处理相对路径的 Location 头，转换为绝对 URL
                    URL newUrl = new URL(url, location);
                    currentUrl = newUrl.toString();
                    redirectCount++;
                    continue;   // 继续循环，处理下一跳
                } else {
                    // 不是重定向，当前 URL 即为最终链接
                    return currentUrl;
                }
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }
        throw new RuntimeException("重定向次数超过限制：" + maxRedirects);
    }

    public static void main(String[] args) throws Exception {
        String shortUrl = "https://t.co/AiGjkp3513";
        String longUrl = expandShortUrl(shortUrl);
        System.out.println("长链接: " + longUrl);
        // 输出示例: 长链接: https://x.com/thedankoe/status/2010042119121957316
    }
}