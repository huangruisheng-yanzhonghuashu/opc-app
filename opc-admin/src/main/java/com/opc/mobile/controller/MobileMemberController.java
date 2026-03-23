package com.opc.mobile.controller;

import com.opc.common.utils.StringUtils;
import com.opc.core.service.IMemberLoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.opc.common.annotation.Log;
import com.opc.common.config.RuoYiConfig;
import com.opc.common.constant.CacheConstants;
import com.opc.common.core.domain.AjaxResult;
import com.opc.common.core.redis.RedisCache;
import com.opc.common.enums.BusinessType;
import com.opc.common.utils.SecurityUtils;
import com.opc.common.utils.file.FileUploadUtils;
import com.opc.common.utils.file.FileUtils;
import com.opc.core.domain.CoreMember;
import com.opc.core.domain.vo.MemberLoginVO;
import com.opc.core.service.ICoreMemberService;
import com.opc.core.service.MemberTokenService;
import com.opc.framework.config.ServerConfig;
import com.opc.mobile.dto.MemberUpdateUserNameDTO;
import com.opc.mobile.dto.MemberUpdatePasswordDTO;
import com.opc.mobile.dto.MemberCancelDTO;
import com.opc.mobile.dto.MemberBindEmailDTO;
import com.opc.web.dto.EmailDTO;
import com.opc.web.service.EmailService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;

import java.util.concurrent.TimeUnit;

/**
 * 移动端会员接口
 *
 * @author opc
 */
@Tag(name = "会员管理", description = "移动端会员信息、图片上传管理接口")
@RestController
@RequestMapping("/mobile/member")
public class MobileMemberController
{
    private static final Logger log = LoggerFactory.getLogger(MobileMemberController.class);

    @Autowired
    private IMemberLoginService memberLoginService;

    @Autowired
    private MemberTokenService memberTokenService;

    @Autowired
    private ICoreMemberService memberService;

    @Autowired
    private ServerConfig serverConfig;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private EmailService emailService;

    @Value("${spring.mail.username:}")
    private String mailFrom;

    /**
     * 图片上传接口
     */
    @Operation(summary = "图片上传", description = "上传图片，返回图片URL")
    @Log(title = "图片上传", businessType = BusinessType.UPDATE)
    @PostMapping("/upload")
    public AjaxResult uploadImage(MultipartFile file, HttpServletRequest request)
    {
        // 验证会员登录状态
        MemberLoginVO memberLoginVO = memberTokenService.getLoginUser(request);
        if (memberLoginVO == null)
        {
            return AjaxResult.error("请先登录");
        }

        try
        {
            if (file == null || file.isEmpty())
            {
                return AjaxResult.error("请选择要上传的文件");
            }

            // 上传文件路径
            String filePath = RuoYiConfig.getUploadPath();
            // 上传并返回新文件名称
            String fileName = FileUploadUtils.upload(filePath, file);
            String url = serverConfig.getUrl() + fileName;

            log.info("会员上传图片成功：memberId={}, fileName={}", memberLoginVO.getMemberId(), fileName);

            AjaxResult ajax = AjaxResult.success("上传成功");
            ajax.put("url", url);
            ajax.put("fileName", fileName);
            ajax.put("newFileName", FileUtils.getName(fileName));
            ajax.put("originalFilename", file.getOriginalFilename());
            return ajax;
        }
        catch (Exception e)
        {
            log.error("会员上传图片失败：memberId={}", memberLoginVO.getMemberId(), e);
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 头像修改接口
     */
    @Operation(summary = "头像修改", description = "上传新头像并自动更新会员头像")
    @Log(title = "头像修改", businessType = BusinessType.UPDATE)
    @PostMapping("/avatar")
    public AjaxResult updateAvatar(MultipartFile file, HttpServletRequest request)
    {
        // 验证会员登录状态
        MemberLoginVO memberLoginVO = memberTokenService.getLoginUser(request);
        if (memberLoginVO == null)
        {
            return AjaxResult.error("请先登录");
        }

        try
        {
            if (file == null || file.isEmpty())
            {
                return AjaxResult.error("请选择要上传的新头像");
            }

            // 上传文件路径
            String filePath = RuoYiConfig.getUploadPath();
            // 上传并返回新文件名称
            String fileName = FileUploadUtils.upload(filePath, file);
            String url = serverConfig.getUrl() + fileName;

            // 更新会员头像
            CoreMember member = memberService.selectMemberById(memberLoginVO.getMemberId());
            if (member == null)
            {
                return AjaxResult.error("会员不存在");
            }
            member.setAvatar(url);
            int result = memberService.updateMember(member);

            if (result > 0)
            {
                // 更新登录用户信息
                memberLoginVO.setAvatar(url);
                memberTokenService.setLoginUser(memberLoginVO);

                log.info("会员头像修改成功：memberId={}, fileName={}", memberLoginVO.getMemberId(), fileName);

                AjaxResult ajax = AjaxResult.success("头像修改成功");
                ajax.put("url", url);
                ajax.put("avatar", url);
                return ajax;
            }
            else
            {
                return AjaxResult.error("头像更新失败");
            }
        }
        catch (Exception e)
        {
            log.error("会员头像修改失败：memberId={}", memberLoginVO.getMemberId(), e);
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 用户名修改接口
     */
    @Operation(summary = "用户名修改", description = "修改当前登录会员的用户名，用户名需唯一")
    @Log(title = "用户名修改", businessType = BusinessType.UPDATE)
    @PostMapping("/username")
    public AjaxResult updateUsername(@Valid @RequestBody MemberUpdateUserNameDTO updateDTO, HttpServletRequest request)
    {
        // 验证会员登录状态
        MemberLoginVO memberLoginVO = memberTokenService.getLoginUser(request);
        if (memberLoginVO == null)
        {
            return AjaxResult.error("请先登录");
        }

        try
        {
            String username = updateDTO.getUsername();
            if (username == null || username.trim().isEmpty())
            {
                return AjaxResult.error("用户名不能为空");
            }

            // 获取当前会员信息
            CoreMember member = memberService.selectMemberById(memberLoginVO.getMemberId());
            if (member == null)
            {
                return AjaxResult.error("会员不存在");
            }

            // 检查用户名是否变更
            if (username.equals(member.getUsername()))
            {
                return AjaxResult.success("用户名未变更");
            }

            // 检查用户名唯一性
            member.setUsername(username);
            if (!memberService.checkMemberNameUnique(member))
            {
                return AjaxResult.error("该用户名已被使用，请更换其他用户名");
            }

            // 更新用户名
            int result = memberService.updateMember(member);
            if (result > 0)
            {
                log.info("会员用户名修改成功：memberId={}, username={}", memberLoginVO.getMemberId(), username);

                // 更新登录用户信息
                memberLoginVO.setUsername(username);
                memberTokenService.setLoginUser(memberLoginVO);

                AjaxResult ajax = AjaxResult.success("用户名修改成功");
                ajax.put("username", username);
                return ajax;
            }
            else
            {
                return AjaxResult.error("用户名修改失败");
            }
        }
        catch (Exception e)
        {
            log.error("会员用户名修改失败：memberId={}", memberLoginVO.getMemberId(), e);
            return AjaxResult.error("用户名修改失败：" + e.getMessage());
        }
    }

    /**
     * 修改密码接口
     */
    @Operation(summary = "密码修改", description = "验证原密码后修改会员密码")
    @Log(title = "密码修改", businessType = BusinessType.UPDATE)
    @PostMapping("/password")
    public AjaxResult updatePassword(@Valid @RequestBody MemberUpdatePasswordDTO updateDTO, HttpServletRequest request)
    {
        MemberLoginVO memberLoginVO = memberTokenService.getLoginUser(request);
        if (memberLoginVO == null)
        {
            return AjaxResult.error("请先登录");
        }

        try
        {
            String oldPassword = updateDTO.getOldPassword();
            String newPassword = updateDTO.getNewPassword();
            String confirmPassword = updateDTO.getConfirmPassword();

            if (oldPassword == null || oldPassword.trim().isEmpty())
            {
                return AjaxResult.error("原密码不能为空");
            }
            if (newPassword == null || newPassword.trim().isEmpty())
            {
                return AjaxResult.error("新密码不能为空");
            }
            if (newPassword.length() < 6 || newPassword.length() > 20)
            {
                return AjaxResult.error("新密码长度必须在6-20位之间");
            }
            if (!newPassword.equals(confirmPassword))
            {
                return AjaxResult.error("两次输入的密码不一致");
            }

            CoreMember member = memberService.selectMemberById(memberLoginVO.getMemberId());
            if (member == null)
            {
                return AjaxResult.error("会员不存在");
            }

            if (!SecurityUtils.matchesPassword(oldPassword, member.getPassword()))
            {
                return AjaxResult.error("原密码错误");
            }

            if (oldPassword.equals(newPassword))
            {
                return AjaxResult.error("新密码不能与原密码相同");
            }

            member.setPassword(SecurityUtils.encryptPassword(newPassword));
            int result = memberService.updateMember(member);

            if (result > 0)
            {
                log.info("会员密码修改成功：memberId={}", memberLoginVO.getMemberId());
                return AjaxResult.success("密码修改成功");
            }
            else
            {
                return AjaxResult.error("密码修改失败");
            }
        }
        catch (Exception e)
        {
            log.error("会员密码修改失败：memberId={}", memberLoginVO.getMemberId(), e);
            return AjaxResult.error("密码修改失败：" + e.getMessage());
        }
    }


    @Operation(summary = "获取会员信息", description = "获取当前登录会员的详细信息")
    @GetMapping("/getMemberInfo")
    public AjaxResult getMemberInfo()
    {
        MemberLoginVO memberLoginVO = memberLoginService.getMemberLoginUser();
        if (StringUtils.isNull(memberLoginVO))
        {
            return AjaxResult.error("用户未登录");
        }
        return AjaxResult.success(memberLoginVO);
    }

    /**
     * 会员注销接口
     */
    @Operation(summary = "会员注销", description = "验证密码后注销当前登录会员账户（账户将被禁用）")
    @Log(title = "会员注销", businessType = BusinessType.DELETE)
    @PostMapping("/cancel")
    public AjaxResult cancelMember(@Valid @RequestBody MemberCancelDTO cancelDTO, HttpServletRequest request)
    {
        MemberLoginVO memberLoginVO = memberTokenService.getLoginUser(request);
        if (memberLoginVO == null)
        {
            return AjaxResult.error("请先登录");
        }

        try
        {
            String password = cancelDTO.getPassword();

            if (password == null || password.trim().isEmpty())
            {
                return AjaxResult.error("密码不能为空");
            }

            CoreMember member = memberService.selectMemberById(memberLoginVO.getMemberId());
            if (member == null)
            {
                return AjaxResult.error("会员不存在");
            }

            // 验证密码
            if (!SecurityUtils.matchesPassword(password, member.getPassword()))
            {
                return AjaxResult.error("密码错误");
            }

            // 检查账户是否已注销
            if ("2".equals(member.getStatus()))
            {
                return AjaxResult.error("账户已注销，无需重复操作");
            }

            // 注销会员账户（状态改为已注销）
            int result = memberService.cancelMember(memberLoginVO.getMemberId());

            if (result > 0)
            {
                // 清除登录状态
                memberTokenService.delLoginUser(memberLoginVO.getToken());

                log.info("会员注销成功：memberId={}, reason={}", memberLoginVO.getMemberId(), cancelDTO.getReason());
                return AjaxResult.success("账户注销成功");
            }
            else
            {
                return AjaxResult.error("账户注销失败");
            }
        }
        catch (Exception e)
        {
            log.error("会员注销失败：memberId={}", memberLoginVO.getMemberId(), e);
            return AjaxResult.error("账户注销失败：" + e.getMessage());
        }
    }

    @Operation(summary = "发送绑定邮箱验证码", description = "向指定邮箱发送绑定验证码，验证码有效期5分钟")
    @PostMapping("/sendBindEmailCode")
    public AjaxResult sendBindEmailCode(@RequestBody EmailCodeRequestDTO requestDTO, HttpServletRequest httpRequest)
    {
        MemberLoginVO memberLoginVO = memberTokenService.getLoginUser(httpRequest);
        if (memberLoginVO == null)
        {
            return AjaxResult.error("请先登录");
        }

        String email = requestDTO.getEmail();

        if (StringUtils.isEmpty(email))
        {
            return AjaxResult.error("邮箱不能为空");
        }

        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"))
        {
            return AjaxResult.error("邮箱格式不正确");
        }

        CoreMember checkMember = new CoreMember();
        checkMember.setEmail(email);
        if (!memberService.checkEmailUnique(checkMember))
        {
            return AjaxResult.error("该邮箱已被其他账户绑定");
        }

        String cacheKey = CacheConstants.EMAIL_CODE_KEY + "bind:" + email;
        String existingCode = redisCache.getCacheObject(cacheKey);
        if (existingCode != null)
        {
            long expireTime = redisCache.getExpire(cacheKey);
            if (expireTime > 240)
            {
                return AjaxResult.error("验证码发送过于频繁，请稍后再试");
            }
        }

        String code = generateCode();

        String subject = "绑定邮箱验证码";
        String content = buildBindEmailContent(code);
        boolean sendResult = emailService.sendHtmlEmail(createEmailDTO(mailFrom, email, subject, content));

        if (sendResult)
        {
            redisCache.setCacheObject(cacheKey, code, 5, TimeUnit.MINUTES);
            log.info("绑定邮箱验证码发送成功：memberId={}, email={}", memberLoginVO.getMemberId(), email);
            return AjaxResult.success("验证码已发送至您的邮箱，有效期5分钟");
        }
        else
        {
            log.error("绑定邮箱验证码发送失败：memberId={}, email={}", memberLoginVO.getMemberId(), email);
            return AjaxResult.error("验证码发送失败，请稍后重试");
        }
    }

    @Operation(summary = "绑定邮箱", description = "使用邮箱验证码绑定会员邮箱")
    @Log(title = "绑定邮箱", businessType = BusinessType.UPDATE)
    @PostMapping("/bindEmail")
    public AjaxResult bindEmail(@Valid @RequestBody MemberBindEmailDTO bindDTO, HttpServletRequest httpRequest)
    {
        MemberLoginVO memberLoginVO = memberTokenService.getLoginUser(httpRequest);
        if (memberLoginVO == null)
        {
            return AjaxResult.error("请先登录");
        }

        String email = bindDTO.getEmail();
        String code = bindDTO.getCode();

        if (StringUtils.isEmpty(email))
        {
            return AjaxResult.error("邮箱不能为空");
        }
        if (StringUtils.isEmpty(code))
        {
            return AjaxResult.error("验证码不能为空");
        }

        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"))
        {
            return AjaxResult.error("邮箱格式不正确");
        }

        CoreMember currentMember = memberService.selectMemberById(memberLoginVO.getMemberId());
        if (currentMember == null)
        {
            return AjaxResult.error("会员不存在");
        }

        if (StringUtils.isNotEmpty(currentMember.getEmail()) && email.equals(currentMember.getEmail()))
        {
            return AjaxResult.success("邮箱未变更");
        }

        CoreMember checkMember = new CoreMember();
        checkMember.setEmail(email);
        if (!memberService.checkEmailUnique(checkMember))
        {
            return AjaxResult.error("该邮箱已被其他账户绑定");
        }

        String cacheKey = CacheConstants.EMAIL_CODE_KEY + "bind:" + email;
        String cacheCode = redisCache.getCacheObject(cacheKey);
        if (cacheCode == null)
        {
            return AjaxResult.error("验证码已过期，请重新获取");
        }
        if (!code.equals(cacheCode))
        {
            return AjaxResult.error("验证码错误");
        }

        currentMember.setEmail(email);
        int result = memberService.updateMember(currentMember);

        if (result > 0)
        {
            memberLoginVO.setEmail(email);
            memberTokenService.setLoginUser(memberLoginVO);

            redisCache.deleteObject(cacheKey);

            log.info("会员绑定邮箱成功：memberId={}, email={}", memberLoginVO.getMemberId(), email);
            return AjaxResult.success("邮箱绑定成功");
        }
        else
        {
            return AjaxResult.error("邮箱绑定失败");
        }
    }

    private String generateCode()
    {
        return String.valueOf((int) ((Math.random() * 9 + 1) * 100000));
    }

    private String buildBindEmailContent(String code)
    {
        return "<div style='font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #e0e0e0; border-radius: 5px;'>" +
                "<h2 style='color: #333; text-align: center;'>绑定邮箱</h2>" +
                "<p style='color: #666; font-size: 14px;'>尊敬的用户，您好！</p>" +
                "<p style='color: #666; font-size: 14px;'>您正在绑定邮箱，验证码为：</p>" +
                "<div style='background-color: #f5f5f5; padding: 15px; text-align: center; margin: 20px 0; border-radius: 3px;'>" +
                "<span style='font-size: 28px; font-weight: bold; color: #1890ff; letter-spacing: 5px;'>" + code + "</span>" +
                "</div>" +
                "<p style='color: #666; font-size: 14px;'>验证码有效期为 <strong>5分钟</strong>，请勿泄露给他人。</p>" +
                "<p style='color: #999; font-size: 12px; margin-top: 30px; text-align: center;'>如非本人操作，请忽略此邮件。</p>" +
                "</div>";
    }

    private EmailDTO createEmailDTO(String from, String to, String subject, String content)
    {
        EmailDTO emailDTO = new EmailDTO();
        emailDTO.setFrom(from);
        emailDTO.setTo(to);
        emailDTO.setSubject(subject);
        emailDTO.setContent(content);
        return emailDTO;
    }

}
