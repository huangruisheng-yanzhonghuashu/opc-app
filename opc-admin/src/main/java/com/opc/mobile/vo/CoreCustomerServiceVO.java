package com.opc.mobile.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;

/**
 * 客服配置 VO
 *
 * @author opc
 */
@Schema(description = "客服配置信息")
public class CoreCustomerServiceVO
{
    private static final long serialVersionUID = 1L;

    /** 客服ID */
    @Schema(description = "客服ID")
    private Long id;

    /** 客服名称 */
    @Schema(description = "客服名称")
    private String serviceName;

    /** 客服二维码URL */
    @Schema(description = "客服二维码URL")
    private String qrCodeUrl;

    /** 客服微信号 */
    @Schema(description = "客服微信号")
    private String wechatId;

    /** 客服电话 */
    @Schema(description = "客服电话")
    private String phone;

    /** 是否默认（0是 1否） */
    @Schema(description = "是否默认（0是 1否）")
    private String isDefault;

    /** 状态（0启用 1禁用） */
    @Schema(description = "状态（0启用 1禁用）")
    private String status;

    /** 排序号 */
    @Schema(description = "排序号")
    private Integer sortOrder;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建时间")
    private Date createTime;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "更新时间")
    private Date updateTime;

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

    public String getServiceName()
    {
        return serviceName;
    }

    public void setServiceName(String serviceName)
    {
        this.serviceName = serviceName;
    }

    public String getQrCodeUrl()
    {
        return qrCodeUrl;
    }

    public void setQrCodeUrl(String qrCodeUrl)
    {
        this.qrCodeUrl = qrCodeUrl;
    }

    public String getWechatId()
    {
        return wechatId;
    }

    public void setWechatId(String wechatId)
    {
        this.wechatId = wechatId;
    }

    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public String getIsDefault()
    {
        return isDefault;
    }

    public void setIsDefault(String isDefault)
    {
        this.isDefault = isDefault;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public Integer getSortOrder()
    {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder)
    {
        this.sortOrder = sortOrder;
    }

    public Date getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime(Date createTime)
    {
        this.createTime = createTime;
    }

    public Date getUpdateTime()
    {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime)
    {
        this.updateTime = updateTime;
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
