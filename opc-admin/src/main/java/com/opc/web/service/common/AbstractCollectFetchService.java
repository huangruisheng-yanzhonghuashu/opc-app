package com.opc.web.service.common;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opc.common.core.domain.AjaxResult;
import com.opc.core.domain.CoreMaterial;
import com.opc.core.service.ICoreMaterialService;
import com.opc.web.config.opencli.OpenCliProperties;
import com.opc.web.service.common.opecli.OpenCliCommandBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * 采集数据获取服务抽象基类
 * <p>
 * 封装 Twitter、Reddit 等平台的通用数据获取和导入逻辑
 * </p>
 *
 * @author opc
 * @since 3.9.1
 */
public abstract class AbstractCollectFetchService {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    protected ICoreMaterialService materialService;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected OpenCliProperties openCliProperties;

    /**
     * 执行命令并返回输出（自动应用配置中的代理设置）
     *
     * @param builder 命令构建器
     * @return 命令输出结果
     * @throws Exception 执行异常
     */
    protected String executeCommand(OpenCliCommandBuilder builder) throws Exception {
        ProcessBuilder processBuilder = builder.createProcessBuilder(openCliProperties);

        // 获取实际应用的代理设置（builder 中的优先级高于配置）
        String effectiveProxy = builder.getProxyUrl();
        if (effectiveProxy == null || effectiveProxy.isEmpty()) {
            if (openCliProperties != null && openCliProperties.getProxy() != null && openCliProperties.getProxy().isEnabled()) {
                effectiveProxy = openCliProperties.getProxyUrl();
            }
        }

        // 打印实际设置的环境变量（用于调试）
        String proxyDebug = (effectiveProxy != null && !effectiveProxy.isEmpty()) ? effectiveProxy : "未启用";
        if (effectiveProxy != null && !effectiveProxy.isEmpty()) {
            Map<String, String> env = processBuilder.environment();
            String httpProxy = env.get("HTTP_PROXY");
            String httpsProxy = env.get("HTTPS_PROXY");
            log.debug("环境变量 HTTP_PROXY={}, HTTPS_PROXY={}", httpProxy, httpsProxy);
        }

        log.info("执行命令: {} (OS: {}, 代理: {})",
                builder.toFullCommandString(),
                builder.isWindows() ? "Windows" : "Unix",
                proxyDebug);

        // 调试：打印完整的命令列表
        if (log.isDebugEnabled()) {
            log.debug("ProcessBuilder 命令: {}", processBuilder.command());
        }

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
     * 导入素材列表（基础实现）
     *
     * @param materials 素材列表
     * @return 导入结果
     */
    protected ImportResult importMaterials(List<CoreMaterial> materials) {
        int successCount = 0;
        int failCount = 0;
        int skipCount = 0;

        for (CoreMaterial material : materials) {
            String originalId = material.getOriginalId();
            try {
                // 根据 originId 判断数据是否已存在
                if (originalId != null && !originalId.isEmpty()) {
                    CoreMaterial existingMaterial = materialService.selectMaterialByOriginalId(originalId);
                    if (existingMaterial != null) {
                        log.info("素材已存在，跳过导入, originalId: {}", originalId);
                        skipCount++;
                        continue;
                    }
                }

                // 子类可以重写此方法添加更多处理逻辑
                beforeInsertMaterial(material);

                materialService.insertMaterial(material);
                successCount++;
            } catch (Exception e) {
                log.error("导入素材失败, originalId: {}", originalId, e);
                failCount++;
            }
        }

        log.info("导入完成: 总计={}, 成功={}, 跳过={}, 失败={}", materials.size(), successCount, skipCount, failCount);
        return new ImportResult(materials.size(), successCount, failCount, skipCount);
    }

    /**
     * 插入素材前的钩子方法，子类可以重写以添加额外处理
     *
     * @param material 待插入的素材
     */
    protected void beforeInsertMaterial(CoreMaterial material) {
        // 子类可重写
    }

    /**
     * 构建标准导入结果
     *
     * @param keyword    关键词
     * @param sourceType 来源类型
     * @param result     导入结果
     * @return AjaxResult
     */
    protected AjaxResult buildImportResult(String keyword, String sourceType, ImportResult result) {
        AjaxResult ajaxResult = AjaxResult.success("导入完成");
        ajaxResult.put("keyword", keyword);
        ajaxResult.put("sourceType", sourceType);
        ajaxResult.put("total", result.total);
        ajaxResult.put("successCount", result.successCount);
        ajaxResult.put("failCount", result.failCount);
        return ajaxResult;
    }

    /**
     * 安全获取 JSON 文本值
     *
     * @param node       JSON节点
     * @param fieldName  字段名
     * @return 文本值，不存在返回 null
     */
    protected String getTextValue(JsonNode node, String fieldName) {
        if (node.has(fieldName) && !node.get(fieldName).isNull()) {
            return node.get(fieldName).asText();
        }
        return null;
    }

    /**
     * 将换行符转换为 HTML &lt;br&gt; 标签
     *
     * @param text 原文本
     * @return 转换后的文本
     */
    protected String convertNewLineToBr(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }
        return text.replace("\r\n", "<br>").replace("\n", "<br>").replace("\r", "<br>");
    }

    /**
     * 导入结果内部类
     */
    protected static class ImportResult {
        public final int total;
        public final int successCount;
        public final int failCount;
        public final int skipCount;

        public ImportResult(int total, int successCount, int failCount, int skipCount) {
            this.total = total;
            this.successCount = successCount;
            this.failCount = failCount;
            this.skipCount = skipCount;
        }
    }
}
