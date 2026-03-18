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

public class TwitterScraper2 {

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
        options.addArguments("--remote-debugging-port=9222");
        options.addArguments("--disable-gpu");
        options.addArguments("--disable-software-rasterizer");
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

            // 抓取每条推文的详情内容
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

    private static void scrapeTweetDetails(WebDriver driver, WebDriverWait wait, List<Map<String, String>> tweets) {
        for (int i = 0; i < tweets.size(); i++) {
            Map<String, String> tweetData = tweets.get(i);

            try {
                String tweetId = tweetData.get("id");
                String username = tweetData.get("username");

                if (tweetId != null && !tweetId.equals("未知ID") && username != null && !username.equals("unknown")) {
                    System.out.println("   正在获取第 " + (i + 1) + " 条推文的详情 (ID: " + tweetId + ")...");

                    // 构造详情页 URL
                    String detailUrl = "https://x.com/" + username + "/status/" + tweetId;

                    // 访问详情页
                    try {
                        driver.get(detailUrl);
                        sleep(5000);

                        // 等待详情页加载
                        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[data-testid='tweet']")));
                        sleep(3000);

                        // 获取详情页信息
                        String detailPageSource = driver.getPageSource();
                        Document detailDoc = Jsoup.parse(detailPageSource);

                        // 提取推文内容（详情页可能有更多内容）
                        Element mainTweet = detailDoc.selectFirst("[data-testid='tweet']");
                        if (mainTweet != null) {
                            Element textElement = mainTweet.selectFirst("[data-testid='tweetText']");
                            String fullText = "";
                            if (textElement != null) {
                                // 保留换行符 - 递归处理所有子节点
                                fullText = extractTextWithNewlines(textElement);
                            }
                            tweetData.put("text", fullText);

                            // 提取并保存回复内容
                            saveTweetReplies(detailDoc, tweetId);

                            System.out.println("      详情获取成功，内容长度: " + fullText.length());
                        }

                    } catch (Exception e) {
                        System.out.println("      访问推文详情失败: " + e.getMessage());
                    }
                } else {
                    System.out.println("   第 " + (i + 1) + " 条推文无法获取ID或用户名，跳过");
                }

            } catch (Exception e) {
                System.out.println("   获取推文详情失败: " + e.getMessage());
            }
        }
    }

    private static String extractTextWithNewlines(Element element) {
        StringBuilder result = new StringBuilder();

        for (org.jsoup.nodes.Node node : element.childNodes()) {
            if (node instanceof org.jsoup.nodes.TextNode) {
                // 文本节点，直接添加
                String text = ((org.jsoup.nodes.TextNode) node).text();

                // 在 . 或 : 后添加换行
                text = text.replaceAll("\\.\\s*", ".\n");
                text = text.replaceAll(":\\s*", ":\n");

                result.append(text);
            } else if (node instanceof Element) {
                Element child = (Element) node;
                String tagName = child.tagName();

                // 如果是块级元素，添加换行
                if ("div".equals(tagName) || "p".equals(tagName) || "br".equals(tagName)) {
                    String childText = extractTextWithNewlines(child);
                    if (!childText.trim().isEmpty()) {
                        if (result.length() > 0 && !result.toString().endsWith("\n")) {
                            result.append("\n");
                        }
                        result.append(childText);
                    }
                } else if ("span".equals(tagName)) {
                    // span 元素，递归处理但不强制换行
                    String childText = extractTextWithNewlines(child);
                    result.append(childText);
                } else {
                    // 其他元素，递归处理
                    String childText = extractTextWithNewlines(child);
                    result.append(childText);
                }
            }
        }

        // 清理多余的换行
        String finalResult = result.toString().replaceAll("\n+", "\n").trim();

        return finalResult;
    }

    private static void saveTweetReplies(Document doc, String tweetId) {
        try {
            Elements replyTweets = doc.select("[data-testid='tweet']");
            if (replyTweets.size() > 1) {
                String filename = "TwitterScraper2_replies_" + tweetId + ".txt";
                java.io.PrintWriter writer = new java.io.PrintWriter(new java.io.FileWriter(filename, false), false);

                for (int i = 1; i < replyTweets.size(); i++) {
                    Element reply = replyTweets.get(i);
                    Element replyUser = reply.selectFirst("[data-testid='User-Name']");
                    Element replyText = reply.selectFirst("[data-testid='tweetText']");

                    if (replyText != null) {
                        writer.println("=== 回复 " + i + " ===");
                        if (replyUser != null) {
                            writer.println("用户: " + replyUser.text());
                        }

                        // 使用递归方法保留换行
                        String textContent = extractTextWithNewlines(replyText);

                        writer.println("内容: " + textContent);
                        writer.println();
                    }
                }

                writer.close();
                System.out.println("      回复已保存到文件: " + filename);
            }
        } catch (Exception e) {
            System.out.println("      保存回复失败: " + e.getMessage());
        }
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
            String filename = "TwitterScraper2_" + System.currentTimeMillis() + ".txt";
            java.io.PrintWriter writer = new java.io.PrintWriter(new java.io.FileWriter(filename, false), false);

            for (int i = 0; i < tweets.size(); i++) {
                Map<String, String> tweet = tweets.get(i);
                writer.println("ID: " + tweet.get("id"));
                writer.println("Time: " + tweet.get("time"));
                writer.println("User: " + tweet.get("user"));
                writer.println("Text: " + tweet.get("text"));
                writer.println("Replies: " + tweet.get("replies"));
                writer.println("Retweets: " + tweet.get("retweets"));
                writer.println("Likes: " + tweet.get("likes"));
                writer.println("Views: " + tweet.get("views"));
                writer.println();
            }

            writer.println("=== 共 " + tweets.size() + " 条推文 ===");
            writer.close();

            System.out.println("推文已保存到文件: " + filename);
        } catch (Exception e) {
            System.out.println("保存文件失败: " + e.getMessage());
        }
    }



    private static void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
