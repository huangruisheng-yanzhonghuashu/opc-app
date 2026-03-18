package com.opc.web.utils;

import com.opc.web.dto.EmailDTO;
import com.opc.web.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 邮件发送示例类
 * 展示如何使用MailUtils和EmailService发送邮件
 *
 * @author opc
 */
@Component
public class MailExample {

    @Autowired
    private MailUtils mailUtils;

    @Autowired
    private EmailService emailService;

    /**
     * 示例1：发送简单文本邮件
     */
    public void example1() {
        String from = "sender@example.com";
        String to = "receiver@example.com";
        String subject = "测试邮件";
        String content = "这是一封测试邮件，内容为纯文本。";

        boolean result = mailUtils.sendSimpleEmail(from, to, subject, content);
        System.out.println("发送结果：" + (result ? "成功" : "失败"));
    }

    /**
     * 示例2：发送HTML邮件
     */
    public void example2() {
        String from = "sender@example.com";
        String to = "receiver@example.com";
        String subject = "HTML邮件测试";
        String htmlContent = """
                <html>
                <body>
                    <h2>这是一封HTML邮件</h2>
                    <p>欢迎使用邮件发送工具！</p>
                    <ul>
                        <li>功能1：发送纯文本邮件</li>
                        <li>功能2：发送HTML邮件</li>
                        <li>功能3：发送带附件的邮件</li>
                    </ul>
                </body>
                </html>
                """;

        boolean result = mailUtils.sendHtmlEmail(from, to, subject, htmlContent);
        System.out.println("发送结果：" + (result ? "成功" : "失败"));
    }

    /**
     * 示例3：发送带抄送和密送的邮件
     */
    public void example3() {
        String from = "sender@example.com";
        String to = "receiver1@example.com";
        String cc = "cc@example.com";
        String bcc = "bcc@example.com";
        String subject = "测试抄送和密送";
        String content = "这是一封带抄送和密送的测试邮件。";

        boolean result = mailUtils.sendSimpleEmail(from, to, cc, bcc, subject, content);
        System.out.println("发送结果：" + (result ? "成功" : "失败"));
    }

    /**
     * 示例4：发送带附件的邮件
     */
    public void example4() {
        String from = "sender@example.com";
        String to = "receiver@example.com";
        String subject = "带附件的邮件";
        String content = "请查收附件中的文件。";

        // 准备附件
        List<File> attachments = new ArrayList<>();
        attachments.add(new File("d:/test/file1.pdf"));
        attachments.add(new File("d:/test/file2.xlsx"));

        boolean result = mailUtils.sendEmailWithAttachment(from, to, subject, content, attachments);
        System.out.println("发送结果：" + (result ? "成功" : "失败"));
    }

    /**
     * 示例5：发送HTML邮件带附件和内嵌图片
     */
    public void example5() {
        String from = "sender@example.com";
        String to = "receiver@example.com";
        String subject = "HTML邮件带附件和图片";
        String htmlContent = """
                <html>
                <body>
                    <h2>HTML邮件示例</h2>
                    <img src="cid:logo" alt="Logo" width="200" />
                    <p>这封邮件包含内嵌图片和附件。</p>
                </body>
                </html>
                """;

        // 准备附件
        List<File> attachments = new ArrayList<>();
        attachments.add(new File("d:/test/document.pdf"));

        // 准备内嵌图片
        Map<String, File> inlineImages = new HashMap<>();
        inlineImages.put("logo", new File("d:/test/logo.png"));

        boolean result = mailUtils.sendEmail(from, to, null, null, subject, htmlContent, true, attachments, inlineImages);
        System.out.println("发送结果：" + (result ? "成功" : "失败"));
    }

    /**
     * 示例6：使用自定义SMTP服务器发送邮件
     */
    public void example6() {
        String host = "smtp.example.com";
        int port = 587;
        String username = "user@example.com";
        String password = "password";
        String from = "sender@example.com";
        String to = "receiver@example.com";
        String subject = "自定义SMTP测试";
        String content = "使用自定义SMTP服务器发送的测试邮件。";

        boolean result = mailUtils.sendEmailWithCustomSmtp(host, port, username, password,
                from, to, subject, content, false, false, true);
        System.out.println("发送结果：" + (result ? "成功" : "失败"));
    }

    /**
     * 示例7：使用Gmail发送邮件（SSL）
     */
    public void example7() {
        String from = "your@gmail.com";
        String to = "receiver@example.com";
        String subject = "Gmail测试";
        String content = "通过Gmail发送的测试邮件。";

        // Gmail SMTP配置
        boolean result = mailUtils.sendEmailWithCustomSmtp(
                "smtp.gmail.com",
                465,
                "your@gmail.com",
                "your-app-password",  // 需要使用应用专用密码
                from,
                to,
                subject,
                content,
                false,
                true,  // SSL
                false
        );
        System.out.println("发送结果：" + (result ? "成功" : "失败"));
    }

    /**
     * 示例8：使用QQ邮箱发送邮件
     */
    public void example8() {
        String from = "your@qq.com";
        String to = "receiver@example.com";
        String subject = "QQ邮箱测试";
        String content = "通过QQ邮箱发送的测试邮件。";

        // QQ邮箱SMTP配置
        boolean result = mailUtils.sendEmailWithCustomSmtp(
                "smtp.qq.com",
                465,
                "your@qq.com",
                "your-authorization-code",  // 需要使用授权码
                from,
                to,
                subject,
                content,
                false,
                true,  // SSL
                false
        );
        System.out.println("发送结果：" + (result ? "成功" : "失败"));
    }

    /**
     * 示例9：使用EmailDTO发送邮件
     */
    public void example9() {
        EmailDTO emailDTO = new EmailDTO();
        emailDTO.setFrom("sender@example.com");
        emailDTO.setTo("receiver@example.com");
        emailDTO.setCc("cc@example.com");
        emailDTO.setSubject("使用EmailDTO发送邮件");
        emailDTO.setContent("这是使用EmailDTO对象发送的邮件。");
        emailDTO.setHtml(false);

        boolean result = emailService.sendSimpleEmail(emailDTO);
        System.out.println("发送结果：" + (result ? "成功" : "失败"));
    }

    /**
     * 示例10：批量发送邮件
     */
    public void example10() {
        String from = "sender@example.com";
        List<String> toList = List.of(
                "user1@example.com",
                "user2@example.com",
                "user3@example.com"
        );
        String subject = "批量邮件测试";
        String content = "这是一封批量发送的测试邮件。";

        int successCount = mailUtils.sendBatchEmail(from, toList, subject, content, false);
        System.out.println("批量发送完成：共发送 " + toList.size() + " 封，成功 " + successCount + " 封");
    }
}
