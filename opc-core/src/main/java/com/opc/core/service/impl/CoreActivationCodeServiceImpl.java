package com.opc.core.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.opc.common.utils.DateUtils;
import com.opc.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.opc.core.mapper.CoreActivationCodeMapper;
import com.opc.core.domain.CoreActivationCode;
import com.opc.core.service.ICoreActivationCodeService;

/**
 * 激活码Service业务层处理
 *
 * @author opc
 */
@Service
public class CoreActivationCodeServiceImpl implements ICoreActivationCodeService {

    @Autowired
    private CoreActivationCodeMapper coreActivationCodeMapper;

    /**
     * 查询激活码
     *
     * @param id 激活码主键
     * @return 激活码
     */
    @Override
    public CoreActivationCode selectCoreActivationCodeById(Long id) {
        return coreActivationCodeMapper.selectCoreActivationCodeById(id);
    }

    /**
     * 查询激活码列表
     *
     * @param coreActivationCode 激活码
     * @return 激活码
     */
    @Override
    public List<CoreActivationCode> selectCoreActivationCodeList(CoreActivationCode coreActivationCode) {
        return coreActivationCodeMapper.selectCoreActivationCodeList(coreActivationCode);
    }

    /**
     * 批量生成激活码
     *
     * @param count      生成数量
     * @param validDays  有效天数
     * @param channelTag 渠道标签
     * @return 结果
     */
    @Override
    public int batchGenerateActivationCode(int count, int validDays, String channelTag) {
        // 生成批次号：年月日时分秒+随机数
        String batchNo = DateUtils.dateTimeNow("yyyyMMddHHmmss") + UUID.randomUUID().toString().substring(0, 6);
        String createBy = SecurityUtils.getUsername();
        Date now = DateUtils.getNowDate();

        // 计算过期时间
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.add(Calendar.DAY_OF_MONTH, validDays);
        Date expireTime = calendar.getTime();

        List<CoreActivationCode> list = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            CoreActivationCode code = new CoreActivationCode();
            // 生成激活码：AC + 16位随机字符（数字+大写字母）
            code.setCode(generateActivationCode());
            code.setValidDays(validDays);
            code.setExpireTime(expireTime);
            code.setChannelTag(channelTag);
            code.setBatchNo(batchNo);
            code.setStatus("0"); // 0-未使用
            code.setCreateBy(createBy);
            code.setCreateTime(now);
            list.add(code);
        }
        return coreActivationCodeMapper.batchInsertCoreActivationCode(list);
    }

    /**
     * 生成激活码
     *
     * @return 激活码
     */
    private String generateActivationCode() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder("AC");
        for (int i = 0; i < 16; i++) {
            int index = (int) (Math.random() * chars.length());
            sb.append(chars.charAt(index));
        }
        return sb.toString();
    }

    /**
     * 修改激活码
     *
     * @param coreActivationCode 激活码
     * @return 结果
     */
    @Override
    public int updateCoreActivationCode(CoreActivationCode coreActivationCode) {
        coreActivationCode.setUpdateTime(DateUtils.getNowDate());
        return coreActivationCodeMapper.updateCoreActivationCode(coreActivationCode);
    }

    /**
     * 批量删除激活码
     *
     * @param ids 需要删除的激活码主键
     * @return 结果
     */
    @Override
    public int deleteCoreActivationCodeByIds(Long[] ids) {
        return coreActivationCodeMapper.deleteCoreActivationCodeByIds(ids);
    }

    /**
     * 发送激活码（将状态从"未使用"改为"已发送-未使用"）
     *
     * @param ids 主键数组
     * @return 结果
     */
    @Override
    public int sendActivationCode(Long[] ids) {
        // 查询所有记录，检查状态
        for (Long id : ids) {
            CoreActivationCode code = coreActivationCodeMapper.selectCoreActivationCodeById(id);
            if (code == null) {
                throw new RuntimeException("激活码不存在：" + id);
            }
            // 只有"未使用"状态的才能发送
            if (!"0".equals(code.getStatus())) {
                throw new RuntimeException("只有未使用的激活码才能发送，ID：" + id);
            }
        }
        // 批量更新状态和发送时间
        return coreActivationCodeMapper.batchUpdateStatusAndSendTime(ids, "1", DateUtils.getNowDate()); // 1-已发送-未使用
    }

    /**
     * 注销激活码
     *
     * @param ids 主键数组
     * @return 结果
     */
    @Override
    public int cancelActivationCode(Long[] ids) {
        // 查询所有记录，检查状态
        for (Long id : ids) {
            CoreActivationCode code = coreActivationCodeMapper.selectCoreActivationCodeById(id);
            if (code == null) {
                throw new RuntimeException("激活码不存在：" + id);
            }
            // 已使用和已注销的不能再次注销
            if ("2".equals(code.getStatus()) || "3".equals(code.getStatus())) {
                throw new RuntimeException("已使用或已注销的激活码不能再次注销，ID：" + id);
            }
        }
        return coreActivationCodeMapper.batchUpdateStatus(ids, "3"); // 3-已注销
    }

    /**
     * 根据激活码查询
     *
     * @param code 激活码
     * @return 结果
     */
    @Override
    public CoreActivationCode selectCoreActivationCodeByCode(String code) {
        return coreActivationCodeMapper.selectCoreActivationCodeByCode(code);
    }
}
