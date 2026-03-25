package com.opc.system.service.impl;

import com.opc.common.constant.UserConstants;
import com.opc.common.core.domain.entity.SysRole;
import com.opc.common.core.domain.entity.SysUser;
import com.opc.system.domain.SysPost;
import com.opc.system.mapper.SysPostMapper;
import com.opc.system.mapper.SysRoleMapper;
import com.opc.system.mapper.SysUserMapper;
import com.opc.system.mapper.SysUserPostMapper;
import com.opc.system.mapper.SysUserRoleMapper;
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

    @Mock
    private SysRoleMapper roleMapper;

    @Mock
    private SysPostMapper postMapper;

    @Mock
    private SysUserRoleMapper userRoleMapper;

    @Mock
    private SysUserPostMapper userPostMapper;

    @InjectMocks
    private SysUserServiceImpl userService;

    private SysUser createTestUser()
    {
        SysUser user = new SysUser();
        user.setUserId(2L);
        user.setUserName("testuser");
        user.setNickName("测试用户");
        user.setEmail("test@example.com");
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
        assertEquals("testuser", result.get(0).getUserName());
    }

    @Test
    public void testSelectUserByUserName()
    {
        SysUser user = createTestUser();

        when(userMapper.selectUserByUserName("testuser")).thenReturn(user);

        SysUser result = userService.selectUserByUserName("testuser");

        assertNotNull(result);
        assertEquals("testuser", result.getUserName());
    }

    @Test
    public void testSelectUserById()
    {
        SysUser user = createTestUser();

        when(userMapper.selectUserById(2L)).thenReturn(user);

        SysUser result = userService.selectUserById(2L);

        assertNotNull(result);
        assertEquals(2L, result.getUserId());
    }

    @Test
    public void testCheckUserNameUnique_Unique()
    {
        SysUser user = createTestUser();

        when(userMapper.checkUserNameUnique("testuser")).thenReturn(null);

        boolean result = userService.checkUserNameUnique(user);

        assertTrue(result);
    }

    @Test
    public void testCheckUserNameUnique_NotUnique()
    {
        SysUser user = createTestUser();
        SysUser existing = createTestUser();
        existing.setUserId(3L);

        when(userMapper.checkUserNameUnique("testuser")).thenReturn(existing);

        boolean result = userService.checkUserNameUnique(user);

        assertFalse(result);
    }

    @Test
    public void testCheckUserNameUnique_SameId()
    {
        SysUser user = createTestUser();
        SysUser existing = createTestUser();

        when(userMapper.checkUserNameUnique("testuser")).thenReturn(existing);

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
        existing.setUserId(3L);

        when(userMapper.checkPhoneUnique("13800138000")).thenReturn(existing);

        boolean result = userService.checkPhoneUnique(user);

        assertFalse(result);
    }

    @Test
    public void testCheckEmailUnique_Unique()
    {
        SysUser user = createTestUser();

        when(userMapper.checkEmailUnique("test@example.com")).thenReturn(null);

        boolean result = userService.checkEmailUnique(user);

        assertTrue(result);
    }

    @Test
    public void testCheckEmailUnique_NotUnique()
    {
        SysUser user = createTestUser();
        SysUser existing = createTestUser();
        existing.setUserId(3L);

        when(userMapper.checkEmailUnique("test@example.com")).thenReturn(existing);

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

        when(userMapper.updateUserStatus(anyLong(), anyString())).thenReturn(1);

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
        when(userMapper.resetUserPwd(eq(2L), anyString())).thenReturn(1);

        int result = userService.resetUserPwd(2L, "newPassword");

        assertEquals(1, result);
    }

    @Test
    public void testResetUserPwd()
    {
        when(userMapper.resetUserPwd(eq(2L), anyString())).thenReturn(1);

        int result = userService.resetUserPwd(2L, "newPassword");

        assertEquals(1, result);
    }

    @Test
    public void testDeleteUserById()
    {
        when(userRoleMapper.deleteUserRoleByUserId(2L)).thenReturn(1);
        when(userPostMapper.deleteUserPostByUserId(2L)).thenReturn(1);
        when(userMapper.deleteUserById(2L)).thenReturn(1);

        int result = userService.deleteUserById(2L);

        assertEquals(1, result);
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
        SysRole role1 = new SysRole();
        role1.setRoleId(1L);
        role1.setRoleName("管理员");
        SysRole role2 = new SysRole();
        role2.setRoleId(2L);
        role2.setRoleName("普通用户");

        when(roleMapper.selectRolesByUserName("testuser")).thenReturn(Arrays.asList(role1, role2));

        String result = userService.selectUserRoleGroup("testuser");

        assertEquals("管理员,普通用户", result);
    }

    @Test
    public void testSelectUserRoleGroupEmpty()
    {
        when(roleMapper.selectRolesByUserName("testuser")).thenReturn(Arrays.asList());

        String result = userService.selectUserRoleGroup("testuser");

        assertEquals("", result);
    }

    @Test
    public void testSelectUserPostGroup()
    {
        SysPost post1 = new SysPost();
        post1.setPostId(1L);
        post1.setPostName("总经理");
        SysPost post2 = new SysPost();
        post2.setPostId(2L);
        post2.setPostName("部门经理");

        when(postMapper.selectPostsByUserName("testuser")).thenReturn(Arrays.asList(post1, post2));

        String result = userService.selectUserPostGroup("testuser");

        assertEquals("总经理,部门经理", result);
    }

    @Test
    public void testSelectUserPostGroupEmpty()
    {
        when(postMapper.selectPostsByUserName("testuser")).thenReturn(Arrays.asList());

        String result = userService.selectUserPostGroup("testuser");

        assertEquals("", result);
    }

    @Test
    public void testUpdateUserAvatar()
    {
        when(userMapper.updateUserAvatar(eq(2L), anyString())).thenReturn(1);

        boolean result = userService.updateUserAvatar(2L, "/avatar/new.png");

        assertTrue(result);
    }

    @Test
    public void testUpdateLoginInfo()
    {
        when(userMapper.updateLoginInfo(eq(2L), anyString(), any())).thenReturn(1);

        userService.updateLoginInfo(2L, "192.168.1.1", new java.util.Date());

        verify(userMapper).updateLoginInfo(eq(2L), anyString(), any());
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
