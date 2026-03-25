package com.opc.core.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.opc.common.annotation.Excel;
import com.opc.common.annotation.Excel.ColumnType;
import com.opc.common.core.domain.BaseEntity;
import jakarta.validation.constraints.NotNull;

public class CoreCommunityWantToGo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    @Excel(name = "记录ID", cellType = ColumnType.NUMERIC)
    private Long id;

    @Excel(name = "社区ID", cellType = ColumnType.NUMERIC)
    @NotNull(message = "社区ID不能为空")
    private Long communityId;

    @Excel(name = "会员ID", cellType = ColumnType.NUMERIC)
    @NotNull(message = "会员ID不能为空")
    private Long memberId;

    @Excel(name = "状态", readConverterExp = "0=正常,1=取消")
    private String status;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getCommunityId()
    {
        return communityId;
    }

    public void setCommunityId(Long communityId)
    {
        this.communityId = communityId;
    }

    public Long getMemberId()
    {
        return memberId;
    }

    public void setMemberId(Long memberId)
    {
        this.memberId = memberId;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("communityId", getCommunityId())
            .append("memberId", getMemberId())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
