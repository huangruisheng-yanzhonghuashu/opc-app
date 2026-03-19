package com.opc.web.controller.system;

import com.opc.common.core.controller.BaseController;
import com.opc.common.core.domain.AjaxResult;
import com.opc.web.dto.EmailDTO;
import com.opc.web.service.EmailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 邮件发送控制器
 *
 * @author opc
 */
@Tag(name = "邮件管理", description = "邮件发送相关操作")
@RestController
@RequestMapping("/system/email")
public class EmailController extends BaseController {

    @Autowired
    private EmailService emailService;

    @Operation(summary = "发送纯文本邮件", description = "发送简单的纯文本邮件")
    @PostMapping("/sendSimple")
    public AjaxResult sendSimpleEmail(
            @Parameter(description = "发件人邮箱", required = true) @RequestParam String from,
            @Parameter(description = "收件人邮箱", required = true) @RequestParam String to,
            @Parameter(description = "邮件主题", required = true) @RequestParam String subject,
            @Parameter(description = "邮件内容", required = true) @RequestParam String content) {
        EmailDTO emailDTO = new EmailDTO(from, to, subject, content);
        boolean result = emailService.sendSimpleEmail(emailDTO);
        return result ? success("邮件发送成功") : error("邮件发送失败");
    }

    @Operation(summary = "发送纯文本邮件（带抄送）", description = "发送带抄送和密送的纯文本邮件")
    @PostMapping("/sendSimpleWithCc")
    public AjaxResult sendSimpleEmailWithCc(
            @Parameter(description = "发件人邮箱", required = true) @RequestParam String from,
            @Parameter(description = "收件人邮箱", required = true) @RequestParam String to,
            @Parameter(description = "抄送人邮箱") @RequestParam(required = false) String cc,
            @Parameter(description = "密送人邮箱") @RequestParam(required = false) String bcc,
            @Parameter(description = "邮件主题", required = true) @RequestParam String subject,
            @Parameter(description = "邮件内容", required = true) @RequestParam String content) {
        EmailDTO emailDTO = new EmailDTO(from, to, subject, content);
        emailDTO.setCc(cc);
        emailDTO.setBcc(bcc);
        boolean result = emailService.sendSimpleEmail(emailDTO);
        return result ? success("邮件发送成功") : error("邮件发送失败");
    }

    @Operation(summary = "发送HTML邮件", description = "发送HTML格式的邮件")
    @PostMapping("/sendHtml")
    public AjaxResult sendHtmlEmail(
            @Parameter(description = "发件人邮箱", required = true) @RequestParam String from,
            @Parameter(description = "收件人邮箱", required = true) @RequestParam String to,
            @Parameter(description = "邮件主题", required = true) @RequestParam String subject,
            @Parameter(description = "HTML内容", required = true) @RequestParam String htmlContent) {
        EmailDTO emailDTO = new EmailDTO(from, to, subject, htmlContent, true);
        boolean result = emailService.sendHtmlEmail(emailDTO);
        return result ? success("邮件发送成功") : error("邮件发送失败");
    }

    @Operation(summary = "发送带附件的邮件", description = "发送带附件的邮件")
    @PostMapping("/sendWithAttachment")
    public AjaxResult sendEmailWithAttachment(
            @Parameter(description = "发件人邮箱", required = true) @RequestParam String from,
            @Parameter(description = "收件人邮箱", required = true) @RequestParam String to,
            @Parameter(description = "邮件主题", required = true) @RequestParam String subject,
            @Parameter(description = "邮件内容", required = true) @RequestParam String content,
            @Parameter(description = "附件文件路径列表", required = true) @RequestParam List<String> filePaths) {
        EmailDTO emailDTO = new EmailDTO(from, to, subject, content);
        List<File> files = new ArrayList<>();
        for (String filePath : filePaths) {
            File file = new File(filePath);
            if (file.exists()) {
                files.add(file);
            }
        }
        emailDTO.setAttachmentFiles(files);
        boolean result = emailService.sendSimpleEmail(emailDTO);
        return result ? success("邮件发送成功") : error("邮件发送失败");
    }

    @Operation(summary = "使用自定义SMTP发送邮件", description = "使用自定义SMTP服务器配置发送邮件")
    @PostMapping("/sendWithCustomSmtp")
    public AjaxResult sendEmailWithCustomSmtp(
            @Parameter(description = "SMTP服务器地址", required = true) @RequestParam String smtpHost,
            @Parameter(description = "SMTP服务器端口", required = true) @RequestParam int smtpPort,
            @Parameter(description = "SMTP用户名", required = true) @RequestParam String username,
            @Parameter(description = "SMTP密码", required = true) @RequestParam String password,
            @Parameter(description = "发件人邮箱", required = true) @RequestParam String from,
            @Parameter(description = "收件人邮箱", required = true) @RequestParam String to,
            @Parameter(description = "邮件主题", required = true) @RequestParam String subject,
            @Parameter(description = "邮件内容", required = true) @RequestParam String content,
            @Parameter(description = "是否为HTML邮件") @RequestParam(defaultValue = "false") boolean isHtml,
            @Parameter(description = "是否使用SSL") @RequestParam(defaultValue = "false") boolean useSSL,
            @Parameter(description = "是否使用TLS") @RequestParam(defaultValue = "false") boolean useTLS) {
        EmailDTO emailDTO = new EmailDTO(from, to, subject, content, isHtml);
        EmailDTO.SmtpConfig smtpConfig = new EmailDTO.SmtpConfig(smtpHost, smtpPort, username, password, useSSL, useTLS);
        emailDTO.setSmtpConfig(smtpConfig);
        boolean result = emailService.sendEmailWithCustomSmtp(emailDTO);
        return result ? success("邮件发送成功") : error("邮件发送失败");
    }

    @Operation(summary = "批量发送邮件", description = "批量发送邮件给多个收件人")
    @PostMapping("/sendBatch")
    public AjaxResult sendBatchEmail(
            @Parameter(description = "发件人邮箱", required = true) @RequestParam String from,
            @Parameter(description = "收件人邮箱列表", required = true) @RequestParam List<String> toList,
            @Parameter(description = "邮件主题", required = true) @RequestParam String subject,
            @Parameter(description = "邮件内容", required = true) @RequestParam String content,
            @Parameter(description = "是否为HTML邮件") @RequestParam(defaultValue = "false") boolean isHtml) {
        int successCount = emailService.sendBatchEmail(from, toList, subject, content, isHtml);
        Map<String, Object> result = new HashMap<>();
        result.put("total", toList.size());
        result.put("success", successCount);
        result.put("failed", toList.size() - successCount);
        return success(result);
    }
}
