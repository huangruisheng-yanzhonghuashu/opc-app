package com.opc.web.controller.tool;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TwitterScraper3 {

    private static final String TARGET_URL = "https://x.com/search?q=Money%20%2F%20Wealth%20Angle&src=recent_search_click";
    private static final int MAX_PAGES = 1;
    private static final String AUTH_TOKEN = "3bd653f7c2bbc1486afd8187b6f67558fb8e5274";

    public static void main(String[] args) {
        System.out.println("=== Twitter 搜索抓取程序 ===\n");
        System.out.println("正在启动浏览器...");

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36");
        options.addArguments("--start-maximized");
        options.addArguments("--user-data-dir=d:/opc/chrome_user_data");

        WebDriver driver = new ChromeDriver(options);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        try {
            // 先访问X.com以设置cookie
            System.out.println("正在设置登录Cookie...");
            driver.get("https://x.com");
            sleep(4000);

            // 添加auth_token cookie
            Cookie authCookie = new Cookie("auth_token", AUTH_TOKEN, ".x.com", "/", null);
            driver.manage().addCookie(authCookie);
            System.out.println("   Cookie已设置: auth_token");

            // 刷新页面以应用cookie
            driver.navigate().refresh();
            sleep(5000);

            System.out.println("   当前页面: " + driver.getCurrentUrl());

            step5_navigateToTarget(driver, TARGET_URL);
            step6_scrapeMultiplePages(driver, wait, MAX_PAGES);

        } catch (Exception e) {
            System.out.println("错误: " + e.getMessage());
            e.printStackTrace();
        } finally {
            System.out.println("\n按回车键关闭浏览器...");
            try {
                System.in.read();
            } catch (IOException e) {
                e.printStackTrace();
            }
            driver.quit();
            System.out.println("程序已退出");
        }
    }

    private static void step5_navigateToTarget(WebDriver driver, String url) {
        System.out.println("\n[步骤 5] 跳转到搜索页面...");
        driver.get(url);
        sleep(5000);
        System.out.println("   已跳转到: " + driver.getCurrentUrl());
    }

    private static void step6_scrapeMultiplePages(WebDriver driver, WebDriverWait wait, int maxPages) {
        System.out.println("\n[步骤 6] 开始抓取多页推文 (最多 " + maxPages + " 页)...");

        List<Map<String, String>> allTweets = new ArrayList<>();

        for (int page = 1; page <= maxPages; page++) {
            System.out.println("\n=== 正在抓取第 " + page + " 页 ===");

            // 等待推文加载
            try {
                wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[data-testid='tweet']")));
                sleep(4000);
            } catch (Exception e) {
                System.out.println("   等待推文加载超时: " + e.getMessage());
                break;
            }

            // 获取页面源码并解析
            String pageSource = driver.getPageSource();
            List<Map<String, String>> pageTweets = parseTweetsWithJsoup(pageSource);

            if (pageTweets.isEmpty()) {
                System.out.println("   第 " + page + " 页未找到推文，停止翻页");
                break;
            }

            // 访问每条推文的详情页
            scrapeTweetDetails(driver, wait, pageTweets);

            allTweets.addAll(pageTweets);
            System.out.println("   第 " + page + " 页抓取完成，本页 " + pageTweets.size() + " 条，累计 " + allTweets.size() + " 条");

            // 检查是否还有下一页
            if (page < maxPages) {
                boolean hasMore = scrollDownAndCheckMore(driver, wait);
                if (!hasMore) {
                    System.out.println("   已到达页面底部或没有更多推文，停止翻页");
                    break;
                }
                sleep(4000);
            }
        }

        // 输出汇总
        System.out.println("\n=== 抓取完成 ===");
        System.out.println("共抓取 " + allTweets.size() + " 条推文");

        // 保存到文件（可选）
        saveTweetsToFile(allTweets);
    }

    private static List<Map<String, String>> scrapeTweetDetails(WebDriver driver, WebDriverWait wait, List<Map<String, String>> tweets) {
        List<Map<String, String>> detailedTweets = new ArrayList<>();

        for (int i = 0; i < tweets.size(); i++) {
            Map<String, String> tweetData = tweets.get(i);

            try {
                String tweetId = tweetData.get("id");

                if (tweetId != null && !tweetId.equals("未知ID")) {
                    System.out.println("   正在获取第 " + (i + 1) + " 条推文的详情 (ID: " + tweetId + ")...");

                    // 构造详情页 URL
                    String detailUrl = "https://x.com/" + tweetData.get("username") + "/status/" + tweetId;
                    System.out.println("      请求地址: " + detailUrl);

                    // 直接访问详情页
                    try {
                        driver.get(detailUrl);
                        sleep(5000);

                        // 等待详情页加载
                        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[data-testid='tweet']")));
                        sleep(4000);

                        // 获取详情页信息
                        String detailPageSource = driver.getPageSource();

                        // 打印详情页内容
                        System.out.println("\n========== 详情页内容 ==========");
                        printDetailPageContent(detailPageSource);
                        System.out.println("========== 详情页内容结束 ==========\n");

                        // 保存详情页HTML到文件
                        saveDetailPageHtml(detailUrl, detailPageSource, tweetId);

                        System.out.println("      详情获取成功");

                    } catch (Exception e) {
                        System.out.println("      访问推文详情失败: " + e.getMessage());
                    }
                } else {
                    System.out.println("   第 " + (i + 1) + " 条推文无法获取ID，跳过详情获取");
                    detailedTweets.add(tweetData);
                    continue;
                }

                detailedTweets.add(tweetData);

            } catch (Exception e) {
                System.out.println("   获取推文详情失败: " + e.getMessage());
                detailedTweets.add(tweetData);
            }
        }

        return detailedTweets;
    }

    private static String extractTweetId(Element tweet) {
        try {
            // 从时间链接的href中提取
            Element timeElement = tweet.selectFirst("time");
            if (timeElement != null) {
                Element parent = timeElement.parent();
                if (parent != null) {
                    String href = parent.attr("href");
                    if (href != null && !href.isEmpty()) {
                        String[] parts = href.split("/status/");
                        if (parts.length > 1) {
                            String id = parts[1].split("/")[0].split("\\?")[0];
                            return id;
                        }
                    }
                }
            }

            // 从URL中提取
            Elements links = tweet.select("a[href*='/status/']");
            for (Element link : links) {
                String href = link.attr("href");
                if (href != null && href.contains("/status/")) {
                    String[] parts = href.split("/status/");
                    if (parts.length > 1) {
                        String id = parts[1].split("/")[0].split("\\?")[0];
                        if (id.matches("\\d+")) {
                            return id;
                        }
                    }
                }
            }

            return "未知ID";
        } catch (Exception e) {
            return "未知ID";
        }
    }

    private static void printDetailPageContent(String pageSource) {
        Document doc = Jsoup.parse(pageSource);

        try {
            // 获取主推文
            Element mainTweet = doc.selectFirst("[data-testid='tweet']");
            if (mainTweet != null) {
                // 推文ID
                String tweetId = extractTweetId(mainTweet);
                System.out.println("推文ID: " + tweetId);

                // 推文内容
                Element textElement = mainTweet.selectFirst("[data-testid='tweetText']");
                if (textElement != null) {
                    System.out.println("推文内容: " + textElement.text());
                }

                // 发布时间
                Element timeElement = mainTweet.selectFirst("time");
                if (timeElement != null) {
                    System.out.println("发布时间: " + timeElement.attr("datetime"));
                }

                // 用户名
                Element userElement = mainTweet.selectFirst("[data-testid='User-Name']");
                if (userElement != null) {
                    System.out.println("用户: " + userElement.text());
                }

                // 回复数
                Element replyGroup = mainTweet.selectFirst("[data-testid='reply']");
                if (replyGroup != null) {
                    Element replyCount = replyGroup.selectFirst("span[dir='ltr']");
                    String replies = replyCount != null ? replyCount.text() : replyGroup.text();
                    System.out.println("回复数: " + replies);
                }

                // 转推数
                Element retweetGroup = mainTweet.selectFirst("[data-testid='retweet']");
                if (retweetGroup != null) {
                    Element retweetCount = retweetGroup.selectFirst("span[dir='ltr']");
                    String retweets = retweetCount != null ? retweetCount.text() : retweetGroup.text();
                    System.out.println("转推数: " + retweets);
                }

                // 点赞数
                Element likeGroup = mainTweet.selectFirst("[data-testid='like']");
                if (likeGroup != null) {
                    Element likeCount = likeGroup.selectFirst("span[dir='ltr']");
                    String likes = likeCount != null ? likeCount.text() : likeGroup.text();
                    System.out.println("点赞数: " + likes);
                }

                // 查看数
                Element viewGroup = mainTweet.selectFirst("[data-testid='views']");
                if (viewGroup != null) {
                    Element viewCount = viewGroup.selectFirst("span[dir='ltr']");
                    String views = viewCount != null ? viewCount.text() : viewGroup.text();
                    System.out.println("查看数: " + views);
                }
            }

            // 查找引用推文
            Element quotedTweet = doc.selectFirst("[data-testid='tweet'] [data-testid='tweet']");
            if (quotedTweet != null) {
                Element quotedText = quotedTweet.selectFirst("[data-testid='tweetText']");
                if (quotedText != null) {
                    System.out.println("引用推文: " + quotedText.text());
                }
            }

            // 查找回复内容
            Elements replies = doc.select("[data-testid='tweet'] [data-testid='tweet']");
            if (replies.size() > 1) {
                System.out.println("回复数: " + (replies.size() - 1));
                for (int i = 1; i < Math.min(replies.size(), 4); i++) {
                    Element reply = replies.get(i);
                    Element replyText = reply.selectFirst("[data-testid='tweetText']");
                    if (replyText != null) {
                        System.out.println("回复 " + i + ": " + replyText.text());
                    }
                }
            }

            // 查找媒体内容
            Elements mediaElements = doc.select("[data-testid='tweet'] img, [data-testid='tweet'] video");
            List<String> mediaUrls = new ArrayList<>();
            for (Element media : mediaElements) {
                String src = media.attr("src");
                if (!src.isEmpty() && !src.contains("avatar")) {
                    mediaUrls.add(src);
                }
            }
            if (!mediaUrls.isEmpty()) {
                System.out.println("媒体链接数: " + mediaUrls.size());
                for (int i = 0; i < Math.min(mediaUrls.size(), 3); i++) {
                    System.out.println("媒体 " + (i + 1) + ": " + mediaUrls.get(i));
                }
            }

        } catch (Exception e) {
            System.out.println("打印详情页内容失败: " + e.getMessage());
        }
    }

    private static void saveDetailPageHtml(String detailUrl, String pageSource, String tweetId) {
        try {
            String filename = "tweet_detail_" + tweetId + ".html";
            java.io.PrintWriter writer = new java.io.PrintWriter(new java.io.FileWriter(filename, false), false);
            writer.print(pageSource);
            writer.close();
            System.out.println("      详情页原始 HTML 已保存到文件: " + filename);
        } catch (Exception e) {
            System.out.println("      保存详情页内容失败: " + e.getMessage());
        }
    }

    private static boolean scrollDownAndCheckMore(WebDriver driver, WebDriverWait wait) {
        try {
            // 滚动到底部加载更多
            WebElement body = driver.findElement(By.tagName("body"));
            for (int i = 0; i < 3; i++) {
                ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight);");
                sleep(3000);
            }

            // 检查是否有新的推文加载
            Elements tweets = Jsoup.parse(driver.getPageSource()).select("[data-testid='tweet']");
            if (tweets.size() == 0) {
                return false;
            }

            return true;
        } catch (Exception e) {
            System.out.println("   翻页失败: " + e.getMessage());
            return false;
        }
    }

    private static List<Map<String, String>> parseTweetsWithJsoup(String pageSource) {
        Document doc = Jsoup.parse(pageSource);
        Elements tweets = doc.select("[data-testid='tweet']");

        List<Map<String, String>> tweetList = new ArrayList<>();

        for (int i = 0; i < tweets.size(); i++) {
            Element tweet = tweets.get(i);
            Map<String, String> tweetData = new HashMap<>();

            try {
                // 获取推文ID
                String tweetId = extractTweetId(tweet);
                tweetData.put("id", tweetId);

                // 提取用户名（从用户链接中获取）
                String username = extractUsername(tweet);
                tweetData.put("username", username);

                // 推文内容
                Element textElement = tweet.selectFirst("[data-testid='tweetText']");
                String text = textElement != null ? textElement.text() : "无内容";

                // 发布时间
                Element timeElement = tweet.selectFirst("time");
                String time = timeElement != null ? timeElement.attr("datetime") : "未知时间";

                // 用户名
                Element userElement = tweet.selectFirst("[data-testid='User-Name']");
                String user = userElement != null ? userElement.text() : "未知用户";

                // 回复数
                String replies = "0";
                Element replyGroup = tweet.selectFirst("[data-testid='reply']");
                if (replyGroup != null) {
                    Element replyCount = replyGroup.selectFirst("span[dir='ltr']");
                    if (replyCount != null) {
                        replies = replyCount.text();
                    } else {
                        replies = replyGroup.text();
                    }
                }

                // 转推数
                String retweets = "0";
                Element retweetGroup = tweet.selectFirst("[data-testid='retweet']");
                if (retweetGroup != null) {
                    Element retweetCount = retweetGroup.selectFirst("span[dir='ltr']");
                    if (retweetCount != null) {
                        retweets = retweetCount.text();
                    } else {
                        retweets = retweetGroup.text();
                    }
                }

                // 点赞数
                String likes = "0";
                Element likeGroup = tweet.selectFirst("[data-testid='like']");
                if (likeGroup != null) {
                    Element likeCount = likeGroup.selectFirst("span[dir='ltr']");
                    if (likeCount != null) {
                        likes = likeCount.text();
                    } else {
                        likes = likeGroup.text();
                    }
                }

                // 查看数
                String views = "0";
                Element viewGroup = tweet.selectFirst("[data-testid='views']");
                if (viewGroup != null) {
                    Element viewCount = viewGroup.selectFirst("span[dir='ltr']");
                    if (viewCount != null) {
                        views = viewCount.text();
                    } else {
                        views = viewGroup.text();
                    }
                } else {
                    Elements viewElements = tweet.select("[aria-label]");
                    for (Element el : viewElements) {
                        String ariaLabel = el.attr("aria-label");
                        if (ariaLabel != null && (ariaLabel.contains("views") || ariaLabel.contains("查看") || ariaLabel.contains("View"))) {
                            views = ariaLabel;
                            break;
                        }
                    }
                    if (views.equals("0")) {
                        Elements allSpans = tweet.select("span[dir='ltr']");
                        for (Element span : allSpans) {
                            String spanText = span.text();
                            if (!spanText.isEmpty() && (spanText.contains("K") || spanText.contains("M") || spanText.contains("B") || spanText.matches(".*\\d{4,}.*"))) {
                                views = spanText;
                                break;
                            }
                        }
                    }
                }

                tweetData.put("text", text);
                tweetData.put("time", time);
                tweetData.put("user", user);
                tweetData.put("replies", replies);
                tweetData.put("retweets", retweets);
                tweetData.put("likes", likes);
                tweetData.put("views", views);

                tweetList.add(tweetData);
            } catch (Exception e) {
                System.out.println("   解析推文失败: " + e.getMessage());
            }
        }

        return tweetList;
    }

    private static String extractUsername(Element tweet) {
        try {
            // 从用户链接中提取用户名
            Elements userLinks = tweet.select("a[href^='/']");
            for (Element link : userLinks) {
                String href = link.attr("href");
                if (href != null && href.matches("^/[^/]+$") && !href.contains("status") && !href.contains("search")) {
                    return href.substring(1); // 去掉开头的 /
                }
            }
            return "unknown";
        } catch (Exception e) {
            return "unknown";
        }
    }

    private static void saveTweetsToFile(List<Map<String, String>> tweets) {
        try {
            String filename = "tweets_" + System.currentTimeMillis() + ".html";
            java.io.PrintWriter writer = new java.io.PrintWriter(new java.io.FileWriter(filename, false), false);

            // 写入 HTML 头部
            writer.println("<!DOCTYPE html>");
            writer.println("<html lang=\"zh-CN\">");
            writer.println("<head>");
            writer.println("    <meta charset=\"UTF-8\">");
            writer.println("    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">");
            writer.println("    <title>Twitter 推文抓取结果</title>");
            writer.println("    <style>");
            writer.println("        body {");
            writer.println("            font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;");
            writer.println("            background-color: #f5f8fa;");
            writer.println("            margin: 0;");
            writer.println("            padding: 20px;");
            writer.println("        }");
            writer.println("        .container {");
            writer.println("            max-width: 800px;");
            writer.println("            margin: 0 auto;");
            writer.println("        }");
            writer.println("        .header {");
            writer.println("            text-align: center;");
            writer.println("            margin-bottom: 30px;");
            writer.println("            color: #1da1f2;");
            writer.println("        }");
            writer.println("        .tweet {");
            writer.println("            background: white;");
            writer.println("            border: 1px solid #e1e8ed;");
            writer.println("            border-radius: 10px;");
            writer.println("            padding: 20px;");
            writer.println("            margin-bottom: 20px;");
            writer.println("            box-shadow: 0 2px 4px rgba(0,0,0,0.1);");
            writer.println("        }");
            writer.println("        .tweet-header {");
            writer.println("            margin-bottom: 12px;");
            writer.println("        }");
            writer.println("        .user {");
            writer.println("            font-weight: bold;");
            writer.println("            color: #14171a;");
            writer.println("        }");
            writer.println("        .time {");
            writer.println("            color: #657786;");
            writer.println("            font-size: 14px;");
            writer.println("        }");
            writer.println("        .tweet-text {");
            writer.println("            color: #14171a;");
            writer.println("            line-height: 1.6;");
            writer.println("            margin: 15px 0;");
            writer.println("            white-space: pre-wrap;");
            writer.println("            word-wrap: break-word;");
            writer.println("        }");
            writer.println("        .tweet-stats {");
            writer.println("            display: flex;");
            writer.println("            gap: 20px;");
            writer.println("            color: #657786;");
            writer.println("            font-size: 14px;");
            writer.println("            margin-top: 15px;");
            writer.println("        }");
            writer.println("        .stat {");
            writer.println("            display: flex;");
            writer.println("            align-items: center;");
            writer.println("            gap: 5px;");
            writer.println("        }");
            writer.println("        .stat-icon {");
            writer.println("            font-size: 16px;");
            writer.println("        }");
            writer.println("        .footer {");
            writer.println("            text-align: center;");
            writer.println("            margin-top: 30px;");
            writer.println("            color: #657786;");
            writer.println("        }");
            writer.println("    </style>");
            writer.println("</head>");
            writer.println("<body>");
            writer.println("    <div class=\"container\">");
            writer.println("        <div class=\"header\">");
            writer.println("            <h1>🐦 Twitter 推文抓取结果</h1>");
            writer.println("            <p>共抓取 " + tweets.size() + " 条推文</p>");
            writer.println("        </div>");

            // 写入每条推文
            for (int i = 0; i < tweets.size(); i++) {
                Map<String, String> tweet = tweets.get(i);
                writer.println("        <div class=\"tweet\">");
                writer.println("            <div class=\"tweet-header\">");
                writer.println("                <span class=\"user\">" + escapeHtml(tweet.get("user")) + "</span><br>");
                writer.println("                <span class=\"time\">" + escapeHtml(tweet.get("time")) + "</span>");
                writer.println("            </div>");
                writer.println("            <div class=\"tweet-text\">" + escapeHtml(tweet.get("text")) + "</div>");
                writer.println("            <div class=\"tweet-stats\">");
                writer.println("                <span class=\"stat\"><span class=\"stat-icon\">💬</span>" + escapeHtml(tweet.get("replies")) + "</span>");
                writer.println("                <span class=\"stat\"><span class=\"stat-icon\">🔄</span>" + escapeHtml(tweet.get("retweets")) + "</span>");
                writer.println("                <span class=\"stat\"><span class=\"stat-icon\">❤️</span>" + escapeHtml(tweet.get("likes")) + "</span>");
                writer.println("                <span class=\"stat\"><span class=\"stat-icon\">👁️</span>" + escapeHtml(tweet.get("views")) + "</span>");
                writer.println("            </div>");
                writer.println("        </div>");
            }

            // 写入 HTML 尾部
            writer.println("        <div class=\"footer\">");
            writer.println("            <p>抓取时间: " + new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date()) + "</p>");
            writer.println("        </div>");
            writer.println("    </div>");
            writer.println("</body>");
            writer.println("</html>");

            writer.close();

            System.out.println("推文已保存到文件: " + filename);
        } catch (Exception e) {
            System.out.println("保存文件失败: " + e.getMessage());
        }
    }

    private static String escapeHtml(String text) {
        if (text == null) return "";
        return text.replace("&", "&amp;")
                   .replace("<", "&lt;")
                   .replace(">", "&gt;")
                   .replace("\"", "&quot;")
                   .replace("'", "&#39;");
    }



    private static void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
