package com.opc.mobile.controller;

import com.opc.common.constant.CacheConstants;
import com.opc.common.constant.Constants;
import com.opc.common.constant.UserConstants;
import com.opc.common.core.domain.AjaxResult;
import com.opc.common.core.domain.entity.SysUser;
import com.opc.common.core.redis.RedisCache;
import com.opc.common.utils.DateUtils;
import com.opc.common.utils.MessageUtils;
import com.opc.common.utils.SecurityUtils;
import com.opc.common.utils.StringUtils;
import com.opc.framework.manager.AsyncManager;
import com.opc.framework.manager.factory.AsyncFactory;
import com.opc.system.service.ISysConfigService;
import com.opc.system.service.ISysUserService;
import com.opc.mobile.dto.EmailCodeRequestDTO;
import com.opc.mobile.dto.MobileRegisterDTO;
import com.opc.web.dto.EmailDTO;
import com.opc.web.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * 移动端注册接口
 *
 * @author opc
 */
@RestController
@RequestMapping("/mobile/register")
public class MobileRegisterController extends MobileBaseController {

    private static final Logger log = LoggerFactory.getLogger(MobileRegisterController.class);

    @Autowired
    private ISysUserService userService;

    @Autowired
    private ISysConfigService configService;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private EmailService emailService;

    @Value("${spring.mail.username:}")
    private String mailFrom;

    /**
     * 发送邮箱验证码
     *
     * @param requestDTO 请求参数
     * @return 结果
     */
    @PostMapping("/sendEmailCode")
    public AjaxResult sendEmailCode(@RequestBody EmailCodeRequestDTO requestDTO) {
        String username = requestDTO.getUsername();
        String email = requestDTO.getEmail();

        // 参数校验
        if (StringUtils.isEmpty(username)) {
            return AjaxResult.error("用户名不能为空");
        }
        if (StringUtils.isEmpty(email)) {
            return AjaxResult.error("邮箱不能为空");
        }

        // 校验用户名长度
        if (username.length() < UserConstants.USERNAME_MIN_LENGTH
                || username.length() > UserConstants.USERNAME_MAX_LENGTH) {
            return AjaxResult.error("账户长度必须在2到20个字符之间");
        }

        // 校验邮箱格式
        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            return AjaxResult.error("邮箱格式不正确");
        }

        // 检查用户名是否已存在
        SysUser user = new SysUser();
        user.setUserName(username);
        if (!userService.checkUserNameUnique(user)) {
            return AjaxResult.error("用户名已存在");
        }

        // 检查邮箱是否已存在
        user.setEmail(email);
        if (!userService.checkEmailUnique(user)) {
            return AjaxResult.error("邮箱已被注册");
        }

        // 检查是否频繁发送（60秒内只能发送一次）
        String cacheKey = CacheConstants.EMAIL_CODE_KEY + email;
        String existingCode = redisCache.getCacheObject(cacheKey);
        if (existingCode != null) {
            long expireTime = redisCache.getExpire(cacheKey);
            if (expireTime > 240) { // 如果剩余时间大于4分钟（即60秒内）
                return AjaxResult.error("验证码发送过于频繁，请稍后再试");
            }
        }

        // 生成6位数字验证码
        String code = generateCode();

        // 发送邮件
        String subject = "注册验证码";
        String content = buildEmailContent(username, code);
        boolean sendResult = emailService.sendHtmlEmail(
                createEmailDTO(mailFrom, email, subject, content)
        );

        if (sendResult) {
            // 将验证码存入Redis，有效期5分钟
            redisCache.setCacheObject(cacheKey, code, 5, TimeUnit.MINUTES);
            log.info("邮箱验证码发送成功：username={}, email={}", username, email);
            return AjaxResult.success("验证码已发送至您的邮箱，有效期5分钟");
        } else {
            log.error("邮箱验证码发送失败：username={}, email={}", username, email);
            return AjaxResult.error("验证码发送失败，请稍后重试");
        }
    }

    /**
     * 邮箱验证码注册
     *
     * @param registerDTO 注册参数
     * @return 结果
     */
    @PostMapping("/registerByEmail")
    public AjaxResult registerByEmail(@RequestBody MobileRegisterDTO registerDTO) {
        // 检查系统是否开启注册功能
        if (!("true".equals(configService.selectConfigByKey("sys.account.registerUser")))) {
            return AjaxResult.error("当前系统没有开启注册功能！");
        }

        String username = registerDTO.getUsername();
        String password = registerDTO.getPassword();
        String email = registerDTO.getEmail();
        String code = registerDTO.getCode();

        // 参数校验
        if (StringUtils.isEmpty(username)) {
            return AjaxResult.error("用户名不能为空");
        }
        if (StringUtils.isEmpty(password)) {
            return AjaxResult.error("密码不能为空");
        }
        if (StringUtils.isEmpty(email)) {
            return AjaxResult.error("邮箱不能为空");
        }
        if (StringUtils.isEmpty(code)) {
            return AjaxResult.error("验证码不能为空");
        }

        // 校验用户名长度
        if (username.length() < UserConstants.USERNAME_MIN_LENGTH
                || username.length() > UserConstants.USERNAME_MAX_LENGTH) {
            return AjaxResult.error("账户长度必须在2到20个字符之间");
        }

        // 校验密码长度
        if (password.length() < UserConstants.PASSWORD_MIN_LENGTH
                || password.length() > UserConstants.PASSWORD_MAX_LENGTH) {
            return AjaxResult.error("密码长度必须在5到20个字符之间");
        }

        // 校验邮箱格式
        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            return AjaxResult.error("邮箱格式不正确");
        }

        // 验证验证码
        String cacheKey = CacheConstants.EMAIL_CODE_KEY + email;
        String cacheCode = redisCache.getCacheObject(cacheKey);
        if (cacheCode == null) {
            return AjaxResult.error("验证码已过期，请重新获取");
        }
        if (!code.equals(cacheCode)) {
            return AjaxResult.error("验证码错误");
        }

        // 检查用户名是否已存在
        SysUser sysUser = new SysUser();
        sysUser.setUserName(username);
        if (!userService.checkUserNameUnique(sysUser)) {
            return AjaxResult.error("用户名已存在");
        }

        // 检查邮箱是否已存在
        sysUser.setEmail(email);
        if (!userService.checkEmailUnique(sysUser)) {
            return AjaxResult.error("邮箱已被注册");
        }

        // 创建用户
        sysUser.setNickName(username);
        sysUser.setPwdUpdateDate(DateUtils.getNowDate());
        sysUser.setPassword(SecurityUtils.encryptPassword(password));
        sysUser.setEmail(email);

        boolean regFlag = userService.registerUser(sysUser);
        if (!regFlag) {
            return AjaxResult.error("注册失败，请联系系统管理人员");
        }

        // 删除已使用的验证码
        redisCache.deleteObject(cacheKey);

        // 记录注册日志
        AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.REGISTER,
                MessageUtils.message("user.register.success")));

        log.info("用户注册成功：username={}, email={}", username, email);
        return AjaxResult.success("注册成功");
    }

    /**
     * 生成6位数字验证码
     *
     * @return 验证码
     */
    private String generateCode() {
        return String.valueOf((int) ((Math.random() * 9 + 1) * 100000));
    }

    /**
     * 构建邮件内容
     *
     * @param username 用户名
     * @param code     验证码
     * @return HTML内容
     */
    private String buildEmailContent(String username, String code) {
        return "<div style='font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #e0e0e0; border-radius: 5px;'>" +
                "<h2 style='color: #333; text-align: center;'>用户注册</h2>" +
                "<p style='color: #666; font-size: 14px;'>尊敬的 <strong>" + username + "</strong>，您好！</p>" +
                "<p style='color: #666; font-size: 14px;'>您正在进行账号注册，验证码为：</p>" +
                "<div style='background-color: #f5f5f5; padding: 15px; text-align: center; margin: 20px 0; border-radius: 3px;'>" +
                "<span style='font-size: 28px; font-weight: bold; color: #1890ff; letter-spacing: 5px;'>" + code + "</span>" +
                "</div>" +
                "<p style='color: #666; font-size: 14px;'>验证码有效期为 <strong>5分钟</strong>，请勿泄露给他人。</p>" +
                "<p style='color: #999; font-size: 12px; margin-top: 30px; text-align: center;'>如非本人操作，请忽略此邮件。</p>" +
                "</div>";
    }

    /**
     * 创建邮件DTO
     */
    private EmailDTO createEmailDTO(String from, String to, String subject, String content) {
        EmailDTO emailDTO = new EmailDTO();
        emailDTO.setFrom(from);
        emailDTO.setTo(to);
        emailDTO.setSubject(subject);
        emailDTO.setContent(content);
        return emailDTO;
    }
}
