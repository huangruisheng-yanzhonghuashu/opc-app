package com.opc.core.domain;

import java.io.Serializable;

/**
 * 素材用户行为记录对象 core_material_user_action
 *
 * @author opc
 */
public class CoreMaterialUserAction implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 素材ID */
    private Long materialId;

    /** 用户ID（会员ID） */
    private Long userId;

    /** 行为类型（like:喜欢, dislike:不喜欢） */
    private String actionType;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getMaterialId()
    {
        return materialId;
    }

    public void setMaterialId(Long materialId)
    {
        this.materialId = materialId;
    }

    public Long getUserId()
    {
        return userId;
    }

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public String getActionType()
    {
        return actionType;
    }

    public void setActionType(String actionType)
    {
        this.actionType = actionType;
    }

    @Override
    public String toString()
    {
        return "CoreMaterialUserAction{" +
                "id=" + id +
                ", materialId=" + materialId +
                ", userId=" + userId +
                ", actionType='" + actionType + '\'' +
                '}';
    }
}
