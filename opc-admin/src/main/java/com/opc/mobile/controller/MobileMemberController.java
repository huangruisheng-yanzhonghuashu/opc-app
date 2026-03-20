package com.opc.mobile.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.opc.common.annotation.Log;
import com.opc.common.config.RuoYiConfig;
import com.opc.common.core.domain.AjaxResult;
import com.opc.common.enums.BusinessType;
import com.opc.common.utils.file.FileUploadUtils;
import com.opc.common.utils.file.FileUtils;
import com.opc.core.domain.CoreMember;
import com.opc.core.domain.vo.MemberLoginVO;
import com.opc.core.service.ICoreMemberService;
import com.opc.core.service.MemberTokenService;
import com.opc.framework.config.ServerConfig;
import com.opc.mobile.dto.MemberUpdateDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;

/**
 * 移动端会员接口
 *
 * @author opc
 */
@Tag(name = "移动端会员管理", description = "移动端会员信息管理接口")
@RestController
@RequestMapping("/mobile/member")
public class MobileMemberController
{
    private static final Logger log = LoggerFactory.getLogger(MobileMemberController.class);

    @Autowired
    private MemberTokenService memberTokenService;

    @Autowired
    private ICoreMemberService memberService;

    @Autowired
    private ServerConfig serverConfig;

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
     * 会员信息修改接口
     */
    @Operation(summary = "会员信息修改", description = "修改当前登录会员的信息")
    @Log(title = "会员信息修改", businessType = BusinessType.UPDATE)
    @PostMapping("/update")
    public AjaxResult updateMember(@RequestBody MemberUpdateDTO updateDTO, HttpServletRequest request)
    {
        // 验证会员登录状态
        MemberLoginVO memberLoginVO = memberTokenService.getLoginUser(request);
        if (memberLoginVO == null)
        {
            return AjaxResult.error("请先登录");
        }

        try
        {
            // 获取当前会员信息
            CoreMember member = memberService.selectMemberById(memberLoginVO.getMemberId());
            if (member == null)
            {
                return AjaxResult.error("会员不存在");
            }

            // 更新会员信息
            if (updateDTO.getNickname() != null)
            {
                member.setNickname(updateDTO.getNickname());
            }
            if (updateDTO.getPhoneNumber() != null)
            {
                member.setPhoneNumber(updateDTO.getPhoneNumber());
            }
            if (updateDTO.getAvatar() != null)
            {
                member.setAvatar(updateDTO.getAvatar());
            }

            int result = memberService.updateMember(member);
            if (result > 0)
            {
                log.info("会员信息修改成功：memberId={}", memberLoginVO.getMemberId());

                // 更新登录用户信息
                memberLoginVO.setNickname(member.getNickname());
                memberLoginVO.setAvatar(member.getAvatar());
                memberTokenService.setLoginUser(memberLoginVO);

                AjaxResult ajax = AjaxResult.success("修改成功");
                ajax.put("member", member);
                return ajax;
            }
            else
            {
                return AjaxResult.error("修改失败");
            }
        }
        catch (Exception e)
        {
            log.error("会员信息修改失败：memberId={}", memberLoginVO.getMemberId(), e);
            return AjaxResult.error("修改失败：" + e.getMessage());
        }
    }
}
