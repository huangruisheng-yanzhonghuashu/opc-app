package com.opc.web.controller.tool;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.WaitForSelectorState;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TwitterScraper {

    private static final String LOGIN_URL = "https://x.com/i/flow/login";
    private static final String TARGET_URL = "https://x.com/thedankoe";

    private static final String USERNAME = "ruishengle@qq.com";
    private static final String PASSWORD = "hjz20121023";
    private static final int MAX_PAGES = 2;

    public static void main(String[] args) {
        System.out.println("=== Twitter 登录抓取程序 (Playwright) ===\n");

        // 创建 Playwright 实例
        try (Playwright playwright = Playwright.create()) {
            BrowserType.LaunchOptions launchOptions = new BrowserType.LaunchOptions()
                    .setHeadless(false)
                    .setArgs(List.of(
                            "--no-sandbox",
                            "--disable-dev-shm-usage",
                            "--disable-blink-features=AutomationControlled"
                    ));

            Browser browser = playwright.chromium().launch(launchOptions);
            BrowserContext context = browser.newContext(new Browser.NewContextOptions()
                    .setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36"));
            Page page = context.newPage();

            try {
                step1_openLoginPage(page);
                step2_inputUsername(page, USERNAME);
                step3_inputPassword(page, PASSWORD);
                step4_waitLoginSuccess(page);
                step5_navigateToTarget(page, TARGET_URL);
                step6_scrapeMultiplePages(page, MAX_PAGES);

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
                browser.close();
            }
        }
    }

    private static void step1_openLoginPage(Page page) {
        System.out.println("[步骤 1] 打开 X 登录页面...");
        page.navigate(LOGIN_URL);
        sleep(3000);
        System.out.println("   登录页面已打开: " + page.title());
    }

    private static void step2_inputUsername(Page page, String username) {
        System.out.println("\n[步骤 2] 输入用户名...");
        try {
            // 等待并填写用户名
            page.waitForSelector("input[autocomplete='username']", new Page.WaitForSelectorOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(15000));
            page.fill("input[autocomplete='username']", "");
            page.fill("input[autocomplete='username']", username);
            sleep(500);

            // 点击下一步按钮
            page.click("//span[text()='下一步']/ancestor::button");
            sleep(2000);
            System.out.println("   用户名已输入并点击下一步");
        } catch (Exception e) {
            System.out.println("   输入用户名失败: " + e.getMessage());
        }
    }

    private static void step3_inputPassword(Page page, String password) {
        System.out.println("\n[步骤 3] 输入密码...");
        try {
            // 等待并填写密码
            page.waitForSelector("input[autocomplete='current-password']", new Page.WaitForSelectorOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(15000));
            page.fill("input[autocomplete='current-password']", "");
            page.fill("input[autocomplete='current-password']", password);
            sleep(500);

            // 点击登录按钮
            page.click("//span[text()='登录']/ancestor::button");
            sleep(3000);
            System.out.println("   密码已输入并点击登录");
        } catch (Exception e) {
            System.out.println("   输入密码失败: " + e.getMessage());
        }
    }

    private static void step4_waitLoginSuccess(Page page) {
        System.out.println("\n[步骤 4] 等待登录成功...");
        try {
            page.waitForURL(url -> url.contains("home"), new Page.WaitForURLOptions().setTimeout(15000));
            System.out.println("   登录成功! 当前页面: " + page.url());
        } catch (Exception e) {
            System.out.println("   等待登录超时，当前页面: " + page.url());
        }
    }

    private static void step5_navigateToTarget(Page page, String url) {
        System.out.println("\n[步骤 5] 跳转到目标用户页面...");
        page.navigate(url);
        sleep(3000);
        System.out.println("   已跳转到: " + page.url());
    }

    private static void step6_scrapeMultiplePages(Page page, int maxPages) {
        System.out.println("\n[步骤 6] 开始抓取多页推文 (最多 " + maxPages + " 页)...");

        List<Map<String, String>> allTweets = new ArrayList<>();
        String lastTweetId = "";

        for (int pageNum = 1; pageNum <= maxPages; pageNum++) {
            System.out.println("\n=== 正在抓取第 " + pageNum + " 页 ===");

            // 等待推文加载
            try {
                page.waitForSelector("[data-testid='tweet']", new Page.WaitForSelectorOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(15000));
                sleep(2000);
            } catch (Exception e) {
                System.out.println("   等待推文加载超时: " + e.getMessage());
                break;
            }

            // 获取页面源码并解析
            String pageSource = page.content();
            List<Map<String, String>> pageTweets = parseTweetsWithJsoup(pageSource);

            if (pageTweets.isEmpty()) {
                System.out.println("   第 " + pageNum + " 页未找到推文，停止翻页");
                break;
            }

            allTweets.addAll(pageTweets);
            System.out.println("   第 " + pageNum + " 页抓取完成，本页 " + pageTweets.size() + " 条，累计 " + allTweets.size() + " 条");

            // 检查是否还有下一页
            if (pageNum < maxPages) {
                boolean hasMore = scrollDownAndCheckMore(page, lastTweetId);
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

    private static boolean scrollDownAndCheckMore(Page page, String lastTweetId) {
        try {
            // 滚动到底部加载更多
            for (int i = 0; i < 3; i++) {
                page.evaluate("() => { window.scrollTo(0, document.body.scrollHeight); }");
                sleep(1000);
            }

            // 检查是否有新的推文加载
            Elements tweets = Jsoup.parse(page.content()).select("[data-testid='tweet']");
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
                // 推文ID - 从href链接中提取
                String id = "";
                Element tweetElement = tweet.selectFirst("a[href*='/status/']");
                if (tweetElement != null) {
                    String href = tweetElement.attr("href");
                    if (href.contains("/status/")) {
                        String[] parts = href.split("/status/");
                        if (parts.length > 1) {
                            id = parts[1].split("[?#]")[0];
                        }
                    }
                }
                if (id.isEmpty()) {
                    id = tweet.attr("data-testid");
                }

                // 推文内容（暂时不需要，但保留以便调试）
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

                tweetData.put("id", id);
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
            String filename = "tweets_" + System.currentTimeMillis() + ".txt";
            java.io.PrintWriter writer = new java.io.PrintWriter(new java.io.FileWriter(filename, false));

            for (int i = 0; i < tweets.size(); i++) {
                Map<String, String> tweet = tweets.get(i);
                writer.println("=== 推文 " + (i + 1) + " ===");
                writer.println("用户: " + tweet.get("user"));
                writer.println("时间: " + tweet.get("time"));
                writer.println("内容: " + tweet.get("text"));
                writer.println("回复: " + tweet.get("replies"));
                writer.println("转推: " + tweet.get("retweets"));
                writer.println("点赞: " + tweet.get("likes"));
                writer.println("查看: " + tweet.get("views"));
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
