package com.opc.web.service.reddit;

import com.fasterxml.jackson.databind.JsonNode;
import com.opc.common.core.domain.AjaxResult;
import com.opc.core.domain.CoreMaterial;
import com.opc.web.service.common.opecli.OpenCliCommandBuilder;
import com.opc.web.service.common.AbstractCollectFetchService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.opc.web.service.common.opecli.OpenCliConstants.*;

/**
 * Reddit 数据获取服务
 * <p>
 * 封装 Reddit 的数据获取和导入逻辑
 * </p>
 *
 * @author opc
 * @since 3.9.1
 */
@Service
public class RedditFetchService extends AbstractCollectFetchService {

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

            return buildImportResult(keyword, sourceType, result);

        } catch (Exception e) {
            log.error("Reddit 搜索导入失败", e);
            return AjaxResult.error("导入失败: " + e.getMessage());
        }
    }

    /**
     * 执行 opencli reddit search 命令
     */
    private String executeOpenCliRedditCommand(String keyword) throws Exception {
        OpenCliCommandBuilder builder = new OpenCliCommandBuilder(openCliProperties)
                .withModule(MODULE_REDDIT)
                .withSubCommand(SUBCOMMAND_SEARCH)
                .withArg(keyword)
                .withOption(OPTION_SORT, SORT_HOT)
                .withOption("--limit", "10")
                .withOption(OPTION_FORMAT_JSON, VALUE_JSON);

        return executeCommand(builder);
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
            material.setContent(convertNewLineToBr(content));
        } else {
            material.setContent(convertNewLineToBr(title != null ? title : ""));
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
            OpenCliCommandBuilder builder = new OpenCliCommandBuilder(openCliProperties)
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
}
