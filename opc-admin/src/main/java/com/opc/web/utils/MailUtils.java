package com.opc.web.utils;

import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.activation.FileDataSource;
import jakarta.mail.*;
import jakarta.mail.internet.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * 通用邮件发送工具类
 * 支持发送纯文本邮件、HTML邮件、带附件的邮件
 *
 * @author opc
 */
@Component
public class MailUtils {

    private static final Logger log = LoggerFactory.getLogger(MailUtils.class);

    @Autowired
    private JavaMailSender mailSender;

    /**
     * 发送纯文本邮件
     *
     * @param from 发件人邮箱
     * @param to 收件人邮箱
     * @param subject 邮件主题
     * @param content 邮件内容
     * @return 发送结果
     */
    public boolean sendSimpleEmail(String from, String to, String subject, String content) {
        return sendSimpleEmail(from, to, null, null, subject, content);
    }

    /**
     * 发送纯文本邮件（支持抄送）
     *
     * @param from 发件人邮箱
     * @param to 收件人邮箱
     * @param cc 抄送人邮箱（可选）
     * @param bcc 密送人邮箱（可选）
     * @param subject 邮件主题
     * @param content 邮件内容
     * @return 发送结果
     */
    public boolean sendSimpleEmail(String from, String to, String cc, String bcc, String subject, String content) {
        return sendEmail(from, to, cc, bcc, subject, content, false, null, null);
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
    public boolean sendHtmlEmail(String from, String to, String subject, String htmlContent) {
        return sendHtmlEmail(from, to, null, null, subject, htmlContent);
    }

    /**
     * 发送HTML邮件（支持抄送）
     *
     * @param from 发件人邮箱
     * @param to 收件人邮箱
     * @param cc 抄送人邮箱（可选）
     * @param bcc 密送人邮箱（可选）
     * @param subject 邮件主题
     * @param htmlContent HTML内容
     * @return 发送结果
     */
    public boolean sendHtmlEmail(String from, String to, String cc, String bcc, String subject, String htmlContent) {
        return sendEmail(from, to, cc, bcc, subject, htmlContent, true, null, null);
    }

    /**
     * 发送带附件的邮件
     *
     * @param from 发件人邮箱
     * @param to 收件人邮箱
     * @param subject 邮件主题
     * @param content 邮件内容
     * @param attachmentFiles 附件文件列表
     * @return 发送结果
     */
    public boolean sendEmailWithAttachment(String from, String to, String subject, String content, List<File> attachmentFiles) {
        return sendEmailWithAttachment(from, to, null, null, subject, content, attachmentFiles);
    }

    /**
     * 发送带附件的邮件（支持抄送）
     *
     * @param from 发件人邮箱
     * @param to 收件人邮箱
     * @param cc 抄送人邮箱（可选）
     * @param bcc 密送人邮箱（可选）
     * @param subject 邮件主题
     * @param content 邮件内容
     * @param attachmentFiles 附件文件列表
     * @return 发送结果
     */
    public boolean sendEmailWithAttachment(String from, String to, String cc, String bcc, String subject, String content, List<File> attachmentFiles) {
        return sendEmail(from, to, cc, bcc, subject, content, false, attachmentFiles, null);
    }

    /**
     * 发送HTML邮件带附件
     *
     * @param from 发件人邮箱
     * @param to 收件人邮箱
     * @param subject 邮件主题
     * @param htmlContent HTML内容
     * @param attachmentFiles 附件文件列表
     * @return 发送结果
     */
    public boolean sendHtmlEmailWithAttachment(String from, String to, String subject, String htmlContent, List<File> attachmentFiles) {
        return sendHtmlEmailWithAttachment(from, to, null, null, subject, htmlContent, attachmentFiles);
    }

    /**
     * 发送HTML邮件带附件（支持抄送）
     *
     * @param from 发件人邮箱
     * @param to 收件人邮箱
     * @param cc 抄送人邮箱（可选）
     * @param bcc 密送人邮箱（可选）
     * @param subject 邮件主题
     * @param htmlContent HTML内容
     * @param attachmentFiles 附件文件列表
     * @return 发送结果
     */
    public boolean sendHtmlEmailWithAttachment(String from, String to, String cc, String bcc, String subject, String htmlContent, List<File> attachmentFiles) {
        return sendEmail(from, to, cc, bcc, subject, htmlContent, true, attachmentFiles, null);
    }

    /**
     * 发送邮件（通用方法）
     *
     * @param from 发件人邮箱
     * @param to 收件人邮箱
     * @param cc 抄送人邮箱（可选）
     * @param bcc 密送人邮箱（可选）
     * @param subject 邮件主题
     * @param content 邮件内容
     * @param isHtml 是否为HTML邮件
     * @param attachmentFiles 附件文件列表（可选）
     * @param inlineImages 内嵌图片（可选，Map<String, File> 格式：图片ID -> 图片文件）
     * @return 发送结果
     */
    public boolean sendEmail(String from, String to, String cc, String bcc, String subject, String content, boolean isHtml, List<File> attachmentFiles, Map<String, File> inlineImages) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            // 设置发件人
            helper.setFrom(from);
            // 设置收件人
            helper.setTo(to);
            // 设置抄送（可选）
            if (cc != null && !cc.isEmpty()) {
                helper.setCc(cc);
            }
            // 设置密送（可选）
            if (bcc != null && !bcc.isEmpty()) {
                helper.setBcc(bcc);
            }
            // 设置主题
            helper.setSubject(subject);
            // 设置内容
            helper.setText(content, isHtml);

            // 添加附件
            if (attachmentFiles != null && !attachmentFiles.isEmpty()) {
                for (File file : attachmentFiles) {
                    helper.addAttachment(file.getName(), file);
                }
            }

            // 添加内嵌图片
            if (inlineImages != null && !inlineImages.isEmpty()) {
                for (Map.Entry<String, File> entry : inlineImages.entrySet()) {
                    helper.addInline(entry.getKey(), entry.getValue());
                }
            }

            // 发送邮件
            mailSender.send(message);
            log.info("邮件发送成功：from={}, to={}, subject={}", from, to, subject);
            return true;
        } catch (Exception e) {
            log.error("邮件发送失败：from={}, to={}, subject={}, error={}", from, to, subject, e.getMessage(), e);
            return false;
        }
    }

    /**
     * 使用自定义SMTP服务器发送邮件
     *
     * @param host SMTP服务器地址
     * @param port SMTP服务器端口
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
    public boolean sendEmailWithCustomSmtp(String host, int port, String username, String password,
                                           String from, String to, String subject, String content,
                                           boolean isHtml, boolean useSSL, boolean useTLS) {
        return sendEmailWithCustomSmtp(host, port, username, password, from, to, null, null,
                                       subject, content, isHtml, null, null, useSSL, useTLS);
    }

    /**
     * 使用自定义SMTP服务器发送邮件（完整版）
     *
     * @param host SMTP服务器地址
     * @param port SMTP服务器端口
     * @param username 用户名
     * @param password 密码
     * @param from 发件人邮箱
     * @param to 收件人邮箱
     * @param cc 抄送人邮箱（可选）
     * @param bcc 密送人邮箱（可选）
     * @param subject 邮件主题
     * @param content 邮件内容
     * @param isHtml 是否为HTML邮件
     * @param attachmentFiles 附件文件列表（可选）
     * @param inlineImages 内嵌图片（可选）
     * @param useSSL 是否使用SSL
     * @param useTLS 是否使用TLS
     * @return 发送结果
     */
    public boolean sendEmailWithCustomSmtp(String host, int port, String username, String password,
                                           String from, String to, String cc, String bcc,
                                           String subject, String content, boolean isHtml,
                                           List<File> attachmentFiles, Map<String, File> inlineImages,
                                           boolean useSSL, boolean useTLS) {
        Session session = null;
        try {
            // 创建Properties配置
            Properties props = new Properties();
            props.put("mail.smtp.host", host);
            props.put("mail.smtp.port", port);
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.ssl.enable", String.valueOf(useSSL));
            props.put("mail.smtp.starttls.enable", String.valueOf(useTLS));

            if (useSSL) {
                props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
                props.put("mail.smtp.socketFactory.fallback", "false");
            }

            // 创建认证
            Authenticator authenticator = new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            };

            // 创建Session
            session = Session.getInstance(props, authenticator);

            // 创建消息
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));

            if (cc != null && !cc.isEmpty()) {
                message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(cc));
            }

            if (bcc != null && !bcc.isEmpty()) {
                message.setRecipients(Message.RecipientType.BCC, InternetAddress.parse(bcc));
            }

            message.setSubject(subject, "UTF-8");

            // 创建内容部分
            MimeBodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent(content, isHtml ? "text/html;charset=UTF-8" : "text/plain;charset=UTF-8");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);

            // 添加附件
            if (attachmentFiles != null && !attachmentFiles.isEmpty()) {
                for (File file : attachmentFiles) {
                    MimeBodyPart attachmentPart = new MimeBodyPart();
                    DataSource source = new FileDataSource(file);
                    attachmentPart.setDataHandler(new DataHandler(source));
                    attachmentPart.setFileName(MimeUtility.encodeText(file.getName()));
                    multipart.addBodyPart(attachmentPart);
                }
            }

            // 添加内嵌图片
            if (inlineImages != null && !inlineImages.isEmpty()) {
                for (Map.Entry<String, File> entry : inlineImages.entrySet()) {
                    MimeBodyPart imagePart = new MimeBodyPart();
                    DataSource imageSource = new FileDataSource(entry.getValue());
                    imagePart.setDataHandler(new DataHandler(imageSource));
                    imagePart.setHeader("Content-ID", "<" + entry.getKey() + ">");
                    imagePart.setDisposition(MimeBodyPart.INLINE);
                    multipart.addBodyPart(imagePart);
                }
            }

            message.setContent(multipart);

            // 发送邮件
            Transport.send(message);
            log.info("邮件发送成功（自定义SMTP）：from={}, to={}, subject={}", from, to, subject);
            return true;
        } catch (Exception e) {
            log.error("邮件发送失败（自定义SMTP）：from={}, to={}, subject={}, error={}", from, to, subject, e.getMessage(), e);
            return false;
        }
    }

    /**
     * 批量发送邮件
     *
     * @param from 发件人邮箱
     * @param toList 收件人邮箱列表
     * @param subject 邮件主题
     * @param content 邮件内容
     * @param isHtml 是否为HTML邮件
     * @return 成功发送的数量
     */
    public int sendBatchEmail(String from, List<String> toList, String subject, String content, boolean isHtml) {
        int successCount = 0;
        for (String to : toList) {
            if (sendEmail(from, to, null, null, subject, content, isHtml, null, null)) {
                successCount++;
            }
        }
        log.info("批量发送邮件完成：总数={}, 成功={}", toList.size(), successCount);
        return successCount;
    }
}
