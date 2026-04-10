package com.opc.core.service;

import java.util.List;
import com.opc.core.domain.CoreActivationCode;

/**
 * 激活码Service接口
 *
 * @author opc
 */
public interface ICoreActivationCodeService {

    /**
     * 查询激活码
     *
     * @param id 激活码主键
     * @return 激活码
     */
    public CoreActivationCode selectCoreActivationCodeById(Long id);

    /**
     * 查询激活码列表
     *
     * @param coreActivationCode 激活码
     * @return 激活码集合
     */
    public List<CoreActivationCode> selectCoreActivationCodeList(CoreActivationCode coreActivationCode);

    /**
     * 批量生成激活码
     *
     * @param count 生成数量
     * @param validDays 有效天数
     * @param channelTag 渠道标签
     * @return 结果
     */
    public int batchGenerateActivationCode(int count, int validDays, String channelTag);

    /**
     * 修改激活码
     *
     * @param coreActivationCode 激活码
     * @return 结果
     */
    public int updateCoreActivationCode(CoreActivationCode coreActivationCode);

    /**
     * 批量删除激活码
     *
     * @param ids 需要删除的激活码主键集合
     * @return 结果
     */
    public int deleteCoreActivationCodeByIds(Long[] ids);

    /**
     * 发送激活码
     *
     * @param ids 主键数组
     * @return 结果
     */
    public int sendActivationCode(Long[] ids);

    /**
     * 注销激活码
     *
     * @param ids 主键数组
     * @return 结果
     */
    public int cancelActivationCode(Long[] ids);

    /**
     * 根据激活码查询
     *
     * @param code 激活码
     * @return 结果
     */
    public CoreActivationCode selectCoreActivationCodeByCode(String code);
}
