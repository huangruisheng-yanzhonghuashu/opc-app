package com.opc.core.service.impl;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.LoadState;
import com.opc.core.domain.CoreMaterial;
import com.opc.core.service.ICoreMaterialMediaService;
import com.opc.core.service.ICoreMaterialService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class CoreMaterialMediaServiceImpl implements ICoreMaterialMediaService {

    private static final Logger log = LoggerFactory.getLogger(CoreMaterialMediaServiceImpl.class);

    @Autowired
    private ICoreMaterialService materialService;

    @Value("${opc.uploadPath:/upload}")
    private String uploadPath;

    @Override
    @Async
    public void downloadMediaAsync(CoreMaterial material) {
        if (material == null || material.getId() == null || material.getOriginalUrl() == null) {
            log.warn("素材信息不完整，跳过媒体下载");
            return;
        }

        Long materialId = material.getId();
        String originalUrl = material.getOriginalUrl();

        log.info("开始下载媒体文件，素材ID: {}, URL: {}", materialId, originalUrl);

        // 使用 Playwright 获取媒体
        List<MediaInfo> mediaList = fetchMediaWithPlaywright(originalUrl);
        
        if (mediaList.isEmpty()) {
            log.info("未找到媒体文件: {}", originalUrl);
            return;
        }

        try {
            List<String> downloadedPaths = new ArrayList<>();
            
            for (int i = 0; i < mediaList.size(); i++) {
                MediaInfo media = mediaList.get(i);
                String localPath = downloadFile(media.url, media.type, materialId);
                
                if (localPath != null) {
                    downloadedPaths.add(localPath);
                    
                    if (i == 0 && "image".equals(media.type)) {
                        material.setCoverImage(localPath);
                    }
                    if ("video".equals(media.type)) {
                        material.setVideoUrl(localPath);
                    }
                }
            }

            if (!downloadedPaths.isEmpty()) {
                appendMediaToContent(material, downloadedPaths);
                materialService.updateMaterial(material);
                log.info("媒体下载完成，素材ID: {}, 数量: {}", materialId, downloadedPaths.size());
            }

        } catch (Exception e) {
            log.error("保存媒体信息失败，素材ID: {}", materialId, e);
        }
    }

    /**
     * 使用 Playwright 访问推文详情页获取媒体URL
     */
    private List<MediaInfo> fetchMediaWithPlaywright(String tweetUrl) {
        List<MediaInfo> mediaList = new ArrayList<>();
        
        try (Playwright playwright = Playwright.create()) {
            BrowserType.LaunchOptions launchOptions = new BrowserType.LaunchOptions()
                    .setHeadless(true)
                    .setArgs(List.of(
                            "--no-sandbox",
                            "--disable-dev-shm-usage",
                            "--disable-gpu"
                    ));

            Browser browser = playwright.chromium().launch(launchOptions);
            BrowserContext context = browser.newContext(new Browser.NewContextOptions()
                    .setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36"));

            Page page = context.newPage();
            
            log.info("Playwright 访问推文页面: {}", tweetUrl);
            page.navigate(tweetUrl);
            page.waitForLoadState(LoadState.NETWORKIDLE);
            Thread.sleep(3000);

            // 获取页面HTML
            String html = page.content();
            Document doc = Jsoup.parse(html);

            // 提取图片 - 推文中的图片通常有特定属性
            Elements images = doc.select("img[src*='pbs.twimg.com'], img[src*='video.twimg.com']");
            for (Element img : images) {
                String src = img.attr("src");
                // 过滤掉头像等小图
                if (src.contains("media") || src.contains("tweet_video_thumb")) {
                    // 获取原图URL (去掉尺寸限制)
                    String origUrl = src.replaceAll(":\\w+$", "") + ":orig";
                    if (!containsUrl(mediaList, origUrl)) {
                        mediaList.add(new MediaInfo(origUrl, "image"));
                        log.info("找到图片: {}", origUrl);
                    }
                }
            }

            // 提取视频 - 查找视频元素或视频缩略图
            Elements videos = doc.select("video[src], video source[src]");
            for (Element video : videos) {
                String src = video.attr("src");
                if (!src.isEmpty() && !containsUrl(mediaList, src)) {
                    mediaList.add(new MediaInfo(src, "video"));
                    log.info("找到视频: {}", src);
                }
            }

            // 从script或meta中查找视频URL
            Elements scripts = doc.select("script");
            for (Element script : scripts) {
                String data = script.html();
                if (data.contains("video.twimg.com") || data.contains(".mp4")) {
                    // 提取视频URL
                    List<String> videoUrls = extractUrlsFromText(data, "video.twimg.com");
                    for (String url : videoUrls) {
                        if (url.contains(".mp4") && !containsUrl(mediaList, url)) {
                            mediaList.add(new MediaInfo(url, "video"));
                            log.info("从脚本找到视频: {}", url);
                        }
                    }
                }
            }

            browser.close();
            
        } catch (Exception e) {
            log.error("Playwright 获取媒体失败: {}", tweetUrl, e);
        }
        
        return mediaList;
    }

    private List<String> extractUrlsFromText(String text, String domain) {
        List<String> urls = new ArrayList<>();
        String regex = "https?://[^\\s\"'<>]+" + domain.replace(".", "\\.") + "[^\\s\"'<>]+";
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(regex);
        java.util.regex.Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            urls.add(matcher.group());
        }
        return urls;
    }

    private boolean containsUrl(List<MediaInfo> list, String url) {
        return list.stream().anyMatch(m -> m.url.equals(url));
    }

    private String downloadFile(String url, String type, Long materialId) {
        HttpURLConnection conn = null;
        try {
            URL u = new URL(url);
            conn = (HttpURLConnection) u.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(30000);
            conn.setReadTimeout(120000);
            conn.setRequestProperty("User-Agent", "Mozilla/5.0");

            if (conn.getResponseCode() != 200) {
                log.warn("下载失败，状态码: {}, URL: {}", conn.getResponseCode(), url);
                return null;
            }

            String ext = getExt(url, type);
            String name = UUID.randomUUID().toString().replace("-", "") + "." + ext;
            String relPath = String.format("material/%d/%s/%s", materialId, type, name);
            String fullPath = uploadPath + "/" + relPath;

            Path dir = Paths.get(uploadPath, "material", String.valueOf(materialId), type);
            if (!Files.exists(dir)) Files.createDirectories(dir);

            try (InputStream in = conn.getInputStream();
                 FileOutputStream out = new FileOutputStream(fullPath)) {
                byte[] buf = new byte[8192];
                int len;
                while ((len = in.read(buf)) != -1) {
                    out.write(buf, 0, len);
                }
            }

            log.info("下载成功: {}", fullPath);
            return "/" + relPath;

        } catch (Exception e) {
            log.error("下载失败: {}", url, e);
            return null;
        } finally {
            if (conn != null) conn.disconnect();
        }
    }

    private String getExt(String url, String type) {
        if (url == null) return "jpg";
        String clean = url.split("\\?")[0];
        int dot = clean.lastIndexOf('.');
        int slash = clean.lastIndexOf('/');
        if (dot > slash && dot != -1) {
            return clean.substring(dot + 1).toLowerCase();
        }
        return "video".equals(type) ? "mp4" : "jpg";
    }

    private void appendMediaToContent(CoreMaterial material, List<String> paths) {
        StringBuilder sb = new StringBuilder(material.getContent() != null ? material.getContent() : "");
        for (String path : paths) {
            if (path.contains("/video/")) {
                sb.append("<br><video src=\"").append(path).append("\" controls style=\"max-width:100%\"></video>");
            } else {
                sb.append("<br><img src=\"").append(path).append("\" style=\"max-width:100%\" />");
            }
        }
        material.setContent(sb.toString());
    }

    private static class MediaInfo {
        String url;
        String type;
        MediaInfo(String url, String type) {
            this.url = url;
            this.type = type;
        }
    }
}
