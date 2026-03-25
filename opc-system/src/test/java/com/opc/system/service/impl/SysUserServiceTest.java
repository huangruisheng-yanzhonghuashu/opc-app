package com.opc.system.service.impl;

import com.opc.common.constant.UserConstants;
import com.opc.common.core.domain.entity.SysUser;
import com.opc.system.mapper.SysUserMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SysUserServiceTest
{
    @Mock
    private SysUserMapper userMapper;

    @InjectMocks
    private SysUserServiceImpl userService;

    private SysUser createTestUser()
    {
        SysUser user = new SysUser();
        user.setUserId(1L);
        user.setUserName("admin");
        user.setNickName("管理员");
        user.setEmail("admin@example.com");
        user.setPhonenumber("13800138000");
        user.setStatus("0");
        user.setDelFlag("0");
        return user;
    }

    @Test
    public void testSelectUserList()
    {
        SysUser user = createTestUser();
        List<SysUser> userList = Arrays.asList(user);

        when(userMapper.selectUserList(any(SysUser.class))).thenReturn(userList);

        List<SysUser> result = userService.selectUserList(new SysUser());

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("admin", result.get(0).getUserName());
    }

    @Test
    public void testSelectUserByUserName()
    {
        SysUser user = createTestUser();

        when(userMapper.selectUserByUserName("admin")).thenReturn(user);

        SysUser result = userService.selectUserByUserName("admin");

        assertNotNull(result);
        assertEquals("admin", result.getUserName());
    }

    @Test
    public void testSelectUserById()
    {
        SysUser user = createTestUser();

        when(userMapper.selectUserById(1L)).thenReturn(user);

        SysUser result = userService.selectUserById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getUserId());
    }

    @Test
    public void testCheckUserNameUnique_Unique()
    {
        SysUser user = createTestUser();

        when(userMapper.checkUserNameUnique("admin")).thenReturn(null);

        boolean result = userService.checkUserNameUnique(user);

        assertTrue(result);
    }

    @Test
    public void testCheckUserNameUnique_NotUnique()
    {
        SysUser user = createTestUser();
        SysUser existing = createTestUser();
        existing.setUserId(2L);

        when(userMapper.checkUserNameUnique("admin")).thenReturn(existing);

        boolean result = userService.checkUserNameUnique(user);

        assertFalse(result);
    }

    @Test
    public void testCheckUserNameUnique_SameId()
    {
        SysUser user = createTestUser();
        SysUser existing = createTestUser();

        when(userMapper.checkUserNameUnique("admin")).thenReturn(existing);

        boolean result = userService.checkUserNameUnique(user);

        assertTrue(result);
    }

    @Test
    public void testCheckPhoneUnique_Unique()
    {
        SysUser user = createTestUser();

        when(userMapper.checkPhoneUnique("13800138000")).thenReturn(null);

        boolean result = userService.checkPhoneUnique(user);

        assertTrue(result);
    }

    @Test
    public void testCheckPhoneUnique_NotUnique()
    {
        SysUser user = createTestUser();
        SysUser existing = createTestUser();
        existing.setUserId(2L);

        when(userMapper.checkPhoneUnique("13800138000")).thenReturn(existing);

        boolean result = userService.checkPhoneUnique(user);

        assertFalse(result);
    }

    @Test
    public void testCheckEmailUnique_Unique()
    {
        SysUser user = createTestUser();

        when(userMapper.checkEmailUnique("admin@example.com")).thenReturn(null);

        boolean result = userService.checkEmailUnique(user);

        assertTrue(result);
    }

    @Test
    public void testCheckEmailUnique_NotUnique()
    {
        SysUser user = createTestUser();
        SysUser existing = createTestUser();
        existing.setUserId(2L);

        when(userMapper.checkEmailUnique("admin@example.com")).thenReturn(existing);

        boolean result = userService.checkEmailUnique(user);

        assertFalse(result);
    }

    @Test
    public void testCheckUserAllowed()
    {
        SysUser user = createTestUser();

        assertDoesNotThrow(() -> {
            userService.checkUserAllowed(user);
        });
    }

    @Test
    public void testUpdateUserStatus()
    {
        SysUser user = createTestUser();
        user.setStatus("1");

        when(userMapper.updateUser(any(SysUser.class))).thenReturn(1);

        int result = userService.updateUserStatus(user);

        assertEquals(1, result);
    }

    @Test
    public void testUpdateUserProfile()
    {
        SysUser user = createTestUser();
        user.setNickName("新昵称");

        when(userMapper.updateUser(any(SysUser.class))).thenReturn(1);

        int result = userService.updateUserProfile(user);

        assertEquals(1, result);
    }

    @Test
    public void testResetPwd()
    {
        SysUser user = createTestUser();
        user.setPassword("newPassword");

        when(userMapper.updateUser(any(SysUser.class))).thenReturn(1);

        int result = userService.resetPwd(user);

        assertEquals(1, result);
    }

    @Test
    public void testResetUserPwd()
    {
        when(userMapper.resetUserPwd(eq(1L), anyString())).thenReturn(1);

        int result = userService.resetUserPwd(1L, "newPassword");

        assertEquals(1, result);
    }

    @Test
    public void testDeleteUserById()
    {
        when(userMapper.deleteUserById(1L)).thenReturn(1);

        int result = userService.deleteUserById(1L);

        assertEquals(1, result);
    }

    @Test
    public void testDeleteUserByIds()
    {
        Long[] ids = {1L, 2L, 3L};

        when(userMapper.deleteUserByIds(ids)).thenReturn(3);

        int result = userService.deleteUserByIds(ids);

        assertEquals(3, result);
    }

    @Test
    public void testSelectAllocatedList()
    {
        SysUser user = createTestUser();
        List<SysUser> userList = Arrays.asList(user);

        when(userMapper.selectAllocatedList(any(SysUser.class))).thenReturn(userList);

        List<SysUser> result = userService.selectAllocatedList(new SysUser());

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    public void testSelectUnallocatedList()
    {
        SysUser user = createTestUser();
        List<SysUser> userList = Arrays.asList(user);

        when(userMapper.selectUnallocatedList(any(SysUser.class))).thenReturn(userList);

        List<SysUser> result = userService.selectUnallocatedList(new SysUser());

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    public void testSelectUserRoleGroup()
    {
        when(userMapper.selectUserRoleGroup("admin")).thenReturn("管理员,普通用户");

        String result = userService.selectUserRoleGroup("admin");

        assertEquals("管理员,普通用户", result);
    }

    @Test
    public void testSelectUserPostGroup()
    {
        when(userMapper.selectUserPostGroup("admin")).thenReturn("总经理,部门经理");

        String result = userService.selectUserPostGroup("admin");

        assertEquals("总经理,部门经理", result);
    }

    @Test
    public void testUpdateUserAvatar()
    {
        when(userMapper.updateUserAvatar(eq(1L), anyString())).thenReturn(1);

        boolean result = userService.updateUserAvatar(1L, "/avatar/new.png");

        assertTrue(result);
    }

    @Test
    public void testUpdateLoginInfo()
    {
        when(userMapper.updateLoginInfo(eq(1L), anyString(), any())).thenReturn(1);

        userService.updateLoginInfo(1L, "192.168.1.1", new java.util.Date());

        verify(userMapper).updateLoginInfo(eq(1L), anyString(), any());
    }

    @Test
    public void testSelectUserListEmpty()
    {
        when(userMapper.selectUserList(any(SysUser.class))).thenReturn(Arrays.asList());

        List<SysUser> result = userService.selectUserList(new SysUser());

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testSelectUserByUserNameNotFound()
    {
        when(userMapper.selectUserByUserName("nonexistent")).thenReturn(null);

        SysUser result = userService.selectUserByUserName("nonexistent");

        assertNull(result);
    }

    @Test
    public void testSelectUserByIdNotFound()
    {
        when(userMapper.selectUserById(999L)).thenReturn(null);

        SysUser result = userService.selectUserById(999L);

        assertNull(result);
    }

    @Test
    public void testCheckUserNameUniqueWithNullId()
    {
        SysUser user = new SysUser();
        user.setUserName("newuser");

        when(userMapper.checkUserNameUnique("newuser")).thenReturn(null);

        boolean result = userService.checkUserNameUnique(user);

        assertTrue(result);
    }

    @Test
    public void testCheckPhoneUniqueWithNullId()
    {
        SysUser user = new SysUser();
        user.setPhonenumber("13900139000");

        when(userMapper.checkPhoneUnique("13900139000")).thenReturn(null);

        boolean result = userService.checkPhoneUnique(user);

        assertTrue(result);
    }

    @Test
    public void testCheckEmailUniqueWithNullId()
    {
        SysUser user = new SysUser();
        user.setEmail("new@example.com");

        when(userMapper.checkEmailUnique("new@example.com")).thenReturn(null);

        boolean result = userService.checkEmailUnique(user);

        assertTrue(result);
    }
}
