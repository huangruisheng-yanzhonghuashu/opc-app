package com.opc.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * 邮件数据传输对象
 * 用于封装邮件发送所需的所有参数
 *
 * @author opc
 */
@Schema(description = "邮件发送请求参数")
public class EmailDTO {

    @Schema(description = "发件人邮箱", required = true, example = "sender@example.com")
    private String from;

    @Schema(description = "收件人邮箱", required = true, example = "receiver@example.com")
    private String to;

    @Schema(description = "抄送人邮箱，多个用逗号分隔", example = "cc@example.com")
    private String cc;

    @Schema(description = "密送人邮箱，多个用逗号分隔", example = "bcc@example.com")
    private String bcc;

    @Schema(description = "邮件主题", required = true, example = "邮件主题")
    private String subject;

    @Schema(description = "邮件内容", required = true, example = "<p>邮件正文内容</p>")
    private String content;

    @Schema(description = "是否为HTML邮件", example = "true")
    private boolean isHtml = false;

    @Schema(description = "附件文件列表")
    private List<File> attachmentFiles;

    @Schema(description = "内嵌图片，key为图片ID，value为图片文件")
    private Map<String, File> inlineImages;

    @Schema(description = "自定义SMTP服务器配置")
    private SmtpConfig smtpConfig;

    public EmailDTO() {
    }

    public EmailDTO(String from, String to, String subject, String content) {
        this.from = from;
        this.to = to;
        this.subject = subject;
        this.content = content;
    }

    public EmailDTO(String from, String to, String subject, String content, boolean isHtml) {
        this.from = from;
        this.to = to;
        this.subject = subject;
        this.content = content;
        this.isHtml = isHtml;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getCc() {
        return cc;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    public String getBcc() {
        return bcc;
    }

    public void setBcc(String bcc) {
        this.bcc = bcc;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isHtml() {
        return isHtml;
    }

    public void setHtml(boolean html) {
        isHtml = html;
    }

    public List<File> getAttachmentFiles() {
        return attachmentFiles;
    }

    public void setAttachmentFiles(List<File> attachmentFiles) {
        this.attachmentFiles = attachmentFiles;
    }

    public Map<String, File> getInlineImages() {
        return inlineImages;
    }

    public void setInlineImages(Map<String, File> inlineImages) {
        this.inlineImages = inlineImages;
    }

    public SmtpConfig getSmtpConfig() {
        return smtpConfig;
    }

    public void setSmtpConfig(SmtpConfig smtpConfig) {
        this.smtpConfig = smtpConfig;
    }

    @Schema(description = "SMTP配置类")
    public static class SmtpConfig {
        @Schema(description = "SMTP服务器地址", example = "smtp.example.com")
        private String host;

        @Schema(description = "SMTP服务器端口", example = "587")
        private int port;

        @Schema(description = "SMTP用户名", example = "username")
        private String username;

        @Schema(description = "SMTP密码", example = "password")
        private String password;

        @Schema(description = "是否使用SSL", example = "false")
        private boolean useSSL = false;

        @Schema(description = "是否使用TLS", example = "true")
        private boolean useTLS = false;

        public SmtpConfig() {
        }

        public SmtpConfig(String host, int port, String username, String password) {
            this.host = host;
            this.port = port;
            this.username = username;
            this.password = password;
        }

        public SmtpConfig(String host, int port, String username, String password, boolean useSSL, boolean useTLS) {
            this.host = host;
            this.port = port;
            this.username = username;
            this.password = password;
            this.useSSL = useSSL;
            this.useTLS = useTLS;
        }

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public boolean isUseSSL() {
            return useSSL;
        }

        public void setUseSSL(boolean useSSL) {
            this.useSSL = useSSL;
        }

        public boolean isUseTLS() {
            return useTLS;
        }

        public void setUseTLS(boolean useTLS) {
            this.useTLS = useTLS;
        }
    }
}
