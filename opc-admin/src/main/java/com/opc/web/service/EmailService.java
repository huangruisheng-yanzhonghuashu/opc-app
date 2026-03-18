package com.opc.web.service;

import com.opc.web.dto.EmailDTO;
import com.opc.web.utils.MailUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 邮件服务类
 * 提供邮件发送的业务层封装
 *
 * @author opc
 */
@Service
public class EmailService {

    @Autowired
    private MailUtils mailUtils;

    /**
     * 发送纯文本邮件
     *
     * @param emailDTO 邮件数据传输对象
     * @return 发送结果
     */
    public boolean sendSimpleEmail(EmailDTO emailDTO) {
        return mailUtils.sendEmail(
                emailDTO.getFrom(),
                emailDTO.getTo(),
                emailDTO.getCc(),
                emailDTO.getBcc(),
                emailDTO.getSubject(),
                emailDTO.getContent(),
                false,
                emailDTO.getAttachmentFiles(),
                emailDTO.getInlineImages()
        );
    }

    /**
     * 发送HTML邮件
     *
     * @param emailDTO 邮件数据传输对象
     * @return 发送结果
     */
    public boolean sendHtmlEmail(EmailDTO emailDTO) {
        return mailUtils.sendEmail(
                emailDTO.getFrom(),
                emailDTO.getTo(),
                emailDTO.getCc(),
                emailDTO.getBcc(),
                emailDTO.getSubject(),
                emailDTO.getContent(),
                true,
                emailDTO.getAttachmentFiles(),
                emailDTO.getInlineImages()
        );
    }

    /**
     * 使用自定义SMTP服务器发送邮件
     *
     * @param emailDTO 邮件数据传输对象
     * @return 发送结果
     */
    public boolean sendEmailWithCustomSmtp(EmailDTO emailDTO) {
        EmailDTO.SmtpConfig config = emailDTO.getSmtpConfig();
        if (config == null) {
            throw new IllegalArgumentException("SMTP配置不能为空");
        }

        return mailUtils.sendEmailWithCustomSmtp(
                config.getHost(),
                config.getPort(),
                config.getUsername(),
                config.getPassword(),
                emailDTO.getFrom(),
                emailDTO.getTo(),
                emailDTO.getCc(),
                emailDTO.getBcc(),
                emailDTO.getSubject(),
                emailDTO.getContent(),
                emailDTO.isHtml(),
                emailDTO.getAttachmentFiles(),
                emailDTO.getInlineImages(),
                config.isUseSSL(),
                config.isUseTLS()
        );
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
        return mailUtils.sendBatchEmail(from, toList, subject, content, isHtml);
    }
}
