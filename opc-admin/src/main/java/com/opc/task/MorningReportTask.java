package com.opc.task;

import com.opc.ai.service.AiChatService;
import com.opc.core.domain.CoreMaterial;
import com.opc.core.service.ICoreMaterialService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * 晨报定时任务
 * 每天早上8点调用AI生成晨报并添加到素材库
 *
 * @author opc
 */
@Component
public class MorningReportTask {

    private static final Logger log = LoggerFactory.getLogger(MorningReportTask.class);

    @Autowired
    private AiChatService aiChatService;

    @Autowired
    private ICoreMaterialService materialService;

    /**
     * 生成晨报的提示词模板
     */
    private static final String MORNING_REPORT_PROMPT = """
        请为我生成一份今日晨报，包含以下内容：
        
        1. 日期：今天的日期
        2. 热点新闻摘要（3-5条国内外重要新闻，每条100字左右）
        3. 科技动态（2-3条最新科技资讯）
        4. 财经简报（股市、汇率等简要信息）
        5. 今日提示（天气提醒、节日等）
        
        要求：
        - 内容简洁明了，适合早晨快速阅读
        - 语言通俗易懂
        - 用Markdown格式输出
        - 总字数控制在800-1200字
        """;

    /**
     * 每隔一分钟生成晨报（测试用）
     * cron表达式：0 0/1 * * * ? （每隔1分钟执行一次）
     */
    //@Scheduled(cron = "0 0/2 * * * ?")
    public void generateMorningReport() {
        try {
            log.info("开始生成晨报...");

            // 检查AI服务是否配置
            if (!isAiServiceAvailable()) {
                log.warn("AI服务未配置，跳过晨报生成。请配置 spring.ai.api-key");
                return;
            }

            // 生成日期
            LocalDate today = LocalDate.now();
            String dateStr = today.format(DateTimeFormatter.ofPattern("yyyy年MM月dd日"));
            String title = dateStr + "晨报";

            // 调用AI生成晨报内容
            String content = aiChatService.chatWithSystemPrompt(
                    "你是一位专业的新闻编辑，擅长撰写简洁明了的晨报。生成的格式是HTML格式",
                    MORNING_REPORT_PROMPT
            );

            if (content == null || content.trim().isEmpty()) {
                log.error("AI生成的晨报内容为空");
                return;
            }

            // 生成摘要（取前100字）
            String summary = generateSummary(content);

            // 创建素材对象
            CoreMaterial material = new CoreMaterial();
            material.setTitle(title);
            material.setSummary(summary);
            material.setContent(content);
            material.setAuthor("AI晨报助手");
            material.setSource("AI生成");
            material.setPackageType(0); // 0=晨报
            material.setStatus("0"); // 0=上线
            material.setContentType("text");
            material.setIsTop("0");
            material.setPublishTime(Instant.now());
            material.setViewCount(0L);
            material.setLikeCount(0L);
            material.setReplyCount(0L);
            material.setShareCount(0L);
            material.setCommentCount(0L);

            // 插入素材库
            int result = materialService.insertMaterial(material);

            if (result > 0) {
                log.info("晨报生成成功，素材ID：{}，标题：{}", material.getId(), title);
            } else {
                log.error("晨报生成失败，插入数据库返回0");
            }

        } catch (Exception e) {
            log.error("生成晨报时发生异常", e);
        }
    }

    /**
     * 检查AI服务是否可用
     */
    private boolean isAiServiceAvailable() {
        try {
            // 尝试调用一个简单的chat来验证服务是否配置
            aiChatService.chat("test");
            return true;
        } catch (IllegalStateException e) {
            return false;
        } catch (Exception e) {
            // 其他异常（如网络问题）也认为服务可用，只是可能暂时出错
            return true;
        }
    }

    /**
     * 生成摘要
     */
    private String generateSummary(String content) {
        if (content == null || content.isEmpty()) {
            return "";
        }
        // 去除Markdown标记，取纯文本前100字
        String plainText = content
                .replaceAll("#+ ", "")
                .replaceAll("\\*\\*", "")
                .replaceAll("\\*", "")
                .replaceAll("`", "")
                .replaceAll("\\[([^\\]]+)\\]\\([^\\)]+\\)", "$1")
                .replaceAll("\n+", " ");

        if (plainText.length() <= 100) {
            return plainText;
        }
        return plainText.substring(0, 100) + "...";
    }
}
