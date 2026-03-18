package com.opc.web.dto;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * 邮件数据传输对象
 * 用于封装邮件发送所需的所有参数
 *
 * @author opc
 */
public class EmailDTO {

    /**
     * 发件人邮箱
     */
    private String from;

    /**
     * 收件人邮箱
     */
    private String to;

    /**
     * 抄送人邮箱（可选）
     */
    private String cc;

    /**
     * 密送人邮箱（可选）
     */
    private String bcc;

    /**
     * 邮件主题
     */
    private String subject;

    /**
     * 邮件内容
     */
    private String content;

    /**
     * 是否为HTML邮件
     */
    private boolean isHtml = false;

    /**
     * 附件文件列表（可选）
     */
    private List<File> attachmentFiles;

    /**
     * 内嵌图片（可选，Map<String, File> 格式：图片ID -> 图片文件）
     */
    private Map<String, File> inlineImages;

    /**
     * 自定义SMTP服务器配置（可选）
     */
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

    /**
     * SMTP配置类
     */
    public static class SmtpConfig {
        /**
         * SMTP服务器地址
         */
        private String host;

        /**
         * SMTP服务器端口
         */
        private int port;

        /**
         * 用户名
         */
        private String username;

        /**
         * 密码
         */
        private String password;

        /**
         * 是否使用SSL
         */
        private boolean useSSL = false;

        /**
         * 是否使用TLS
         */
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
