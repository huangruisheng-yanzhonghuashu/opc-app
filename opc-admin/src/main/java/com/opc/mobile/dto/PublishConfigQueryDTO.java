package com.opc.mobile.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;


/**
 * 发布配置查询DTO
 *
 * @author opc
 */
@Schema(description = "发布配置查询参数")
public class PublishConfigQueryDTO
{
    /** 平台类型 (ios/android) */
    @NotBlank(message = "平台类型不能为空")
    @Schema(description = "平台类型：ios或android", required = true)
    private String platformType;

    public String getPlatformType()
    {
        return platformType;
    }

    public void setPlatformType(String platformType)
    {
        this.platformType = platformType;
    }
}
