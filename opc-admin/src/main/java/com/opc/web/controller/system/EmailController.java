package com.opc.web.controller.system;

import com.opc.common.core.controller.BaseController;
import com.opc.common.core.domain.AjaxResult;
import com.opc.web.dto.EmailDTO;
import com.opc.web.service.EmailService;
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
@RestController
@RequestMapping("/system/email")
public class EmailController extends BaseController {

    @Autowired
    private EmailService emailService;

    /**
     * 发送纯文本邮件
     *
     * @param from 发件人邮箱
     * @param to 收件人邮箱
     * @param subject 邮件主题
     * @param content 邮件内容
     * @return 发送结果
     */
    @PostMapping("/sendSimple")
    public AjaxResult sendSimpleEmail(@RequestParam String from,
                                      @RequestParam String to,
                                      @RequestParam String subject,
                                      @RequestParam String content) {
        EmailDTO emailDTO = new EmailDTO(from, to, subject, content);
        boolean result = emailService.sendSimpleEmail(emailDTO);
        return result ? success("邮件发送成功") : error("邮件发送失败");
    }

    /**
     * 发送纯文本邮件（带抄送）
     *
     * @param from 发件人邮箱
     * @param to 收件人邮箱
     * @param cc 抄送人邮箱（可选）
     * @param bcc 密送人邮箱（可选）
     * @param subject 邮件主题
     * @param content 邮件内容
     * @return 发送结果
     */
    @PostMapping("/sendSimpleWithCc")
    public AjaxResult sendSimpleEmailWithCc(@RequestParam String from,
                                            @RequestParam String to,
                                            @RequestParam(required = false) String cc,
                                            @RequestParam(required = false) String bcc,
                                            @RequestParam String subject,
                                            @RequestParam String content) {
        EmailDTO emailDTO = new EmailDTO(from, to, subject, content);
        emailDTO.setCc(cc);
        emailDTO.setBcc(bcc);
        boolean result = emailService.sendSimpleEmail(emailDTO);
        return result ? success("邮件发送成功") : error("邮件发送失败");
    }

    /**
     * 发送HTML邮件
     *
     * @param from 发件人邮箱
     * @param to 收件人邮箱
     * @param subject 邮件主题
     * @param htmlContent HTML内容
     * @return 发送结果
     */
    @PostMapping("/sendHtml")
    public AjaxResult sendHtmlEmail(@RequestParam String from,
                                     @RequestParam String to,
                                     @RequestParam String subject,
                                     @RequestParam String htmlContent) {
        EmailDTO emailDTO = new EmailDTO(from, to, subject, htmlContent, true);
        boolean result = emailService.sendHtmlEmail(emailDTO);
        return result ? success("邮件发送成功") : error("邮件发送失败");
    }

    /**
     * 发送带附件的邮件
     *
     * @param from 发件人邮箱
     * @param to 收件人邮箱
     * @param subject 邮件主题
     * @param content 邮件内容
     * @param filePaths 附件文件路径列表
     * @return 发送结果
     */
    @PostMapping("/sendWithAttachment")
    public AjaxResult sendEmailWithAttachment(@RequestParam String from,
                                              @RequestParam String to,
                                              @RequestParam String subject,
                                              @RequestParam String content,
                                              @RequestParam List<String> filePaths) {
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

    /**
     * 使用自定义SMTP发送邮件
     *
     * @param smtpHost SMTP服务器地址
     * @param smtpPort SMTP服务器端口
     * @param username 用户名
     * @param password 密码
     * @param from 发件人邮箱
     * @param to 收件人邮箱
     * @param subject 邮件主题
     * @param content 邮件内容
     * @param isHtml 是否为HTML邮件
     * @param useSSL 是否使用SSL
     * @param useTLS 是否使用TLS
     * @return 发送结果
     */
    @PostMapping("/sendWithCustomSmtp")
    public AjaxResult sendEmailWithCustomSmtp(@RequestParam String smtpHost,
                                                @RequestParam int smtpPort,
                                                @RequestParam String username,
                                                @RequestParam String password,
                                                @RequestParam String from,
                                                @RequestParam String to,
                                                @RequestParam String subject,
                                                @RequestParam String content,
                                                @RequestParam(defaultValue = "false") boolean isHtml,
                                                @RequestParam(defaultValue = "false") boolean useSSL,
                                                @RequestParam(defaultValue = "false") boolean useTLS) {
        EmailDTO emailDTO = new EmailDTO(from, to, subject, content, isHtml);
        EmailDTO.SmtpConfig smtpConfig = new EmailDTO.SmtpConfig(smtpHost, smtpPort, username, password, useSSL, useTLS);
        emailDTO.setSmtpConfig(smtpConfig);
        boolean result = emailService.sendEmailWithCustomSmtp(emailDTO);
        return result ? success("邮件发送成功") : error("邮件发送失败");
    }

    /**
     * 批量发送邮件
     *
     * @param from 发件人邮箱
     * @param toList 收件人邮箱列表
     * @param subject 邮件主题
     * @param content 邮件内容
     * @param isHtml 是否为HTML邮件
     * @return 发送结果
     */
    @PostMapping("/sendBatch")
    public AjaxResult sendBatchEmail(@RequestParam String from,
                                      @RequestParam List<String> toList,
                                      @RequestParam String subject,
                                      @RequestParam String content,
                                      @RequestParam(defaultValue = "false") boolean isHtml) {
        int successCount = emailService.sendBatchEmail(from, toList, subject, content, isHtml);
        Map<String, Object> result = new HashMap<>();
        result.put("total", toList.size());
        result.put("success", successCount);
        result.put("failed", toList.size() - successCount);
        return success(result);
    }
}
