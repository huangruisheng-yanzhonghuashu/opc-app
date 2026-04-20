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

    /** 版本号 */
    @Schema(description = "版本号", required = false)
    private String version;

    public String getPlatformType()
    {
        return platformType;
    }

    public void setPlatformType(String platformType)
    {
        this.platformType = platformType;
    }

    public String getVersion()
    {
        return version;
    }

    public void setVersion(String version)
    {
        this.version = version;
    }
}
