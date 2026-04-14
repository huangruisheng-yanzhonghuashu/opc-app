package com.opc.mobile.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.opc.common.core.controller.BaseController;
import com.opc.common.core.domain.AjaxResult;
import com.opc.core.domain.CorePublishConfig;
import com.opc.core.service.ICorePublishConfigService;
import com.opc.mobile.dto.PublishConfigQueryDTO;
import com.opc.mobile.vo.CorePublishConfigVO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 移动端发布配置接口
 * 
 * @author opc
 */
@Tag(name = "APP发布", description = "移动端发布配置相关接口")
@RestController
@RequestMapping("/mobile/publishConfig")
public class MobilePublishConfigController extends BaseController
{
    @Autowired
    private ICorePublishConfigService publishConfigService;


    /**
     * 获取当前发布状态
     * 
     * @param dto 查询参数
     * @return 发布配置VO (包含发布状态 0=发布中, 1=发布完成)
     */
    @Operation(summary = "获取发布状态", description = "根据平台类型获取当前发布配置及状态：0=发布中, 1=发布完成，无需登录")
    @ApiResponse(responseCode = "200", description = "成功", content = @Content(schema = @Schema(implementation = CorePublishConfigVO.class)))
    @PostMapping("/status")
    public AjaxResult getPublishStatus(@Valid @RequestBody PublishConfigQueryDTO dto)
    {
        CorePublishConfig query = new CorePublishConfig();
        query.setPlatformType(dto.getPlatformType());
        List<CorePublishConfig> list = publishConfigService.selectCorePublishConfigList(query);
        
        if (list == null || list.isEmpty())
        {
            // 返回空的VO对象，状态默认为0
            CorePublishConfigVO emptyVo = new CorePublishConfigVO();
            emptyVo.setPlatformType(dto.getPlatformType());
            emptyVo.setPublishStatus("0");
            return AjaxResult.success(emptyVo);
        }
        
        // 取最新的配置
        CorePublishConfig latest = list.get(0);
        CorePublishConfigVO vo = new CorePublishConfigVO();
        BeanUtils.copyProperties(latest, vo);
        return AjaxResult.success(vo);
    }
}
