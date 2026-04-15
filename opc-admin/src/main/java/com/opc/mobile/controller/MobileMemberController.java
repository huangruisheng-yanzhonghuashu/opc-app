package com.opc.mobile.controller;

import com.opc.common.annotation.MemberAnonymous;
import com.opc.common.annotation.MemberLogin;
import com.opc.common.utils.StringUtils;
import com.opc.core.service.IMemberLoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.opc.common.annotation.Log;
import com.opc.common.config.SopConfig;
import com.opc.common.constant.CacheConstants;
import com.opc.common.core.domain.AjaxResult;
import com.opc.common.core.redis.RedisCache;
import com.opc.common.enums.BusinessType;
import com.opc.common.utils.SecurityUtils;
import com.opc.common.utils.file.FileUploadUtils;
import com.opc.common.utils.file.FileUtils;
import com.opc.core.domain.CoreMember;
import com.opc.core.domain.CoreMemberConfig;
import com.opc.core.domain.CorePackage;
import com.opc.core.domain.CorePackageOrder;
import com.opc.core.domain.CoreFeedback;
import com.opc.core.domain.vo.MemberLoginVO;
import com.opc.core.service.ICoreBannerService;
import com.opc.core.service.ICoreFeedbackService;
import com.opc.core.service.ICoreMemberConfigService;
import com.opc.core.service.ICoreMemberService;
import com.opc.core.service.ICorePackageOrderService;
import com.opc.core.service.ICorePackageService;
import com.opc.core.service.MemberTokenService;
import com.opc.framework.config.ServerConfig;
import com.opc.mobile.dto.MemberUpdateUserNameDTO;
import com.opc.mobile.dto.MemberUpdatePasswordDTO;
import com.opc.mobile.dto.MemberCancelDTO;
import com.opc.mobile.dto.MemberBindEmailDTO;
import com.opc.mobile.dto.MemberResetPasswordByEmailDTO;
import com.opc.mobile.dto.EmailCodeRequestDTO;
import com.opc.mobile.dto.OrderIdDTO;
import com.opc.mobile.dto.FeedbackSubmitDTO;
import com.opc.mobile.dto.FeedbackIdDTO;
import com.opc.mobile.vo.CoreFeedbackVO;
import com.opc.mobile.vo.CoreMemberConfigVO;
import com.opc.mobile.vo.CorePackageOrderVO;
import com.opc.mobile.vo.CorePackageVO;
import com.opc.web.dto.EmailDTO;
import com.opc.web.service.EmailService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 移动端会员接口
 *
 * @author opc
 */
@Tag(name = "会员管理", description = "移动端会员信息、图片上传、购买记录、意见反馈管理接口")
@RestController
@RequestMapping("/mobile/member")
@MemberLogin
public class MobileMemberController {
    private static final Logger log = LoggerFactory.getLogger(MobileMemberController.class);

    @Autowired
    private IMemberLoginService memberLoginService;

    @Autowired
    private MemberTokenService memberTokenService;

    @Autowired
    private ICoreMemberService memberService;

    @Autowired
    private ICorePackageOrderService packageOrderService;

    @Autowired
    private ICorePackageService packageService;

    @Autowired
    private ICoreFeedbackService feedbackService;

    @Autowired
    private ICoreMemberConfigService memberConfigService;

    @Autowired
    private ICoreBannerService bannerService;

    @Autowired
    private ServerConfig serverConfig;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private EmailService emailService;

    @Value("${spring.mail.username:}")
    private String mailFrom;

    @Value("${member.register.skipEmailCode:false}")
    private boolean skipEmailCode;

    /**
     * 图片上传接口（匿名访问，无需登录）
     */
    @Operation(summary = "图片上传", description = "上传图片，返回图片URL")
    @Log(title = "图片上传", businessType = BusinessType.UPDATE)
    @PostMapping("/upload")
    @MemberAnonymous
    public AjaxResult uploadImage(MultipartFile file, HttpServletRequest request) {
        try {
            if (file == null || file.isEmpty()) {
                return AjaxResult.error("请选择要上传的文件");
            }

            // 上传文件路径
            String filePath = SopConfig.getUploadPath();
            // 上传并返回新文件名称
            String fileName = FileUploadUtils.upload(filePath, file);
            String url = serverConfig.getUrl() + fileName;

            log.info("匿名上传图片成功：fileName={}", fileName);

            AjaxResult ajax = AjaxResult.success("上传成功");
            ajax.put("url", url);
            ajax.put("fileName", fileName);
            ajax.put("newFileName", FileUtils.getName(fileName));
            ajax.put("originalFilename", file.getOriginalFilename());
            return ajax;
        } catch (Exception e) {
            log.error("匿名上传图片失败", e);
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 头像修改接口
     */
    @Operation(summary = "头像修改", description = "上传新头像并自动更新会员头像")
    @Log(title = "头像修改", businessType = BusinessType.UPDATE)
    @PostMapping("/avatar")
    public AjaxResult updateAvatar(MultipartFile file, HttpServletRequest request) {
        // 验证会员登录状态
        MemberLoginVO memberLoginVO = memberTokenService.getLoginUser(request);
        if (memberLoginVO == null) {
            return AjaxResult.error("请先登录");
        }

        try {
            if (file == null || file.isEmpty()) {
                return AjaxResult.error("请选择要上传的新头像");
            }

            // 上传文件路径
            String filePath = SopConfig.getUploadPath();
            // 上传并返回新文件名称
            String fileName = FileUploadUtils.upload(filePath, file);
            String url = serverConfig.getUrl() + fileName;

            // 更新会员头像
            CoreMember member = memberService.selectMemberById(memberLoginVO.getMemberId());
            if (member == null) {
                return AjaxResult.error("会员不存在");
            }
            member.setAvatar(url);
            int result = memberService.updateMember(member);

            if (result > 0) {
                // 更新登录用户信息
                memberLoginVO.setAvatar(url);
                memberTokenService.setLoginUser(memberLoginVO);

                log.info("会员头像修改成功：memberId={}, fileName={}", memberLoginVO.getMemberId(), fileName);

                AjaxResult ajax = AjaxResult.success("头像修改成功");
                ajax.put("url", url);
                ajax.put("avatar", url);
                return ajax;
            } else {
                return AjaxResult.error("头像更新失败");
            }
        } catch (Exception e) {
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
    public AjaxResult updateUsername(@Valid @RequestBody MemberUpdateUserNameDTO updateDTO, HttpServletRequest request) {
        // 验证会员登录状态
        MemberLoginVO memberLoginVO = memberTokenService.getLoginUser(request);
        if (memberLoginVO == null) {
            return AjaxResult.error("请先登录");
        }

        try {
            String username = updateDTO.getUsername();

            // 获取当前会员信息
            CoreMember member = memberService.selectMemberById(memberLoginVO.getMemberId());
            if (member == null) {
                return AjaxResult.error("会员不存在");
            }

            // 检查用户名是否变更
            if (username.equals(member.getUsername())) {
                return AjaxResult.success("用户名未变更");
            }

            // 检查用户名唯一性
            member.setUsername(username);
            if (!memberService.checkMemberNameUnique(member)) {
                return AjaxResult.error("该用户名已被使用，请更换其他用户名");
            }

            // 更新用户名
            int result = memberService.updateMember(member);
            if (result > 0) {
                log.info("会员用户名修改成功：memberId={}, username={}", memberLoginVO.getMemberId(), username);

                // 更新登录用户信息
                memberLoginVO.setUsername(username);
                memberTokenService.setLoginUser(memberLoginVO);

                AjaxResult ajax = AjaxResult.success("用户名修改成功");
                ajax.put("username", username);
                return ajax;
            } else {
                return AjaxResult.error("用户名修改失败");
            }
        } catch (Exception e) {
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
    public AjaxResult updatePassword(@Valid @RequestBody MemberUpdatePasswordDTO updateDTO, HttpServletRequest request) {
        MemberLoginVO memberLoginVO = memberTokenService.getLoginUser(request);
        if (memberLoginVO == null) {
            return AjaxResult.error("请先登录");
        }

        try {
            String oldPassword = updateDTO.getOldPassword();
            String newPassword = updateDTO.getNewPassword();
            String confirmPassword = updateDTO.getConfirmPassword();

            // 校验两次输入的密码是否一致
            if (!newPassword.equals(confirmPassword)) {
                return AjaxResult.error("两次输入的密码不一致");
            }

            CoreMember member = memberService.selectMemberById(memberLoginVO.getMemberId());
            if (member == null) {
                return AjaxResult.error("会员不存在");
            }

            if (!SecurityUtils.matchesPassword(oldPassword, member.getPassword())) {
                return AjaxResult.error("原密码错误");
            }

            if (oldPassword.equals(newPassword)) {
                return AjaxResult.error("新密码不能与原密码相同");
            }

            member.setPassword(SecurityUtils.encryptPassword(newPassword));
            int result = memberService.updateMember(member);

            if (result > 0) {
                log.info("会员密码修改成功：memberId={}", memberLoginVO.getMemberId());
                return AjaxResult.success("密码修改成功");
            } else {
                return AjaxResult.error("密码修改失败");
            }
        } catch (Exception e) {
            log.error("会员密码修改失败：memberId={}", memberLoginVO.getMemberId(), e);
            return AjaxResult.error("密码修改失败：" + e.getMessage());
        }
    }


    @Operation(summary = "获取会员信息", description = "获取当前登录会员的详细信息")
    @PostMapping("/getMemberInfo")
    public AjaxResult getMemberInfo() {
        MemberLoginVO memberLoginVO = memberLoginService.getMemberLoginUser();
        if (StringUtils.isNull(memberLoginVO)) {
            return AjaxResult.error("用户未登录");
        }
        return AjaxResult.success(memberLoginVO);
    }

    /**
     * 会员注销接口
     */
    @Operation(summary = "会员注销", description = "注销当前登录会员账户（账户将被禁用）")
    @Log(title = "会员注销", businessType = BusinessType.DELETE)
    @PostMapping("/cancel")
    public AjaxResult cancelMember(HttpServletRequest request) {
        MemberLoginVO memberLoginVO = memberTokenService.getLoginUser(request);
        if (memberLoginVO == null) {
            return AjaxResult.error("请先登录");
        }

        try {
            CoreMember member = memberService.selectMemberById(memberLoginVO.getMemberId());
            if (member == null) {
                return AjaxResult.error("会员不存在");
            }

            // 检查账户是否已注销
            if ("2".equals(member.getStatus())) {
                return AjaxResult.error("账户已注销，无需重复操作");
            }

            // 注销会员账户（状态改为已注销）
            int result = memberService.cancelMember(memberLoginVO.getMemberId());

            if (result > 0) {
                // 清除登录状态
                memberTokenService.delLoginUser(memberLoginVO.getToken());

                log.info("会员注销成功：memberId={}", memberLoginVO.getMemberId());
                return AjaxResult.success("账户注销成功");
            } else {
                return AjaxResult.error("账户注销失败");
            }
        } catch (Exception e) {
            log.error("会员注销失败：memberId={}", memberLoginVO.getMemberId(), e);
            return AjaxResult.error("账户注销失败：" + e.getMessage());
        }
    }

    @Operation(summary = "发送绑定邮箱验证码", description = "向指定邮箱发送绑定验证码，验证码有效期5分钟")
    @PostMapping("/sendBindEmailCode")
    public AjaxResult sendBindEmailCode(@Validated @RequestBody EmailCodeRequestDTO requestDTO, HttpServletRequest httpRequest) {
        MemberLoginVO memberLoginVO = memberTokenService.getLoginUser(httpRequest);
        if (memberLoginVO == null) {
            return AjaxResult.error("请先登录");
        }

        String email = requestDTO.getEmail();

        CoreMember checkMember = new CoreMember();
        checkMember.setEmail(email);
        if (!memberService.checkEmailUnique(checkMember)) {
            return AjaxResult.error("该邮箱已被其他账户绑定");
        }

        String cacheKey = CacheConstants.EMAIL_CODE_KEY + "bind:" + email;
        String existingCode = redisCache.getCacheObject(cacheKey);
        if (existingCode != null) {
            long expireTime = redisCache.getExpire(cacheKey);
            if (expireTime > 240) {
                return AjaxResult.error("验证码发送过于频繁，请稍后再试");
            }
        }

        String code = generateCode();

        String subject = "绑定邮箱验证码";
        String content = buildBindEmailContent(code);
        boolean sendResult = emailService.sendHtmlEmail(createEmailDTO(mailFrom, email, subject, content));

        if (sendResult) {
            redisCache.setCacheObject(cacheKey, code, 5, TimeUnit.MINUTES);
            log.info("绑定邮箱验证码发送成功：memberId={}, email={}", memberLoginVO.getMemberId(), email);
            return AjaxResult.success("验证码已发送至您的邮箱，有效期5分钟");
        } else {
            log.error("绑定邮箱验证码发送失败：memberId={}, email={}", memberLoginVO.getMemberId(), email);
            return AjaxResult.error("验证码发送失败，请稍后重试");
        }
    }

    @Operation(summary = "绑定邮箱", description = "使用邮箱验证码绑定会员邮箱")
    @Log(title = "绑定邮箱", businessType = BusinessType.UPDATE)
    @PostMapping("/bindEmail")
    public AjaxResult bindEmail(@Valid @RequestBody MemberBindEmailDTO bindDTO, HttpServletRequest httpRequest) {
        MemberLoginVO memberLoginVO = memberTokenService.getLoginUser(httpRequest);
        if (memberLoginVO == null) {
            return AjaxResult.error("请先登录");
        }

        String email = bindDTO.getEmail();
        String code = bindDTO.getCode();

        CoreMember currentMember = memberService.selectMemberById(memberLoginVO.getMemberId());
        if (currentMember == null) {
            return AjaxResult.error("会员不存在");
        }

        if (StringUtils.isNotEmpty(currentMember.getEmail()) && email.equals(currentMember.getEmail())) {
            return AjaxResult.success("邮箱未变更");
        }

        CoreMember checkMember = new CoreMember();
        checkMember.setEmail(email);
        if (!memberService.checkEmailUnique(checkMember)) {
            return AjaxResult.error("该邮箱已被其他账户绑定");
        }

        String cacheKey = CacheConstants.EMAIL_CODE_KEY + "bind:" + email;
        String cacheCode = redisCache.getCacheObject(cacheKey);
        if (cacheCode == null) {
            return AjaxResult.error("验证码已过期，请重新获取");
        }
        if (!code.equals(cacheCode)) {
            return AjaxResult.error("验证码错误");
        }

        currentMember.setEmail(email);
        int result = memberService.updateMember(currentMember);

        if (result > 0) {
            memberLoginVO.setEmail(email);
            memberTokenService.setLoginUser(memberLoginVO);

            redisCache.deleteObject(cacheKey);

            log.info("会员绑定邮箱成功：memberId={}, email={}", memberLoginVO.getMemberId(), email);
            return AjaxResult.success("邮箱绑定成功");
        } else {
            return AjaxResult.error("邮箱绑定失败");
        }
    }

    private String generateCode() {
        return String.valueOf((int) ((Math.random() * 9 + 1) * 100000));
    }

    private String buildBindEmailContent(String code) {
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

    /**
     * 发送重置密码验证码
     */
    @Operation(summary = "发送重置密码验证码", description = "向指定邮箱发送重置密码验证码，验证码有效期5分钟")
    @PostMapping("/sendResetPasswordCode")
    @MemberAnonymous
    public AjaxResult sendResetPasswordCode(@Validated @RequestBody EmailCodeRequestDTO requestDTO) {
        String email = requestDTO.getEmail();

        // 检查邮箱是否已绑定会员
        CoreMember member = memberService.selectMemberByEmail(email);
        if (member == null) {
            return AjaxResult.error("该邮箱未绑定任何账户");
        }

        String cacheKey = CacheConstants.EMAIL_CODE_KEY + "reset_password:" + email;
        String existingCode = redisCache.getCacheObject(cacheKey);
        if (existingCode != null) {
            long expireTime = redisCache.getExpire(cacheKey);
            if (expireTime > 240) {
                return AjaxResult.error("验证码发送过于频繁，请稍后再试");
            }
        }

        String code = generateCode();

        String subject = "重置密码验证码";
        String content = buildResetPasswordEmailContent(code);
        boolean sendResult = emailService.sendHtmlEmail(createEmailDTO(mailFrom, email, subject, content));

        if (sendResult) {
            redisCache.setCacheObject(cacheKey, code, 5, TimeUnit.MINUTES);
            log.info("重置密码验证码发送成功：email={}", email);
            return AjaxResult.success("验证码已发送至您的邮箱，有效期5分钟");
        } else {
            log.error("重置密码验证码发送失败：email={}", email);
            return AjaxResult.error("验证码发送失败，请稍后重试");
        }
    }

    /**
     * 通过邮箱验证码重置密码
     */
    @Operation(summary = "通过邮箱验证码重置密码", description = "使用邮箱验证码重置会员密码，无需登录")
    @Log(title = "重置密码", businessType = BusinessType.UPDATE)
    @PostMapping("/resetPasswordByEmail")
    @MemberAnonymous
    public AjaxResult resetPasswordByEmail(@Valid @RequestBody MemberResetPasswordByEmailDTO resetDTO) {
        String email = resetDTO.getEmail();
        String code = resetDTO.getCode();
        String newPassword = resetDTO.getNewPassword();
        String confirmPassword = resetDTO.getConfirmPassword();

        // 校验两次输入的密码是否一致
        if (!newPassword.equals(confirmPassword)) {
            return AjaxResult.error("两次输入的密码不一致");
        }

        // 检查邮箱是否已绑定会员
        CoreMember member = memberService.selectMemberByEmail(email);
        if (member == null) {
            return AjaxResult.error("该邮箱未绑定任何账户");
        }

        // 验证验证码（开发环境可通过配置跳过）
        if (!skipEmailCode) {
            // 验证验证码
            String cacheKey = CacheConstants.EMAIL_CODE_KEY + "reset_password:" + email;
            String cacheCode = redisCache.getCacheObject(cacheKey);

            if (cacheCode == null) {
                return AjaxResult.error("验证码已过期，请重新获取");
            }
            if (!code.equals(cacheCode)) {
                return AjaxResult.error("验证码错误");
            }
        }


        // 检查新密码是否与旧密码相同
        if (SecurityUtils.matchesPassword(newPassword, member.getPassword())) {
            return AjaxResult.error("新密码不能与旧密码相同");
        }

        // 更新密码
        member.setPassword(SecurityUtils.encryptPassword(newPassword));
        int result = memberService.updateMember(member);

        if (result > 0) {
            // 删除已使用的验证码（仅在非跳过模式下）
            if (!skipEmailCode) {
                String cacheKey = CacheConstants.EMAIL_CODE_KEY + "reset_password:" + email;
                redisCache.deleteObject(cacheKey);
            }

            log.info("会员通过邮箱重置密码成功：memberId={}, email={}", member.getId(), email);
            return AjaxResult.success("密码重置成功");
        } else {
            return AjaxResult.error("密码重置失败");
        }
    }

    private String buildResetPasswordEmailContent(String code) {
        return "<div style='font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #e0e0e0; border-radius: 5px;'>" +
                "<h2 style='color: #333; text-align: center;'>重置密码</h2>" +
                "<p style='color: #666; font-size: 14px;'>尊敬的用户，您好！</p>" +
                "<p style='color: #666; font-size: 14px;'>您正在重置密码，验证码为：</p>" +
                "<div style='background-color: #f5f5f5; padding: 15px; text-align: center; margin: 20px 0; border-radius: 3px;'>" +
                "<span style='font-size: 28px; font-weight: bold; color: #ff4d4f; letter-spacing: 5px;'>" + code + "</span>" +
                "</div>" +
                "<p style='color: #666; font-size: 14px;'>验证码有效期为 <strong>5分钟</strong>，请勿泄露给他人。</p>" +
                "<p style='color: #999; font-size: 12px; margin-top: 30px; text-align: center;'>如非本人操作，请忽略此邮件。</p>" +
                "</div>";
    }

    private EmailDTO createEmailDTO(String from, String to, String subject, String content) {
        EmailDTO emailDTO = new EmailDTO();
        emailDTO.setFrom(from);
        emailDTO.setTo(to);
        emailDTO.setSubject(subject);
        emailDTO.setContent(content);
        return emailDTO;
    }

    /**
     * 购买记录列表接口
     */
    @Operation(summary = "购买记录列表", description = "获取当前登录会员的购买记录列表")
    @ApiResponse(responseCode = "200", description = "成功", content = @Content(schema = @Schema(implementation = CorePackageOrderVO.class)))
    @PostMapping("/order/list")
    public AjaxResult getOrderList(HttpServletRequest request) {
        MemberLoginVO memberLoginVO = memberTokenService.getLoginUser(request);
        if (memberLoginVO == null) {
            return AjaxResult.error("请先登录");
        }

        try {
            CorePackageOrder queryOrder = new CorePackageOrder();
            queryOrder.setMemberId(memberLoginVO.getMemberId());
            List<CorePackageOrder> orderList = packageOrderService.selectOrderList(queryOrder);

            // 转换为VO列表
            List<CorePackageOrderVO> voList = orderList.stream().map(o -> {
                CorePackageOrderVO vo = new CorePackageOrderVO();
                BeanUtils.copyProperties(o, vo);
                return vo;
            }).collect(Collectors.toList());

            log.info("会员查询购买记录列表：memberId={}, 记录数={}", memberLoginVO.getMemberId(), voList.size());

            return AjaxResult.success(voList);
        } catch (Exception e) {
            log.error("查询购买记录列表失败：memberId={}", memberLoginVO.getMemberId(), e);
            return AjaxResult.error("查询购买记录失败：" + e.getMessage());
        }
    }

    /**
     * 购买记录详情接口
     */
    @Operation(summary = "购买记录详情", description = "根据订单ID获取购买记录详情")
    @ApiResponse(responseCode = "200", description = "成功", content = @Content(schema = @Schema(implementation = CorePackageOrderVO.class)))
    @PostMapping("/order/detail")
    public AjaxResult getOrderDetail(@RequestBody OrderIdDTO orderIdDTO, HttpServletRequest request) {
        MemberLoginVO memberLoginVO = memberTokenService.getLoginUser(request);
        if (memberLoginVO == null) {
            return AjaxResult.error("请先登录");
        }

        Long id = orderIdDTO.getId();
        if (id == null || id <= 0) {
            return AjaxResult.error("订单ID不能为空");
        }

        try {
            CorePackageOrder order = packageOrderService.selectOrderById(id);
            if (order == null) {
                return AjaxResult.error("订单不存在");
            }

            // 验证订单是否属于当前会员
            if (!order.getMemberId().equals(memberLoginVO.getMemberId())) {
                return AjaxResult.error("无权查看该订单");
            }

            // 转换为VO
            CorePackageOrderVO vo = new CorePackageOrderVO();
            BeanUtils.copyProperties(order, vo);

            log.info("会员查询购买记录详情：memberId={}, orderId={}", memberLoginVO.getMemberId(), id);

            return AjaxResult.success(vo);
        } catch (Exception e) {
            log.error("查询购买记录详情失败：memberId={}, orderId={}", memberLoginVO.getMemberId(), id, e);
            return AjaxResult.error("查询订单详情失败：" + e.getMessage());
        }
    }

    /**
     * 提交意见反馈
     */
    @Operation(summary = "提交意见反馈", description = "移动端会员提交意见反馈")
    @Log(title = "移动端意见反馈", businessType = BusinessType.INSERT)
    @PostMapping("/feedback/submit")
    public AjaxResult submitFeedback(@Valid @RequestBody FeedbackSubmitDTO submitDTO, HttpServletRequest request) {
        MemberLoginVO memberLoginVO = memberTokenService.getLoginUser(request);

        CoreFeedback feedback = new CoreFeedback();

        // 如果已登录，关联会员信息
        if (memberLoginVO != null) {
            feedback.setMemberId(memberLoginVO.getMemberId());
            feedback.setMemberName(memberLoginVO.getNickname() != null ? memberLoginVO.getNickname() : memberLoginVO.getUsername());
        } else if (submitDTO.getMemberId() != null) {
            // 兼容未登录但传了memberId的情况
            CoreMember member = memberService.selectMemberById(submitDTO.getMemberId());
            if (member != null) {
                feedback.setMemberId(member.getId());
                feedback.setMemberName(member.getNickname() != null ? member.getNickname() : member.getUsername());
            }
        }

        feedback.setType(submitDTO.getType());
        feedback.setTitle(submitDTO.getTitle());
        feedback.setContent(submitDTO.getContent());
        feedback.setContact(submitDTO.getContact());

        int result = feedbackService.insertFeedback(feedback);
        if (result > 0) {
            log.info("会员提交意见反馈成功：memberId={}, title={}", feedback.getMemberId(), submitDTO.getTitle());
            return AjaxResult.success("提交成功");
        } else {
            return AjaxResult.error("提交失败");
        }
    }

    /**
     * 获取反馈列表
     */
    @Operation(summary = "获取反馈列表", description = "获取当前登录会员的反馈列表")
    @ApiResponse(responseCode = "200", description = "成功", content = @Content(schema = @Schema(implementation = CoreFeedbackVO.class)))
    @PostMapping("/feedback/list")
    public AjaxResult feedbackList(HttpServletRequest request) {
        MemberLoginVO memberLoginVO = memberTokenService.getLoginUser(request);
        if (memberLoginVO == null) {
            return AjaxResult.error("请先登录");
        }

        CoreFeedback feedback = new CoreFeedback();
        feedback.setMemberId(memberLoginVO.getMemberId());
        List<CoreFeedback> list = feedbackService.selectFeedbackList(feedback);

        // 转换为VO列表
        List<CoreFeedbackVO> voList = list.stream().map(f -> {
            CoreFeedbackVO vo = new CoreFeedbackVO();
            BeanUtils.copyProperties(f, vo);
            return vo;
        }).collect(Collectors.toList());

        log.info("会员查询反馈列表：memberId={}, 记录数={}", memberLoginVO.getMemberId(), voList.size());

        return AjaxResult.success(voList);
    }

    /**
     * 获取反馈详情
     */
    @Operation(summary = "获取反馈详情", description = "根据反馈ID获取详细信息")
    @ApiResponse(responseCode = "200", description = "成功", content = @Content(schema = @Schema(implementation = CoreFeedbackVO.class)))
    @PostMapping("/feedback/detail")
    public AjaxResult feedbackDetail(@Valid @RequestBody FeedbackIdDTO feedbackIdDTO, HttpServletRequest request) {
        MemberLoginVO memberLoginVO = memberTokenService.getLoginUser(request);
        if (memberLoginVO == null) {
            return AjaxResult.error("请先登录");
        }

        Long id = feedbackIdDTO.getId();
        CoreFeedback feedback = feedbackService.selectFeedbackById(id);
        if (feedback == null) {
            return AjaxResult.error("反馈不存在");
        }

        // 验证反馈是否属于当前会员
        if (feedback.getMemberId() == null || !feedback.getMemberId().equals(memberLoginVO.getMemberId())) {
            return AjaxResult.error("无权查看该反馈");
        }

        // 转换为VO
        CoreFeedbackVO vo = new CoreFeedbackVO();
        BeanUtils.copyProperties(feedback, vo);

        log.info("会员查询反馈详情：memberId={}, feedbackId={}", memberLoginVO.getMemberId(), id);

        return AjaxResult.success(vo);
    }

    /**
     * 会员banner图列表（不需要分页）
     */
    @Operation(summary = "会员banner图列表", description = "获取会员页banner图列表，按sortOrder升序排序")
    @ApiResponse(responseCode = "200", description = "成功", content = @Content(schema = @Schema(implementation = CoreMemberConfigVO.class)))
    @PostMapping("/banner/list")
    public AjaxResult memberBannerList() {
        CoreMemberConfig config = new CoreMemberConfig();
        config.setConfigType("banner");
        // 只查询启用的配置
        config.setStatus("0");
        List<CoreMemberConfig> list = memberConfigService.selectConfigList(config);

        // 转换为VO列表
        List<CoreMemberConfigVO> voList = list.stream().map(c -> {
            CoreMemberConfigVO vo = new CoreMemberConfigVO();
            BeanUtils.copyProperties(c, vo);
            return vo;
        }).collect(Collectors.toList());

        return AjaxResult.success(voList);
    }

    /**
     * VIP引导图片接口（不需要分页）
     */
    @Operation(summary = "VIP引导图片列表", description = "获取VIP引导图片配置列表")
    @ApiResponse(responseCode = "200", description = "成功", content = @Content(schema = @Schema(implementation = CoreMemberConfigVO.class)))
    @PostMapping("/vip/guide/list")
    public AjaxResult vipGuideList() {
        CoreMemberConfig config = new CoreMemberConfig();
        config.setConfigType("vip_guide");
        // 只查询启用的配置
        config.setStatus("0");
        List<CoreMemberConfig> list = memberConfigService.selectConfigList(config);

        // 转换为VO列表
        List<CoreMemberConfigVO> voList = list.stream().map(c -> {
            CoreMemberConfigVO vo = new CoreMemberConfigVO();
            BeanUtils.copyProperties(c, vo);
            return vo;
        }).collect(Collectors.toList());

        return AjaxResult.success(voList);
    }

    /**
     * 套餐配置列表接口（不需要分页）
     */
    @Operation(summary = "套餐配置列表", description = "获取所有上架的套餐配置列表")
    @ApiResponse(responseCode = "200", description = "成功", content = @Content(schema = @Schema(implementation = CorePackageVO.class)))
    @PostMapping("/package/list")
    public AjaxResult packageList() {
        CorePackage pkg = new CorePackage();
        // 只查询上架的套餐
        pkg.setStatus("0");
        List<CorePackage> list = packageService.selectPackageList(pkg);

        // 转换为VO列表
        List<CorePackageVO> voList = list.stream().map(p -> {
            CorePackageVO vo = new CorePackageVO();
            BeanUtils.copyProperties(p, vo);
            return vo;
        }).collect(Collectors.toList());

        return AjaxResult.success(voList);
    }

}
