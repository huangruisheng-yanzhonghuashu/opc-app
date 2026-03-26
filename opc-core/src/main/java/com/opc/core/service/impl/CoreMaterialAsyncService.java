package com.opc.core.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import com.opc.core.mapper.CoreMaterialMapper;

/**
 * 素材异步服务
 * 用于异步更新喜欢/不喜欢计数（原子操作）
 *
 * @author opc
 */
@Service
public class CoreMaterialAsyncService
{
    @Autowired
    private CoreMaterialMapper materialMapper;

    /**
     * 异步增加点赞数（原子操作）
     *
     * @param materialId 素材ID
     */
    @Async("threadPoolTaskExecutor")
    public void incrementLikeCount(Long materialId)
    {
        materialMapper.incrementLikeCount(materialId);
    }

    /**
     * 异步减少点赞数（原子操作）
     *
     * @param materialId 素材ID
     */
    @Async("threadPoolTaskExecutor")
    public void decrementLikeCount(Long materialId)
    {
        materialMapper.decrementLikeCount(materialId);
    }

    /**
     * 异步增加不喜欢数（原子操作）
     *
     * @param materialId 素材ID
     */
    @Async("threadPoolTaskExecutor")
    public void incrementDislikeCount(Long materialId)
    {
        materialMapper.incrementDislikeCount(materialId);
    }

    /**
     * 异步减少不喜欢数（原子操作）
     *
     * @param materialId 素材ID
     */
    @Async("threadPoolTaskExecutor")
    public void decrementDislikeCount(Long materialId)
    {
        materialMapper.decrementDislikeCount(materialId);
    }

    /**
     * 异步切换喜欢/不喜欢状态（原子操作）
     * 从不喜欢切换到喜欢时调用：减少不喜欢数 + 增加点赞数
     *
     * @param materialId 素材ID
     */
    @Async("threadPoolTaskExecutor")
    public void switchToLike(Long materialId)
    {
        // 先减少不喜欢数
        materialMapper.decrementDislikeCount(materialId);
        // 再增加点赞数
        materialMapper.incrementLikeCount(materialId);
    }

    /**
     * 异步切换喜欢/不喜欢状态（原子操作）
     * 从喜欢切换到不喜欢时调用：减少点赞数 + 增加不喜欢数
     *
     * @param materialId 素材ID
     */
    @Async("threadPoolTaskExecutor")
    public void switchToDislike(Long materialId)
    {
        // 先减少点赞数
        materialMapper.decrementLikeCount(materialId);
        // 再增加不喜欢数
        materialMapper.incrementDislikeCount(materialId);
    }

    /**
     * 异步增加查看数（原子操作）
     *
     * @param materialId 素材ID
     */
    @Async("threadPoolTaskExecutor")
    public void incrementViewCount(Long materialId)
    {
        materialMapper.incrementViewCount(materialId);
    }
}
