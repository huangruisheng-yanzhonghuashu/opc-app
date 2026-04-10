package com.opc.core.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.opc.common.annotation.Excel;
import com.opc.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 激活码对象 core_activation_code
 *
 * @author opc
 */
public class CoreActivationCode extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 激活码 */
    @Excel(name = "激活码")
    private String code;

    /** 有效天数 */
    @Excel(name = "有效天数")
    private Integer validDays;

    /** 过期时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "过期时间", width = 20, dateFormat = "yyyy-MM-dd")
    private Date expireTime;

    /** 渠道标签 */
    @Excel(name = "渠道标签")
    private String channelTag;

    /** 批次号 */
    @Excel(name = "批次号")
    private String batchNo;

    /** 状态（0未使用 1已发送-未使用 2已发送-已使用 3已注销 4已过期） */
    @Excel(name = "状态", readConverterExp = "0=未使用,1=已发送-未使用,2=已发送-已使用,3=已注销,4=已过期")
    private String status;

    /** 发送时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "发送时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date sendTime;

    /** 使用时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "使用时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date useTime;

    /** 使用用户ID */
    private Long useUserId;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setValidDays(Integer validDays) {
        this.validDays = validDays;
    }

    public Integer getValidDays() {
        return validDays;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }

    public Date getExpireTime() {
        return expireTime;
    }

    public void setChannelTag(String channelTag) {
        this.channelTag = channelTag;
    }

    public String getChannelTag() {
        return channelTag;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setUseTime(Date useTime) {
        this.useTime = useTime;
    }

    public Date getUseTime() {
        return useTime;
    }

    public void setUseUserId(Long useUserId) {
        this.useUserId = useUserId;
    }

    public Long getUseUserId() {
        return useUserId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("code", getCode())
                .append("validDays", getValidDays())
                .append("expireTime", getExpireTime())
                .append("channelTag", getChannelTag())
                .append("batchNo", getBatchNo())
                .append("status", getStatus())
                .append("sendTime", getSendTime())
                .append("useTime", getUseTime())
                .append("useUserId", getUseUserId())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .toString();
    }
}
