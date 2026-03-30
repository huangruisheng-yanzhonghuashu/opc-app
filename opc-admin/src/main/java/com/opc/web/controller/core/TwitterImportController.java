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
 * Twitter 数据导入控制器
 * <p>
 * 通过调用 opencli 命令行工具，实现 Twitter 数据的搜索和导入功能。
 * 支持将 Twitter 推文数据解析并保存到 CoreMaterial 素材表中。
 * </p>
 *
 * <h3>主要功能：</h3>
 * <ul>
 *   <li>Twitter 推文搜索与导入 - 执行 opencli twitter search 命令</li>
 *   <li>自动翻译 - 使用 TranslateUtils 将推文内容翻译为中文</li>
 *   <li>素材表管理 - 将数据保存到 CoreMaterial 表</li>
 * </ul>
 *
 * <h3>API 端点：</h3>
 * <ul>
 *   <li>POST /core/twitter/search?keyword=xxx - 搜索并导入推文</li>
 * </ul>
 *
 * @author opc
 * @since 3.9.1
 * @see com.opc.core.domain.CoreMaterial
 * @see com.opc.core.service.ICoreMaterialService
 */
@Tag(name = "Twitter数据导入", description = "Twitter推文搜索和导入")
@RestController
@RequestMapping("/core/twitter")
public class TwitterImportController extends BaseController {

    private static final Logger log = LoggerFactory.getLogger(TwitterImportController.class);

    @Autowired
    private ICoreMaterialService materialService;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 搜索 Twitter 并导入到素材表
     *
     * @param keyword 搜索关键词
     * @return 导入结果
     */
    @Operation(summary = "搜索并导入推文", description = "根据关键词搜索Twitter推文并导入到素材表")
    @Parameter(name = "keyword", description = "搜索关键词", required = true)
    @PreAuthorize("@ss.hasPermi('core:material:add')")
    @Log(title = "Twitter搜索导入", businessType = BusinessType.IMPORT)
    @PostMapping("/search")
    public AjaxResult searchAndImport(@RequestParam String keyword) {
        try {
            // 查询采集配置获取 source_type
            //String sourceType = getSourceTypeByKeyword(keyword);
            String sourceType = SOURCE_TWITTER;

            // 执行 opencli 命令获取 Twitter 数据
            String jsonResult = executeOpenCliCommand(keyword);
            if (jsonResult == null || jsonResult.isEmpty()) {
                return AjaxResult.error("未获取到 Twitter 数据");
            }

            // 解析并保存数据
            List<CoreMaterial> materials = parseTwitterJson(jsonResult, sourceType);
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

            AjaxResult result = AjaxResult.success("导入完成");
            result.put("keyword", keyword);
            result.put("sourceType", sourceType);
            result.put("total", materials.size());
            result.put("successCount", successCount);
            result.put("failCount", failCount);
            return result;
        } catch (Exception e) {
            log.error("Twitter 搜索导入失败", e);
            return AjaxResult.error("导入失败: " + e.getMessage());
        }
    }

    /**
     * 执行 opencli 命令获取 Twitter 数据
     *
     * @param keyword 搜索关键词
     * @return JSON 结果字符串
     */
    private String executeOpenCliCommand(String keyword) throws Exception {
        // 使用命令构建器构建命令
        OpenCliCommandBuilder builder = new OpenCliCommandBuilder()
                .withModule(MODULE_TWITTER)
                .withSubCommand(SUBCOMMAND_SEARCH)
                .withArg(keyword)
                .withOption("--limit", "10")
                .withOption(OPTION_FORMAT_JSON, VALUE_JSON);

        ProcessBuilder processBuilder = builder.createProcessBuilder();

        log.info("执行命令: {} (OS: {})", builder.toCommandString(), builder.isWindows() ? "Windows" : "Unix");
        Process process = processBuilder.start();

        // 读取命令输出（保留换行符）
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
     *
     * @param twitterJson Twitter JSON 字符串
     * @param sourceType  来源类型
     * @return CoreMaterial 列表
     */
    private List<CoreMaterial> parseTwitterJson(String twitterJson, String sourceType) throws Exception {
        List<CoreMaterial> materials = new ArrayList<>();

        JsonNode rootNode = objectMapper.readTree(twitterJson);

        // 处理 JSON 数组
        if (rootNode.isArray()) {
            for (JsonNode node : rootNode) {
                CoreMaterial material = convertToMaterial(node, sourceType);
                if (material != null) {
                    materials.add(material);
                }
            }
        } else {
            // 处理单个对象
            CoreMaterial material = convertToMaterial(rootNode, sourceType);
            if (material != null) {
                materials.add(material);
            }
        }

        return materials;
    }

    /**
     * 将单个 Twitter JSON 节点转换为 CoreMaterial
     *
     * @param node       JSON 节点
     * @param sourceType 来源类型
     * @return CoreMaterial 对象
     */
    private CoreMaterial convertToMaterial(JsonNode node, String sourceType) {
        if (node == null || !node.has("id")) {
            return null;
        }

        CoreMaterial material = new CoreMaterial();

        // 设置原ID
        material.setOriginalId(getTextValue(node, "id"));

        // 设置内容（text字段）- 将换行符转为HTML换行并翻译为中文
        String originalText = getTextValue(node, "text");
        String text = convertNewLineToBr(originalText);
        // 自动检测语言并翻译为中文
        //String translatedText = TranslateUtils.toChinese(originalText);
        String translatedText = text;
        material.setContent(translatedText != null ? convertNewLineToBr(translatedText) : text);

        // 设置标题（取翻译后text前15字符，去掉HTML标签）
        String plainText = translatedText != null ? translatedText.replaceAll("<[^>]+>", "") : "";
        String title = !plainText.isEmpty()
                ? (plainText.length() > 15 ? plainText.substring(0, 15) + "..." : plainText)
                : "Twitter 内容";
        material.setTitle(title);

        // 设置作者
        material.setAuthor(getTextValue(node, "author"));

        // 设置原链接
        material.setOriginalUrl(getTextValue(node, "url"));

        // 设置点赞数
        if (node.has("likes") && !node.get("likes").isNull()) {
            material.setLikeCount(node.get("likes").asLong());
        }

        // 设置查看数（views字段可能是字符串数字）
        if (node.has("views") && !node.get("views").isNull()) {
            String viewsStr = node.get("views").asText().replaceAll(",", "");
            try {
                material.setViewCount(Long.parseLong(viewsStr));
            } catch (NumberFormatException e) {
                material.setViewCount(0L);
            }
        }

        // 设置套餐类型为普通会员 (1)
        material.setPackageType(PACKAGE_TYPE_NORMAL);

        // 设置状态为下线 (1)
        material.setStatus(STATUS_OFFLINE);

        // 设置内容类型为文本
        material.setContentType(CONTENT_TYPE_TEXT);

        // 设置来源（从采集配置中获取）
        material.setSource(sourceType != null ? sourceType : SOURCE_TWITTER);

        return material;
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
        // 将 \r\n 或 \n 替换为 <br>
        return text.replace("\r\n", "<br>").replace("\n", "<br>").replace("\r", "<br>");
    }

}
