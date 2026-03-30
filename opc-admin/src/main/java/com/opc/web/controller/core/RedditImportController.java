package com.opc.web.controller.core;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opc.common.annotation.Log;
import com.opc.common.core.controller.BaseController;
import com.opc.common.core.domain.AjaxResult;
import com.opc.common.enums.BusinessType;
import com.opc.common.utils.translate.TranslateUtils;
import com.opc.core.domain.CoreMaterial;
import com.opc.core.service.ICoreMaterialService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static com.opc.web.controller.core.OpenCliConstants.*;

/**
 * Reddit 数据导入控制器
 * <p>
 * 通过调用 opencli 命令行工具，实现 Reddit 帖子的搜索和导入功能。
 * 支持将 Reddit 帖子数据解析并保存到 CoreMaterial 素材表中，同时获取详细内容。
 * </p>
 *
 * <h3>主要功能：</h3>
 * <ul>
 *   <li>Reddit 帖子搜索与导入 - 执行 opencli reddit search 命令</li>
 *   <li>帖子详情获取 - 执行 opencli reddit read 命令获取正文内容</li>
 *   <li>自动翻译 - 使用 TranslateUtils 将帖子内容翻译为中文</li>
 *   <li>素材表管理 - 将数据保存到 CoreMaterial 表</li>
 * </ul>
 *
 * <h3>API 端点：</h3>
 * <ul>
 *   <li>POST /core/reddit/search?keyword=xxx - 搜索并导入 Reddit 帖子</li>
 * </ul>
 *
 * <h3>数据处理流程：</h3>
 * <ol>
 *   <li>执行 reddit search 获取帖子列表</li>
 *   <li>对每个帖子执行 reddit read 获取详细内容</li>
 *   <li>翻译内容并保存到 CoreMaterial 表</li>
 * </ol>
 *
 * @author opc
 * @since 3.9.1
 * @see com.opc.core.domain.CoreMaterial
 * @see com.opc.core.service.ICoreMaterialService
 */
@Tag(name = "Reddit数据导入", description = "Reddit帖子搜索和导入")
@RestController
@RequestMapping("/core/reddit")
public class RedditImportController extends BaseController {

    private static final Logger log = LoggerFactory.getLogger(RedditImportController.class);

    @Autowired
    private ICoreMaterialService materialService;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 搜索 Reddit 并导入到素材表
     *
     * @param keyword 搜索关键词
     * @return 导入结果
     */
    @Operation(summary = "搜索并导入Reddit帖子", description = "根据关键词搜索Reddit帖子并导入到素材表")
    @Parameter(name = "keyword", description = "搜索关键词", required = true)
    @PreAuthorize("@ss.hasPermi('core:material:add')")
    @Log(title = "Reddit搜索导入", businessType = BusinessType.IMPORT)
    @PostMapping("/search")
    public AjaxResult searchRedditAndImport(@RequestParam String keyword) {
        try {
            String sourceType = SOURCE_REDDIT;

            // 执行 opencli reddit search 命令获取数据
            String jsonResult = executeOpenCliRedditCommand(keyword);
            if (jsonResult == null || jsonResult.isEmpty()) {
                return AjaxResult.error("未获取到 Reddit 数据");
            }

            // 解析并保存数据
            List<CoreMaterial> materials = parseRedditJson(jsonResult, sourceType);
            int successCount = 0;
            int failCount = 0;

            for (CoreMaterial material : materials) {
                String originalId = material.getOriginalId();
                try {
                    materialService.insertMaterial(material);
                    successCount++;

                } catch (Exception e) {
                    log.error("导入 Reddit 素材失败, originalId: {}", originalId, e);
                    failCount++;
                }
            }

            AjaxResult result = AjaxResult.success("导入完成");
            result.put("keyword", keyword);
            result.put("sourceType", sourceType);
            result.put("total", materials.size());
            result.put("successCount", successCount);
            result.put("failCount", failCount);
            return result;
        } catch (Exception e) {
            log.error("Reddit 搜索导入失败", e);
            return AjaxResult.error("导入失败: " + e.getMessage());
        }
    }

    /**
     * 执行 opencli reddit search 命令获取数据
     *
     * @param keyword 搜索关键词
     * @return JSON 结果字符串
     */
    private String executeOpenCliRedditCommand(String keyword) throws Exception {
        // 使用命令构建器构建命令
        OpenCliCommandBuilder builder = new OpenCliCommandBuilder()
                .withModule(MODULE_REDDIT)
                .withSubCommand(SUBCOMMAND_SEARCH)
                .withArg(keyword)
                .withOption(OPTION_SORT, SORT_HOT)
                .withOption("--limit", "10")
                .withOption(OPTION_FORMAT_JSON, VALUE_JSON);

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
     * 解析 Reddit JSON 数据
     *
     * @param redditJson Reddit JSON 字符串
     * @param sourceType 来源类型
     * @return CoreMaterial 列表
     */
    private List<CoreMaterial> parseRedditJson(String redditJson, String sourceType) throws Exception {
        List<CoreMaterial> materials = new ArrayList<>();

        JsonNode rootNode = objectMapper.readTree(redditJson);

        // 处理 JSON 数组
        if (rootNode.isArray()) {
            for (JsonNode node : rootNode) {
                CoreMaterial material = convertRedditToMaterial(node, sourceType);
                if (material != null) {
                    materials.add(material);
                }
            }
        } else {
            // 处理单个对象
            CoreMaterial material = convertRedditToMaterial(rootNode, sourceType);
            if (material != null) {
                materials.add(material);
            }
        }

        return materials;
    }

    /**
     * 将单个 Reddit JSON 节点转换为 CoreMaterial
     *
     * @param node       JSON 节点
     * @param sourceType 来源类型
     * @return CoreMaterial 对象
     */
    private CoreMaterial convertRedditToMaterial(JsonNode node, String sourceType) {
        if (node == null) {
            return null;
        }

        CoreMaterial material = new CoreMaterial();

        // 设置标题
        String title = getTextValue(node, "title");
        material.setTitle(title != null ? title : "Reddit 内容");

        // 设置作者
        material.setAuthor(getTextValue(node, "author"));

        // 设置原链接（url 字段）
        material.setOriginalUrl(getTextValue(node, "url"));

        // 使用 url 作为 originalId（提取其中的 post ID）
        String url = getTextValue(node, "url");

        // 获取详细内容 - 执行 opencli reddit read 命令
        String content;
        if (url != null) {
            // 从 URL 中提取帖子ID
            String originalId = extractRedditPostId(url);
            material.setOriginalId(originalId != null ? originalId : url);

            // 执行 opencli reddit read 获取详细内容
            String detailText = fetchRedditDetailContent(url);
            if (detailText != null && !detailText.isEmpty()) {
                content = detailText;
            } else {
                content = title != null ? title : "";
            }
        } else {
            content = title != null ? title : "";
        }

        // 翻译为中文
//        String translatedContent = TranslateUtils.toChinese(content);
        String translatedContent = content;
        material.setContent(translatedContent != null ? translatedContent : content);

        // 设置点赞数（score 字段）
        if (node.has("score") && !node.get("score").isNull()) {
            material.setLikeCount(node.get("score").asLong());
        }

        // 设置评论数（comments 字段）
        if (node.has("comments") && !node.get("comments").isNull()) {
            material.setCommentCount(node.get("comments").asLong());
        }

        // 设置查看数为 0（Reddit API 通常不返回查看数）
        material.setViewCount(0L);

        // 设置套餐类型为普通会员 (1)
        material.setPackageType(PACKAGE_TYPE_NORMAL);

        // 设置状态为下线 (1)
        material.setStatus(STATUS_OFFLINE);

        // 设置内容类型为文本（Reddit 帖子通常是链接或文本）
        material.setContentType(CONTENT_TYPE_TEXT);

        // 设置来源
        material.setSource(sourceType != null ? sourceType : SOURCE_REDDIT);

        return material;
    }

    /**
     * 执行 opencli reddit read 命令获取帖子详细内容
     *
     * @param postUrl Reddit 帖子 URL
     * @return 帖子正文内容（text 字段）
     */
    private String fetchRedditDetailContent(String postUrl) {
        try {
            // 使用命令构建器构建命令
            ProcessBuilder processBuilder = new OpenCliCommandBuilder()
                    .withModule(MODULE_REDDIT)
                    .withSubCommand(SUBCOMMAND_READ)
                    .withArg(postUrl)
                    .withOption(OPTION_FORMAT_JSON, VALUE_JSON)
                    .createProcessBuilder();

            OpenCliCommandBuilder builder = new OpenCliCommandBuilder()
                    .withModule(MODULE_REDDIT)
                    .withSubCommand(SUBCOMMAND_READ)
                    .withArg(postUrl)
                    .withOption(OPTION_FORMAT_JSON, VALUE_JSON);
            log.info("执行命令: {}", builder.toCommandString());
            Process process = builder.createProcessBuilder().start();

            // 读取命令输出
            StringBuilder output = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                }
            }

            int exitCode = process.waitFor();
            String result = output.toString().trim();

            if (exitCode != 0) {
                log.warn("获取 Reddit 详情失败，退出码: {}, URL: {}", exitCode, postUrl);
                return null;
            }

            // 解析 JSON 获取第一条记录的 text 字段
            JsonNode rootNode = objectMapper.readTree(result);
            if (rootNode.isArray() && rootNode.size() > 0) {
                JsonNode firstNode = rootNode.get(0);
                if (firstNode.has("text") && !firstNode.get("text").isNull()) {
                    String text = firstNode.get("text").asText();
                    log.info("获取到 Reddit 帖子内容: {} 字符, URL: {}", text.length(), postUrl);
                    return text;
                }
            }

            log.warn("Reddit 详情返回数据格式异常: {}", result);
            return null;

        } catch (Exception e) {
            log.error("获取 Reddit 详情失败, URL: {}", postUrl, e);
            return null;
        }
    }

    /**
     * 从 Reddit URL 中提取帖子ID
     *
     * @param url Reddit 帖子 URL
     * @return 帖子ID
     */
    private String extractRedditPostId(String url) {
        if (url == null || url.isEmpty()) {
            return null;
        }
        try {
            // URL 格式: https://www.reddit.com/r/subreddit/comments/xxxxx/title/
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
}
