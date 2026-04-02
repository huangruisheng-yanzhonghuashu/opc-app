package com.opc.common.utils;

import com.opc.web.config.twitter.v2.TwitterApiV2Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;

@Component
public class ShortUrlResolver {

    private static TwitterApiV2Properties twitterApiV2Properties;

    @Autowired
    public void setTwitterApiV2Properties(TwitterApiV2Properties properties) {
        ShortUrlResolver.twitterApiV2Properties = properties;
    }

    public static String expandShortUrl(String shortUrl) throws Exception {
        int maxRedirects = 10;
        int redirectCount = 0;
        String currentUrl = shortUrl;

        boolean proxyEnabled = twitterApiV2Properties != null && twitterApiV2Properties.isProxyEnabled();
        String proxyHost = twitterApiV2Properties != null ? twitterApiV2Properties.getProxyHost() : "localhost";
        int proxyPort = twitterApiV2Properties != null ? twitterApiV2Properties.getProxyPort() : 7899;

        while (redirectCount < maxRedirects) {
            HttpURLConnection connection = null;
            try {
                URL url = new URL(currentUrl);
                
                if (proxyEnabled) {
                    Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPort));
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

    public static void main(String[] args) throws Exception {
        String shortUrl = "https://t.co/HtWe54gHgR";
        String longUrl = expandShortUrl(shortUrl);
        System.out.println("长链接: " + longUrl);
    }
}
