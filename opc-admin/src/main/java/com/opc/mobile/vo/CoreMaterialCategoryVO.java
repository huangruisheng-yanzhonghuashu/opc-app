package com.opc.mobile.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;

/**
 * 素材二级分类 VO
 *
 * @author opc
 */
@Schema(description = "素材二级分类信息")
public class CoreMaterialCategoryVO
{
    private static final long serialVersionUID = 1L;

    /** 分类ID */
    @Schema(description = "分类ID")
    private Long id;

    /** 分类名称 */
    @Schema(description = "分类名称")
    private String categoryName;

    /** 套餐分类（0晨报 1普通素材 2VIP素材 3超级VIP） */
    @Schema(description = "套餐分类（0晨报 1普通素材 2VIP素材 3超级VIP）")
    private Integer packageType;

    /** 排序 */
    @Schema(description = "排序")
    private Integer sortOrder;

    /** 状态（0启用 1禁用） */
    @Schema(description = "状态（0启用 1禁用）")
    private String status;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建时间")
    private Instant createTime;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "更新时间")
    private Instant updateTime;

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

    public String getCategoryName()
    {
        return categoryName;
    }

    public void setCategoryName(String categoryName)
    {
        this.categoryName = categoryName;
    }

    public Integer getPackageType()
    {
        return packageType;
    }

    public void setPackageType(Integer packageType)
    {
        this.packageType = packageType;
    }

    public Integer getSortOrder()
    {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder)
    {
        this.sortOrder = sortOrder;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public Instant getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime(Instant createTime)
    {
        this.createTime = createTime;
    }

    public Instant getUpdateTime()
    {
        return updateTime;
    }

    public void setUpdateTime(Instant updateTime)
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
