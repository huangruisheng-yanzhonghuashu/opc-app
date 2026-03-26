package com.opc.core.mapper;

import org.apache.ibatis.annotations.Param;
import com.opc.core.domain.CoreMaterialUserAction;

/**
 * 素材用户行为记录Mapper接口
 *
 * @author opc
 */
public interface CoreMaterialUserActionMapper
{
    /**
     * 查询用户是否已执行某操作
     *
     * @param materialId 素材ID
     * @param userId 用户ID
     * @param actionType 行为类型
     * @return 记录数
     */
    int checkUserAction(@Param("materialId") Long materialId,
                        @Param("userId") Long userId,
                        @Param("actionType") String actionType);

    /**
     * 插入用户行为记录
     *
     * @param action 行为记录
     * @return 影响行数
     */
    int insertUserAction(CoreMaterialUserAction action);

    /**
     * 删除用户行为记录
     *
     * @param materialId 素材ID
     * @param userId 用户ID
     * @param actionType 行为类型
     * @return 影响行数
     */
    int deleteUserAction(@Param("materialId") Long materialId,
                         @Param("userId") Long userId,
                         @Param("actionType") String actionType);

    /**
     * 删除用户的所有行为记录（切换操作时调用）
     *
     * @param materialId 素材ID
     * @param userId 用户ID
     * @return 影响行数
     */
    int deleteUserAllActions(@Param("materialId") Long materialId,
                             @Param("userId") Long userId);
}
