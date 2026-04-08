package com.opc.common.utils;

import com.opc.web.config.opencli.OpenCliProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;

@Component
public class ShortUrlResolver {

    private static OpenCliProperties openCliProperties;

    @Autowired
    public void setOpenCliProperties(OpenCliProperties properties) {
        ShortUrlResolver.openCliProperties = properties;
    }

    public static String expandShortUrl(String shortUrl) throws Exception {
        int maxRedirects = 10;
        int redirectCount = 0;
        String currentUrl = shortUrl;

        while (redirectCount < maxRedirects) {
            HttpURLConnection connection = null;
            try {
                URL url = new URL(currentUrl);
                
                // 使用 opencli.proxy 配置
                Proxy proxy = getHttpProxy();
                if (proxy != null) {
                    connection = (HttpURLConnection) url.openConnection(proxy);
                } else {
                    connection = (HttpURLConnection) url.openConnection();
                }
                
                connection.setInstanceFollowRedirects(false);
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);

                int statusCode = connection.getResponseCode();
                String location = connection.getHeaderField("Location");

                if (statusCode >= 300 && statusCode < 400 && location != null) {
                    URL newUrl = new URL(url, location);
                    currentUrl = newUrl.toString();
                    redirectCount++;
                    continue;
                } else {
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

    /**
     * 获取 HTTP 代理配置
     */
    private static Proxy getHttpProxy() {
        if (openCliProperties == null || openCliProperties.getProxy() == null
                || !openCliProperties.getProxy().isEnabled()) {
            return null;
        }

        String proxyUrl = openCliProperties.getProxyUrl();
        if (proxyUrl == null || proxyUrl.isEmpty()) {
            return null;
        }

        try {
            URL url = new URL(proxyUrl);
            String host = url.getHost();
            int port = url.getPort() > 0 ? url.getPort() : 7890;
            return new Proxy(Proxy.Type.HTTP, new InetSocketAddress(host, port));
        } catch (Exception e) {
            return null;
        }
    }

    public static void main(String[] args) throws Exception {
        String shortUrl = "https://t.co/HtWe54gHgR";
        String longUrl = expandShortUrl(shortUrl);
        System.out.println("长链接: " + longUrl);
    }
}
