package com.opc.core.mapper;

import java.util.Date;
import java.util.List;
import com.opc.core.domain.CoreActivationCode;
import org.apache.ibatis.annotations.Param;

/**
 * 激活码Mapper接口
 *
 * @author opc
 */
public interface CoreActivationCodeMapper {

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
     * 新增激活码
     *
     * @param coreActivationCode 激活码
     * @return 结果
     */
    public int insertCoreActivationCode(CoreActivationCode coreActivationCode);

    /**
     * 批量新增激活码
     *
     * @param list 激活码列表
     * @return 结果
     */
    public int batchInsertCoreActivationCode(List<CoreActivationCode> list);

    /**
     * 修改激活码
     *
     * @param coreActivationCode 激活码
     * @return 结果
     */
    public int updateCoreActivationCode(CoreActivationCode coreActivationCode);

    /**
     * 删除激活码
     *
     * @param id 激活码主键
     * @return 结果
     */
    public int deleteCoreActivationCodeById(Long id);

    /**
     * 批量删除激活码
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteCoreActivationCodeByIds(Long[] ids);

    /**
     * 根据激活码查询
     *
     * @param code 激活码
     * @return 结果
     */
    public CoreActivationCode selectCoreActivationCodeByCode(String code);

    /**
     * 批量修改状态
     *
     * @param ids 主键数组
     * @param status 状态
     * @return 结果
     */
    public int batchUpdateStatus(@Param("ids") Long[] ids, @Param("status") String status);

    /**
     * 批量修改状态和发送时间
     *
     * @param ids 主键数组
     * @param status 状态
     * @param sendTime 发送时间
     * @return 结果
     */
    public int batchUpdateStatusAndSendTime(@Param("ids") Long[] ids, @Param("status") String status, @Param("sendTime") Date sendTime);
}
