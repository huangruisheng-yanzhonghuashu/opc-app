package com.opc.web.service.core;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opc.common.core.domain.AjaxResult;
import com.opc.core.domain.CoreMaterial;
import com.opc.core.service.ICoreMaterialService;
import com.opc.web.controller.core.OpenCliCommandBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static com.opc.web.controller.core.OpenCliConstants.*;

/**
 * 采集信息源数据获取服务
 * <p>
 * 封装 Twitter 和 Reddit 的数据获取和导入逻辑，供多个控制器复用
 * </p>
 *
 * @author opc
 * @since 3.9.1
 */
@Service
public class CollectSourceFetchService {

    private static final Logger log = LoggerFactory.getLogger(CollectSourceFetchService.class);

    @Autowired
    private ICoreMaterialService materialService;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 获取 Twitter 数据并导入
     *
     * @param keyword 搜索关键词
     * @return 导入结果
     */
    public AjaxResult fetchTwitterData(String keyword) {
        try {
            String sourceType = SOURCE_TWITTER;

            // 执行 opencli 命令获取 Twitter 数据
            String jsonResult = executeOpenCliCommand(MODULE_TWITTER, SUBCOMMAND_SEARCH, keyword);
            if (jsonResult == null || jsonResult.isEmpty()) {
                return AjaxResult.error("未获取到 Twitter 数据");
            }

            // 解析并保存数据
            List<CoreMaterial> materials = parseTwitterJson(jsonResult, sourceType);
            ImportResult result = importMaterials(materials);

            AjaxResult ajaxResult = AjaxResult.success("导入完成");
            ajaxResult.put("keyword", keyword);
            ajaxResult.put("sourceType", sourceType);
            ajaxResult.put("total", result.total);
            ajaxResult.put("successCount", result.successCount);
            ajaxResult.put("failCount", result.failCount);
            return ajaxResult;

        } catch (Exception e) {
            log.error("Twitter 搜索导入失败", e);
            return AjaxResult.error("导入失败: " + e.getMessage());
        }
    }

    /**
     * 获取 Reddit 数据并导入
     *
     * @param keyword 搜索关键词
     * @return 导入结果
     */
    public AjaxResult fetchRedditData(String keyword) {
        try {
            String sourceType = SOURCE_REDDIT;

            // 执行 opencli reddit search 命令获取数据
            String jsonResult = executeOpenCliRedditCommand(keyword);
            if (jsonResult == null || jsonResult.isEmpty()) {
                return AjaxResult.error("未获取到 Reddit 数据");
            }

            // 解析并保存数据
            List<CoreMaterial> materials = parseRedditJson(jsonResult, sourceType);
            ImportResult result = importMaterials(materials);

            AjaxResult ajaxResult = AjaxResult.success("导入完成");
            ajaxResult.put("keyword", keyword);
            ajaxResult.put("sourceType", sourceType);
            ajaxResult.put("total", result.total);
            ajaxResult.put("successCount", result.successCount);
            ajaxResult.put("failCount", result.failCount);
            return ajaxResult;

        } catch (Exception e) {
            log.error("Reddit 搜索导入失败", e);
            return AjaxResult.error("导入失败: " + e.getMessage());
        }
    }

    /**
     * 导入素材列表
     */
    private ImportResult importMaterials(List<CoreMaterial> materials) {
        int successCount = 0;
        int failCount = 0;

        for (CoreMaterial material : materials) {
            String originalId = material.getOriginalId();
            try {
                materialService.insertMaterial(material);
                successCount++;
            } catch (Exception e) {
                log.error("导入素材失败, originalId: {}", originalId, e);
                failCount++;
            }
        }

        return new ImportResult(materials.size(), successCount, failCount);
    }

    /**
     * 执行 opencli 命令获取数据
     */
    private String executeOpenCliCommand(String module, String subCommand, String keyword) throws Exception {
        // 使用命令构建器构建命令
        OpenCliCommandBuilder builder = new OpenCliCommandBuilder()
                .withModule(module)
                .withSubCommand(subCommand)
                .withArg(keyword)
                .withOption("--limit", "10")
                .withOption(OPTION_FORMAT_JSON, VALUE_JSON);

        return executeCommand(builder);
    }

    /**
     * 执行 opencli reddit search 命令
     */
    private String executeOpenCliRedditCommand(String keyword) throws Exception {
        OpenCliCommandBuilder builder = new OpenCliCommandBuilder()
                .withModule(MODULE_REDDIT)
                .withSubCommand(SUBCOMMAND_SEARCH)
                .withArg(keyword)
                .withOption(OPTION_SORT, SORT_HOT)
                .withOption("--limit", "10")
                .withOption(OPTION_FORMAT_JSON, VALUE_JSON);

        return executeCommand(builder);
    }

    /**
     * 执行命令并返回输出
     */
    private String executeCommand(OpenCliCommandBuilder builder) throws Exception {
        ProcessBuilder processBuilder = builder.createProcessBuilder();
        log.info("执行命令: {} (OS: {})", builder.toCommandString(), builder.isWindows() ? "Windows" : "Unix");

        Process process = processBuilder.start();

        // 读取命令输出
        StringBuilder output = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
        }

        // 等待命令执行完成
        int exitCode = process.waitFor();
        if (exitCode != 0) {
            log.error("命令执行失败，退出码: {}", exitCode);
            throw new RuntimeException("opencli 命令执行失败，退出码: " + exitCode);
        }

        String result = output.toString().trim();
        log.info("获取到 {} 字节数据", result.length());
        return result;
    }

    /**
     * 解析 Twitter JSON 数据
     */
    private List<CoreMaterial> parseTwitterJson(String twitterJson, String sourceType) throws Exception {
        List<CoreMaterial> materials = new ArrayList<>();
        JsonNode rootNode = objectMapper.readTree(twitterJson);

        if (rootNode.isArray()) {
            for (JsonNode node : rootNode) {
                CoreMaterial material = convertTwitterToMaterial(node, sourceType);
                if (material != null) {
                    materials.add(material);
                }
            }
        } else {
            CoreMaterial material = convertTwitterToMaterial(rootNode, sourceType);
            if (material != null) {
                materials.add(material);
            }
        }

        return materials;
    }

    /**
     * 解析 Reddit JSON 数据
     */
    private List<CoreMaterial> parseRedditJson(String redditJson, String sourceType) throws Exception {
        List<CoreMaterial> materials = new ArrayList<>();
        JsonNode rootNode = objectMapper.readTree(redditJson);

        if (rootNode.isArray()) {
            for (JsonNode node : rootNode) {
                CoreMaterial material = convertRedditToMaterial(node, sourceType);
                if (material != null) {
                    materials.add(material);
                }
            }
        } else {
            CoreMaterial material = convertRedditToMaterial(rootNode, sourceType);
            if (material != null) {
                materials.add(material);
            }
        }

        return materials;
    }

    /**
     * 将 Twitter JSON 节点转换为 CoreMaterial
     */
    private CoreMaterial convertTwitterToMaterial(JsonNode node, String sourceType) {
        if (node == null || !node.has("id")) {
            return null;
        }

        CoreMaterial material = new CoreMaterial();
        material.setOriginalId(getTextValue(node, "id"));

        // 设置内容
        String originalText = getTextValue(node, "text");
        String text = convertNewLineToBr(originalText);
        material.setContent(text);

        // 设置标题（取内容前15字符）
        String plainText = text != null ? text.replaceAll("<[^>]+>", "") : "";
        String title = !plainText.isEmpty()
                ? (plainText.length() > 15 ? plainText.substring(0, 15) + "..." : plainText)
                : "Twitter 内容";
        material.setTitle(title);

        material.setAuthor(getTextValue(node, "author"));
        material.setOriginalUrl(getTextValue(node, "url"));

        // 设置点赞数
        if (node.has("likes") && !node.get("likes").isNull()) {
            material.setLikeCount(node.get("likes").asLong());
        }

        // 设置查看数
        if (node.has("views") && !node.get("views").isNull()) {
            String viewsStr = node.get("views").asText().replaceAll(",", "");
            try {
                material.setViewCount(Long.parseLong(viewsStr));
            } catch (NumberFormatException e) {
                material.setViewCount(0L);
            }
        }

        material.setPackageType(PACKAGE_TYPE_NORMAL);
        material.setStatus(STATUS_OFFLINE);
        material.setContentType(CONTENT_TYPE_TEXT);
        material.setSource(sourceType != null ? sourceType : SOURCE_TWITTER);

        return material;
    }

    /**
     * 将 Reddit JSON 节点转换为 CoreMaterial
     */
    private CoreMaterial convertRedditToMaterial(JsonNode node, String sourceType) {
        if (node == null) {
            return null;
        }

        CoreMaterial material = new CoreMaterial();

        // 设置标题
        String title = getTextValue(node, "title");
        material.setTitle(title != null ? title : "Reddit 内容");

        material.setAuthor(getTextValue(node, "author"));
        material.setOriginalUrl(getTextValue(node, "url"));

        // 使用 url 作为 originalId
        String url = getTextValue(node, "url");
        if (url != null) {
            String originalId = extractRedditPostId(url);
            material.setOriginalId(originalId != null ? originalId : url);

            // 获取详细内容
            String detailText = fetchRedditDetailContent(url);
            String content = detailText != null && !detailText.isEmpty() ? detailText : (title != null ? title : "");
            material.setContent(content);
        } else {
            material.setContent(title != null ? title : "");
        }

        // 设置点赞数
        if (node.has("score") && !node.get("score").isNull()) {
            material.setLikeCount(node.get("score").asLong());
        }

        // 设置评论数
        if (node.has("comments") && !node.get("comments").isNull()) {
            material.setCommentCount(node.get("comments").asLong());
        }

        material.setViewCount(0L);
        material.setPackageType(PACKAGE_TYPE_NORMAL);
        material.setStatus(STATUS_OFFLINE);
        material.setContentType(CONTENT_TYPE_TEXT);
        material.setSource(sourceType != null ? sourceType : SOURCE_REDDIT);

        return material;
    }

    /**
     * 获取 Reddit 帖子详细内容
     */
    private String fetchRedditDetailContent(String postUrl) {
        try {
            OpenCliCommandBuilder builder = new OpenCliCommandBuilder()
                    .withModule(MODULE_REDDIT)
                    .withSubCommand(SUBCOMMAND_READ)
                    .withArg(postUrl)
                    .withOption(OPTION_FORMAT_JSON, VALUE_JSON);

            String result = executeCommand(builder);
            JsonNode rootNode = objectMapper.readTree(result);

            if (rootNode.isArray() && rootNode.size() > 0) {
                JsonNode firstNode = rootNode.get(0);
                if (firstNode.has("text") && !firstNode.get("text").isNull()) {
                    String text = firstNode.get("text").asText();
                    log.info("获取到 Reddit 帖子内容: {} 字符, URL: {}", text.length(), postUrl);
                    return text;
                }
            }

            log.warn("Reddit 详情返回数据格式异常");
            return null;

        } catch (Exception e) {
            log.error("获取 Reddit 详情失败, URL: {}", postUrl, e);
            return null;
        }
    }

    /**
     * 从 Reddit URL 中提取帖子ID
     */
    private String extractRedditPostId(String url) {
        if (url == null || url.isEmpty()) {
            return null;
        }
        try {
            String[] parts = url.split("/");
            for (int i = 0; i < parts.length; i++) {
                if ("comments".equals(parts[i]) && i + 1 < parts.length) {
                    return parts[i + 1];
                }
            }
        } catch (Exception e) {
            log.warn("提取 Reddit 帖子ID失败: {}", url);
        }
        return null;
    }

    /**
     * 安全获取文本值
     */
    private String getTextValue(JsonNode node, String fieldName) {
        if (node.has(fieldName) && !node.get(fieldName).isNull()) {
            return node.get(fieldName).asText();
        }
        return null;
    }

    /**
     * 将换行符转换为 HTML <br> 标签
     */
    private String convertNewLineToBr(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }
        return text.replace("\r\n", "<br>").replace("\n", "<br>").replace("\r", "<br>");
    }

    /**
     * 导入结果内部类
     */
    private static class ImportResult {
        final int total;
        final int successCount;
        final int failCount;

        ImportResult(int total, int successCount, int failCount) {
            this.total = total;
            this.successCount = successCount;
            this.failCount = failCount;
        }
    }
}
