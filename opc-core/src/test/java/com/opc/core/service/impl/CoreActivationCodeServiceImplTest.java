package com.opc.core.service.impl;

import com.opc.core.domain.CoreActivationCode;
import com.opc.core.mapper.CoreActivationCodeMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import com.opc.common.utils.SecurityUtils;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CoreActivationCodeServiceImplTest {

    @Mock
    private CoreActivationCodeMapper coreActivationCodeMapper;

    @InjectMocks
    private CoreActivationCodeServiceImpl activationCodeService;

    @Test
    public void testSelectCoreActivationCodeById() {
        CoreActivationCode code = new CoreActivationCode();
        code.setId(1L);
        code.setCode("AC1234567890ABCDEF");
        code.setStatus("0");

        when(coreActivationCodeMapper.selectCoreActivationCodeById(1L)).thenReturn(code);

        CoreActivationCode result = activationCodeService.selectCoreActivationCodeById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("AC1234567890ABCDEF", result.getCode());
        assertEquals("0", result.getStatus());
    }

    @Test
    public void testSelectCoreActivationCodeByIdNotFound() {
        when(coreActivationCodeMapper.selectCoreActivationCodeById(999L)).thenReturn(null);

        CoreActivationCode result = activationCodeService.selectCoreActivationCodeById(999L);

        assertNull(result);
    }

    @Test
    public void testSelectCoreActivationCodeByCode() {
        CoreActivationCode code = new CoreActivationCode();
        code.setId(1L);
        code.setCode("AC1234567890ABCDEF");
        code.setStatus("1");

        when(coreActivationCodeMapper.selectCoreActivationCodeByCode("AC1234567890ABCDEF")).thenReturn(code);

        CoreActivationCode result = activationCodeService.selectCoreActivationCodeByCode("AC1234567890ABCDEF");

        assertNotNull(result);
        assertEquals("AC1234567890ABCDEF", result.getCode());
        assertEquals("1", result.getStatus());
    }

    @Test
    public void testSelectCoreActivationCodeList() {
        CoreActivationCode code1 = new CoreActivationCode();
        code1.setId(1L);
        code1.setCode("AC1234567890ABCDEF");
        code1.setStatus("0");

        CoreActivationCode code2 = new CoreActivationCode();
        code2.setId(2L);
        code2.setCode("AC0987654321FEDCBA");
        code2.setStatus("1");

        when(coreActivationCodeMapper.selectCoreActivationCodeList(any(CoreActivationCode.class)))
                .thenReturn(Arrays.asList(code1, code2));

        List<CoreActivationCode> list = activationCodeService.selectCoreActivationCodeList(new CoreActivationCode());

        assertNotNull(list);
        assertEquals(2, list.size());
        assertEquals("AC1234567890ABCDEF", list.get(0).getCode());
        assertEquals("AC0987654321FEDCBA", list.get(1).getCode());
    }

    @Test
    public void testBatchGenerateActivationCode() {
        try (MockedStatic<SecurityUtils> securityUtils = mockStatic(SecurityUtils.class)) {
            securityUtils.when(SecurityUtils::getUsername).thenReturn("testUser");
            when(coreActivationCodeMapper.batchInsertCoreActivationCode(anyList())).thenReturn(5);

            int result = activationCodeService.batchGenerateActivationCode(5, 30, "TEST_CHANNEL");

            assertEquals(5, result);
            verify(coreActivationCodeMapper).batchInsertCoreActivationCode(argThat(list -> {
                if (list.size() != 5) return false;
                CoreActivationCode code = (CoreActivationCode) list.get(0);
                // 验证激活码格式：AC开头，共18位
                if (!code.getCode().startsWith("AC")) return false;
                if (code.getCode().length() != 18) return false;
                // 验证状态为未使用
                if (!"0".equals(code.getStatus())) return false;
                // 验证渠道标签
                if (!"TEST_CHANNEL".equals(code.getChannelTag())) return false;
                // 验证有效天数
                if (code.getValidDays() != 30) return false;
                // 验证过期时间已设置
                if (code.getExpireTime() == null) return false;
                // 验证创建人
                if (!"testUser".equals(code.getCreateBy())) return false;
                return true;
            }));
        }
    }

    @Test
    public void testUpdateCoreActivationCode() {
        CoreActivationCode code = new CoreActivationCode();
        code.setId(1L);
        code.setChannelTag("UPDATED_CHANNEL");

        when(coreActivationCodeMapper.updateCoreActivationCode(any(CoreActivationCode.class))).thenReturn(1);

        int result = activationCodeService.updateCoreActivationCode(code);

        assertEquals(1, result);
        verify(coreActivationCodeMapper).updateCoreActivationCode(argThat(c ->
            c.getId().equals(1L) && "UPDATED_CHANNEL".equals(c.getChannelTag()) && c.getUpdateTime() != null
        ));
    }

    @Test
    public void testDeleteCoreActivationCodeByIds() {
        Long[] ids = {1L, 2L, 3L};
        when(coreActivationCodeMapper.deleteCoreActivationCodeByIds(ids)).thenReturn(3);

        int result = activationCodeService.deleteCoreActivationCodeByIds(ids);

        assertEquals(3, result);
        verify(coreActivationCodeMapper).deleteCoreActivationCodeByIds(ids);
    }

    @Test
    public void testSendActivationCodeSuccess() {
        Long[] ids = {1L, 2L};

        CoreActivationCode code1 = new CoreActivationCode();
        code1.setId(1L);
        code1.setStatus("0");

        CoreActivationCode code2 = new CoreActivationCode();
        code2.setId(2L);
        code2.setStatus("0");

        when(coreActivationCodeMapper.selectCoreActivationCodeById(1L)).thenReturn(code1);
        when(coreActivationCodeMapper.selectCoreActivationCodeById(2L)).thenReturn(code2);
        when(coreActivationCodeMapper.batchUpdateStatusAndSendTime(eq(ids), eq("1"), any(Date.class))).thenReturn(2);

        int result = activationCodeService.sendActivationCode(ids);

        assertEquals(2, result);
    }

    @Test
    public void testSendActivationCodeNotFound() {
        Long[] ids = {999L};
        when(coreActivationCodeMapper.selectCoreActivationCodeById(999L)).thenReturn(null);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            activationCodeService.sendActivationCode(ids);
        });

        assertEquals("激活码不存在：999", exception.getMessage());
    }

    @Test
    public void testSendActivationCodeAlreadyUsed() {
        Long[] ids = {1L};

        CoreActivationCode code = new CoreActivationCode();
        code.setId(1L);
        code.setStatus("2"); // 已使用状态

        when(coreActivationCodeMapper.selectCoreActivationCodeById(1L)).thenReturn(code);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            activationCodeService.sendActivationCode(ids);
        });

        assertEquals("只有未使用的激活码才能发送，ID：1", exception.getMessage());
    }

    @Test
    public void testSendActivationCodeAlreadySent() {
        Long[] ids = {1L};

        CoreActivationCode code = new CoreActivationCode();
        code.setId(1L);
        code.setStatus("1"); // 已发送状态

        when(coreActivationCodeMapper.selectCoreActivationCodeById(1L)).thenReturn(code);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            activationCodeService.sendActivationCode(ids);
        });

        assertEquals("只有未使用的激活码才能发送，ID：1", exception.getMessage());
    }

    @Test
    public void testCancelActivationCodeSuccess() {
        Long[] ids = {1L, 2L};

        CoreActivationCode code1 = new CoreActivationCode();
        code1.setId(1L);
        code1.setStatus("0");

        CoreActivationCode code2 = new CoreActivationCode();
        code2.setId(2L);
        code2.setStatus("1");

        when(coreActivationCodeMapper.selectCoreActivationCodeById(1L)).thenReturn(code1);
        when(coreActivationCodeMapper.selectCoreActivationCodeById(2L)).thenReturn(code2);
        when(coreActivationCodeMapper.batchUpdateStatus(ids, "3")).thenReturn(2);

        int result = activationCodeService.cancelActivationCode(ids);

        assertEquals(2, result);
    }

    @Test
    public void testCancelActivationCodeNotFound() {
        Long[] ids = {999L};
        when(coreActivationCodeMapper.selectCoreActivationCodeById(999L)).thenReturn(null);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            activationCodeService.cancelActivationCode(ids);
        });

        assertEquals("激活码不存在：999", exception.getMessage());
    }

    @Test
    public void testCancelActivationCodeAlreadyUsed() {
        Long[] ids = {1L};

        CoreActivationCode code = new CoreActivationCode();
        code.setId(1L);
        code.setStatus("2"); // 已使用状态

        when(coreActivationCodeMapper.selectCoreActivationCodeById(1L)).thenReturn(code);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            activationCodeService.cancelActivationCode(ids);
        });

        assertEquals("已使用或已注销的激活码不能再次注销，ID：1", exception.getMessage());
    }

    @Test
    public void testCancelActivationCodeAlreadyCancelled() {
        Long[] ids = {1L};

        CoreActivationCode code = new CoreActivationCode();
        code.setId(1L);
        code.setStatus("3"); // 已注销状态

        when(coreActivationCodeMapper.selectCoreActivationCodeById(1L)).thenReturn(code);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            activationCodeService.cancelActivationCode(ids);
        });

        assertEquals("已使用或已注销的激活码不能再次注销，ID：1", exception.getMessage());
    }

    @Test
    public void testUseActivationCode() {
        when(coreActivationCodeMapper.updateActivationCodeUsed(eq("AC1234567890ABCDEF"), eq("2"), any(Date.class), eq(100L)))
                .thenReturn(1);

        int result = activationCodeService.useActivationCode("AC1234567890ABCDEF", 100L);

        assertEquals(1, result);
        verify(coreActivationCodeMapper).updateActivationCodeUsed(
                eq("AC1234567890ABCDEF"),
                eq("2"),
                any(Date.class),
                eq(100L)
        );
    }
}
