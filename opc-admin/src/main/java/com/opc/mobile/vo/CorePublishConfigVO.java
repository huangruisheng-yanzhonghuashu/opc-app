package com.opc.mobile.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;

/**
 * 发布配置 VO
 *
 * @author opc
 */
@Schema(description = "发布配置信息")
public class CorePublishConfigVO
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @Schema(description = "主键ID")
    private Long id;

    /** 平台类型 (ios/android) */
    @Schema(description = "平台类型 (ios/android)")
    private String platformType;

    /** 版本号 */
    @Schema(description = "版本号")
    private String version;

    /** 发布状态 (0=发布中, 1=发布完成) */
    @Schema(description = "发布状态 (0=发布中, 1=发布完成)")
    private String publishStatus;

    /** 发布时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "发布时间")
    private Date publishTime;

    /** 备注 */
    @Schema(description = "备注")
    private String remark;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

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

    public String getPublishStatus()
    {
        return publishStatus;
    }

    public void setPublishStatus(String publishStatus)
    {
        this.publishStatus = publishStatus;
    }

    public Date getPublishTime()
    {
        return publishTime;
    }

    public void setPublishTime(Date publishTime)
    {
        this.publishTime = publishTime;
    }

    public String getRemark()
    {
        return remark;
    }

    public void setRemark(String remark)
    {
        this.remark = remark;
    }
}
