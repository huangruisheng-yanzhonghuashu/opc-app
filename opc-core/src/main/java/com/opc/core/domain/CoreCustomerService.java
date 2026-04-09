package com.opc.core.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.opc.common.annotation.Excel;
import com.opc.common.annotation.Excel.ColumnType;
import com.opc.common.core.domain.BaseEntity;

/**
 * 客服配置对象 core_customer_service
 * 
 * @author opc
 */
public class CoreCustomerService extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 客服ID */
    @Excel(name = "客服ID", cellType = ColumnType.NUMERIC)
    private Long id;

    /** 客服名称 */
    @Excel(name = "客服名称")
    private String serviceName;

    /** 客服二维码URL */
    @Excel(name = "客服二维码")
    private String qrCodeUrl;

    /** 客服微信号 */
    @Excel(name = "客服微信号")
    private String wechatId;

    /** 客服电话 */
    @Excel(name = "客服电话")
    private String phone;

    /** 是否默认（0是 1否） */
    @Excel(name = "是否默认", readConverterExp = "0=是,1=否")
    private String isDefault;

    /** 状态（0启用 1禁用） */
    @Excel(name = "状态", readConverterExp = "0=启用,1=禁用")
    private String status;

    /** 排序号 */
    @Excel(name = "排序号")
    private Integer sortOrder;

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

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("serviceName", getServiceName())
            .append("qrCodeUrl", getQrCodeUrl())
            .append("wechatId", getWechatId())
            .append("phone", getPhone())
            .append("isDefault", getIsDefault())
            .append("status", getStatus())
            .append("sortOrder", getSortOrder())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
