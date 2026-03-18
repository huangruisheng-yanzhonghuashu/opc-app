package com.ruoyi.web.controller.tool;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
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

public class TwitterScraper1 {

    private static final String LOGIN_URL = "https://x.com/i/flow/login";
    private static final String TARGET_URL = "https://x.com/thedankoe/highlights";

    private static final String USERNAME = "ruishengle@qq.com";
    private static final String PASSWORD = "hjz20121023";
    private static final int MAX_PAGES = 1;

    public static void main(String[] args) {
        System.out.println("=== Twitter 登录抓取程序 ===\n");

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36");
        WebDriver driver = new ChromeDriver(options);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        try {
            step1_openLoginPage(driver);
            step2_inputUsername(driver, wait, USERNAME);
            step3_inputPassword(driver, wait, PASSWORD);
            step4_waitLoginSuccess(driver, wait);
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
        }
    }

    private static void step1_openLoginPage(WebDriver driver) {
        System.out.println("[步骤 1] 打开 X 登录页面...");
        driver.get(LOGIN_URL);
        sleep(3000);
        System.out.println("   登录页面已打开: " + driver.getTitle());
    }

    private static void step2_inputUsername(WebDriver driver, WebDriverWait wait, String username) {
        System.out.println("\n[步骤 2] 输入用户名...");
        try {
            WebElement usernameInput = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.cssSelector("input[autocomplete='username']")));
            usernameInput.clear();
            usernameInput.sendKeys(username);
            sleep(500);

            WebElement nextButton = driver.findElement(By.xpath("//span[text()='下一步']/ancestor::button"));
            nextButton.click();
            sleep(2000);
            System.out.println("   用户名已输入并点击下一步");
        } catch (Exception e) {
            System.out.println("   输入用户名失败: " + e.getMessage());
        }
    }

    private static void step3_inputPassword(WebDriver driver, WebDriverWait wait, String password) {
        System.out.println("\n[步骤 3] 输入密码...");
        try {
            WebElement passwordInput = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.cssSelector("input[autocomplete='current-password']")));
            passwordInput.clear();
            passwordInput.sendKeys(password);
            sleep(500);

            WebElement loginButton = driver.findElement(By.xpath("//span[text()='登录']/ancestor::button"));
            loginButton.click();
            sleep(3000);
            System.out.println("   密码已输入并点击登录");
        } catch (Exception e) {
            System.out.println("   输入密码失败: " + e.getMessage());
        }
    }

    private static void step4_waitLoginSuccess(WebDriver driver, WebDriverWait wait) {
        System.out.println("\n[步骤 4] 等待登录成功...");
        try {
            wait.until(ExpectedConditions.urlContains("home"));
            System.out.println("   登录成功! 当前页面: " + driver.getCurrentUrl());
        } catch (Exception e) {
            System.out.println("   等待登录超时，当前页面: " + driver.getCurrentUrl());
        }
    }

    private static void step5_navigateToTarget(WebDriver driver, String url) {
        System.out.println("\n[步骤 5] 跳转到目标用户页面...");
        driver.get(url);
        sleep(3000);
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
                sleep(2000);
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

            // 点击每条推文进入详情页获取更多信息
            List<Map<String, String>> detailedTweets = scrapeTweetDetails(driver, wait, pageTweets);

            allTweets.addAll(detailedTweets);
            System.out.println("   第 " + page + " 页抓取完成，本页 " + detailedTweets.size() + " 条，累计 " + allTweets.size() + " 条");

            // 检查是否还有下一页
            if (page < maxPages) {
                boolean hasMore = scrollDownAndCheckMore(driver, wait);
                if (!hasMore) {
                    System.out.println("   已到达页面底部或没有更多推文，停止翻页");
                    break;
                }
                sleep(2000);
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
                    String detailUrl = "https://x.com/thedankoe/status/" + tweetId;
                    System.out.println("      请求地址: " + detailUrl);

                    // 直接访问详情页
                    try {
                        driver.get(detailUrl);
                        sleep(3000);

                        // 等待详情页加载
                        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[data-testid='tweet']")));
                        sleep(2000);

                        // 获取详情页信息
                        String detailPageSource = driver.getPageSource();

                        // 打印详情页内容
                        System.out.println("\n========== 详情页内容 ==========");
                        printDetailPageContent(detailPageSource);
                        System.out.println("========== 详情页内容结束 ==========\n");

                        // 解析详情页信息到tweetData
                        parseTweetDetail(detailPageSource, tweetData);

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
            // 方法1：从时间链接的href中提取
            Element timeElement = tweet.selectFirst("time");
            if (timeElement != null) {
                Element parent = timeElement.parent();
                if (parent != null) {
                    String href = parent.attr("href");
                    if (href != null && !href.isEmpty()) {
                        // URL格式通常是: /username/status/1234567890
                        String[] parts = href.split("/status/");
                        if (parts.length > 1) {
                            String id = parts[1].split("/")[0].split("\\?")[0];
                            return id;
                        }
                    }
                }
            }

            // 方法2：从aria-label中提取
            String ariaLabel = tweet.attr("aria-label");
            if (ariaLabel != null && !ariaLabel.isEmpty()) {
                // 可能包含推文ID
            }

            // 方法3：从data-*属性中查找
            try {
                org.jsoup.nodes.Attributes attrs = tweet.attributes();
                for (org.jsoup.nodes.Attribute attr : attrs) {
                    String key = attr.getKey();
                    if (key.startsWith("data-") && (key.contains("id") || key.contains("tweet-id"))) {
                        String value = attr.getValue();
                        if (value != null && !value.isEmpty() && value.matches("\\d+")) {
                            return value;
                        }
                    }
                }
            } catch (Exception e) {
                // 忽略异常
            }

            // 方法4：从URL中提取（如果是详情页）
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

    private static void parseTweetDetail(String pageSource, Map<String, String> tweetData) {
        Document doc = Jsoup.parse(pageSource);

        try {
            // 查找回复内容
            Elements replies = doc.select("[data-testid='tweet'][data-testid='reply']");
            if (!replies.isEmpty()) {
                List<String> replyTexts = new ArrayList<>();
                for (Element reply : replies) {
                    Element textElement = reply.selectFirst("[data-testid='tweetText']");
                    if (textElement != null) {
                        replyTexts.add(textElement.text());
                    }
                }
                if (!replyTexts.isEmpty()) {
                    tweetData.put("reply_contents", String.join(" ||| ", replyTexts));
                }
            }

            // 查找转推的内容
            Elements retweets = doc.select("[data-testid='tweet'][data-testid='retweet']");
            if (!retweets.isEmpty()) {
                List<String> retweetTexts = new ArrayList<>();
                for (Element retweet : retweets) {
                    Element textElement = retweet.selectFirst("[data-testid='tweetText']");
                    if (textElement != null) {
                        retweetTexts.add(textElement.text());
                    }
                }
                if (!retweetTexts.isEmpty()) {
                    tweetData.put("retweet_contents", String.join(" ||| ", retweetTexts));
                }
            }

            // 查找引用推文
            Element quotedTweet = doc.selectFirst("[data-testid='tweet'] [data-testid='tweet']");
            if (quotedTweet != null) {
                Element quotedText = quotedTweet.selectFirst("[data-testid='tweetText']");
                if (quotedText != null) {
                    tweetData.put("quoted_tweet", quotedText.text());
                }
            }

            // 查找媒体内容（图片、视频）
            Elements mediaElements = doc.select("[data-testid='tweet'] img, [data-testid='tweet'] video");
            if (!mediaElements.isEmpty()) {
                List<String> mediaUrls = new ArrayList<>();
                for (Element media : mediaElements) {
                    String src = media.attr("src");
                    if (!src.isEmpty() && !src.contains("avatar")) {
                        mediaUrls.add(src);
                    }
                }
                if (!mediaUrls.isEmpty()) {
                    tweetData.put("media_urls", String.join(" ||| ", mediaUrls));
                }
            }

        } catch (Exception e) {
            System.out.println("      解析详情页失败: " + e.getMessage());
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

            // 直接保存原始 HTML 源码，不做任何处理
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
                sleep(1000);
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
                // 获取推文ID（从URL或data属性中提取）
                String tweetId = extractTweetId(tweet);
                tweetData.put("id", tweetId);

                // 推文内容
                Element textElement = tweet.selectFirst("[data-testid='tweetText']");
                String text = textElement != null ? textElement.text() : "无内容";

                // 发布时间
                Element timeElement = tweet.selectFirst("time");
                String time = timeElement != null ? timeElement.attr("datetime") : "未知时间";

                // 用户名
                Element userElement = tweet.selectFirst("[data-testid='User-Name']");
                String user = userElement != null ? userElement.text() : "未知用户";

                // 回复数 - 查找所有可能的元素
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

                // 查看数 - Twitter 使用不同的选择器
                String views = "0";
                // 尝试多种可能的选择器
                Element viewGroup = tweet.selectFirst("[data-testid='views']");
                if (viewGroup != null) {
                    Element viewCount = viewGroup.selectFirst("span[dir='ltr']");
                    if (viewCount != null) {
                        views = viewCount.text();
                    } else {
                        views = viewGroup.text();
                    }
                } else {
                    // 尝试查找包含 "views" 文本的 aria-label
                    Elements viewElements = tweet.select("[aria-label]");
                    for (Element el : viewElements) {
                        String ariaLabel = el.attr("aria-label");
                        if (ariaLabel != null && (ariaLabel.contains("views") || ariaLabel.contains("查看") || ariaLabel.contains("View"))) {
                            views = ariaLabel;
                            break;
                        }
                    }
                    // 如果 views 还是 0，尝试查找所有包含数字的元素
                    if (views.equals("0")) {
                        Elements allSpans = tweet.select("span[dir='ltr']");
                        for (Element span : allSpans) {
                            String spanText = span.text();
                            // 查找包含 K, M, B 等缩写的数字（通常是查看数）
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

    private static void saveTweetsToFile(List<Map<String, String>> tweets) {
        try {
            String filename = "tweets_details_" + System.currentTimeMillis() + ".txt";
            java.io.PrintWriter writer = new java.io.PrintWriter(new java.io.FileWriter(filename, false));

            for (int i = 0; i < tweets.size(); i++) {
                Map<String, String> tweet = tweets.get(i);
                writer.println("=== 推文 " + (i + 1) + " ===");
                writer.println("ID: " + tweet.get("id"));
                writer.println("用户: " + tweet.get("user"));
                writer.println("时间: " + tweet.get("time"));
                writer.println("内容: " + tweet.get("text"));
                writer.println("回复: " + tweet.get("replies"));
                writer.println("转推: " + tweet.get("retweets"));
                writer.println("点赞: " + tweet.get("likes"));
                writer.println("查看: " + tweet.get("views"));

                // 新增的详情信息
                if (tweet.containsKey("reply_contents") && !tweet.get("reply_contents").isEmpty()) {
                    writer.println("--- 回复内容 ---");
                    String[] replies = tweet.get("reply_contents").split(" \\|\\|\\| ");
                    for (String reply : replies) {
                        writer.println("  " + reply);
                    }
                }

                if (tweet.containsKey("retweet_contents") && !tweet.get("retweet_contents").isEmpty()) {
                    writer.println("--- 转推内容 ---");
                    String[] retweets = tweet.get("retweet_contents").split(" \\|\\|\\| ");
                    for (String retweet : retweets) {
                        writer.println("  " + retweet);
                    }
                }

                if (tweet.containsKey("quoted_tweet") && !tweet.get("quoted_tweet").isEmpty()) {
                    writer.println("--- 引用推文 ---");
                    writer.println("  " + tweet.get("quoted_tweet"));
                }

                if (tweet.containsKey("media_urls") && !tweet.get("media_urls").isEmpty()) {
                    writer.println("--- 媒体链接 ---");
                    String[] medias = tweet.get("media_urls").split(" \\|\\|\\| ");
                    for (String media : medias) {
                        writer.println("  " + media);
                    }
                }

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
