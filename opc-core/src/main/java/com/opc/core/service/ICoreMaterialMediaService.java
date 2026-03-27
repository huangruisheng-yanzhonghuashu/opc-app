package com.opc.core.service;

import com.opc.core.domain.CoreMaterial;

/**
 * 素材媒体下载服务接口
 */
public interface ICoreMaterialMediaService {

    /**
     * 异步下载素材中的媒体文件（图片/视频）
     *
     * @param material 素材对象
     */
    void downloadMediaAsync(CoreMaterial material);
}
