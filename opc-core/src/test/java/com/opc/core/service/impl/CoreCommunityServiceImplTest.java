package com.opc.core.service.impl;

import com.opc.core.domain.CoreCommunity;
import com.opc.core.mapper.CoreCommunityMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CoreCommunityServiceImplTest
{
    @Mock
    private CoreCommunityMapper communityMapper;

    @InjectMocks
    private CoreCommunityServiceImpl communityService;

    @Test
    public void testSelectCommunityList()
    {
        CoreCommunity community = new CoreCommunity();
        community.setName("测试社区");

        CoreCommunity resultCommunity = new CoreCommunity();
        resultCommunity.setId(1L);
        resultCommunity.setName("测试社区");

        when(communityMapper.selectCommunityList(any(CoreCommunity.class))).thenReturn(Arrays.asList(resultCommunity));

        List<CoreCommunity> list = communityService.selectCommunityList(community);

        assertNotNull(list);
        assertEquals(1, list.size());
        assertEquals("测试社区", list.get(0).getName());
    }

    @Test
    public void testSelectCommunityListEmpty()
    {
        CoreCommunity community = new CoreCommunity();
        community.setName("不存在的社区");

        when(communityMapper.selectCommunityList(any(CoreCommunity.class))).thenReturn(Arrays.asList());

        List<CoreCommunity> list = communityService.selectCommunityList(community);

        assertNotNull(list);
        assertEquals(0, list.size());
    }

    @Test
    public void testSelectCommunityById()
    {
        CoreCommunity community = new CoreCommunity();
        community.setId(1L);
        community.setName("测试社区");
        community.setAddress("北京市朝阳区");
        community.setLongitude(new BigDecimal("116.397428"));
        community.setLatitude(new BigDecimal("39.90923"));

        when(communityMapper.selectCommunityById(1L)).thenReturn(community);

        CoreCommunity result = communityService.selectCommunityById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("测试社区", result.getName());
        assertEquals("北京市朝阳区", result.getAddress());
        assertEquals(new BigDecimal("116.397428"), result.getLongitude());
        assertEquals(new BigDecimal("39.90923"), result.getLatitude());
    }

    @Test
    public void testSelectCommunityByIdNotFound()
    {
        when(communityMapper.selectCommunityById(999L)).thenReturn(null);

        CoreCommunity result = communityService.selectCommunityById(999L);

        assertNull(result);
    }

    @Test
    public void testInsertCommunity()
    {
        CoreCommunity community = new CoreCommunity();
        community.setName("新社区");
        community.setAddress("上海市浦东新区");
        community.setLongitude(new BigDecimal("121.473667"));
        community.setLatitude(new BigDecimal("31.230416"));
        community.setWantToGoCount(0);
        community.setVisitedCount(0);
        community.setReviewCount(0);
        community.setRating(new BigDecimal("5.0"));
        community.setStatus("0");
        community.setSortOrder(1);

        when(communityMapper.insertCommunity(any(CoreCommunity.class))).thenReturn(1);

        int result = communityService.insertCommunity(community);

        assertEquals(1, result);
    }

    @Test
    public void testUpdateCommunity()
    {
        CoreCommunity community = new CoreCommunity();
        community.setId(1L);
        community.setName("更新后的社区名称");
        community.setAddress("更新后的地址");
        community.setWantToGoCount(100);
        community.setVisitedCount(50);
        community.setRating(new BigDecimal("4.5"));

        when(communityMapper.updateCommunity(any(CoreCommunity.class))).thenReturn(1);

        int result = communityService.updateCommunity(community);

        assertEquals(1, result);
    }

    @Test
    public void testDeleteCommunityById()
    {
        when(communityMapper.deleteCommunityById(1L)).thenReturn(1);

        int result = communityService.deleteCommunityById(1L);

        assertEquals(1, result);
    }

    @Test
    public void testDeleteCommunityByIds()
    {
        Long[] ids = {1L, 2L, 3L};
        when(communityMapper.deleteCommunityByIds(ids)).thenReturn(3);

        int result = communityService.deleteCommunityByIds(ids);

        assertEquals(3, result);
    }

    @Test
    public void testCheckCommunityNameUnique_WhenNameExists()
    {
        CoreCommunity existingCommunity = new CoreCommunity();
        existingCommunity.setId(1L);
        existingCommunity.setName("已存在的社区");

        CoreCommunity newCommunity = new CoreCommunity();
        newCommunity.setId(2L);
        newCommunity.setName("已存在的社区");

        when(communityMapper.checkCommunityNameUnique("已存在的社区")).thenReturn(existingCommunity);

        boolean result = communityService.checkCommunityNameUnique(newCommunity);

        assertFalse(result);
    }

    @Test
    public void testCheckCommunityNameUnique_WhenNameNotExists()
    {
        CoreCommunity newCommunity = new CoreCommunity();
        newCommunity.setId(2L);
        newCommunity.setName("新社区");

        when(communityMapper.checkCommunityNameUnique("新社区")).thenReturn(null);

        boolean result = communityService.checkCommunityNameUnique(newCommunity);

        assertTrue(result);
    }

    @Test
    public void testCheckCommunityNameUnique_SameId()
    {
        CoreCommunity community = new CoreCommunity();
        community.setId(1L);
        community.setName("测试社区");

        when(communityMapper.checkCommunityNameUnique("测试社区")).thenReturn(community);

        boolean result = communityService.checkCommunityNameUnique(community);

        assertTrue(result);
    }

    @Test
    public void testCheckCommunityNameUnique_NullId()
    {
        CoreCommunity community = new CoreCommunity();
        community.setName("新社区");

        when(communityMapper.checkCommunityNameUnique("新社区")).thenReturn(null);

        boolean result = communityService.checkCommunityNameUnique(community);

        assertTrue(result);
    }

    @Test
    public void testSelectCommunityListWithStatus()
    {
        CoreCommunity community = new CoreCommunity();
        community.setStatus("0");

        CoreCommunity resultCommunity = new CoreCommunity();
        resultCommunity.setId(1L);
        resultCommunity.setName("正常社区");
        resultCommunity.setStatus("0");

        when(communityMapper.selectCommunityList(any(CoreCommunity.class))).thenReturn(Arrays.asList(resultCommunity));

        List<CoreCommunity> list = communityService.selectCommunityList(community);

        assertNotNull(list);
        assertEquals(1, list.size());
        assertEquals("0", list.get(0).getStatus());
    }

    @Test
    public void testSelectCommunityListWithAddress()
    {
        CoreCommunity community = new CoreCommunity();
        community.setAddress("朝阳区");

        CoreCommunity resultCommunity = new CoreCommunity();
        resultCommunity.setId(1L);
        resultCommunity.setName("朝阳社区");
        resultCommunity.setAddress("北京市朝阳区");

        when(communityMapper.selectCommunityList(any(CoreCommunity.class))).thenReturn(Arrays.asList(resultCommunity));

        List<CoreCommunity> list = communityService.selectCommunityList(community);

        assertNotNull(list);
        assertEquals(1, list.size());
        assertTrue(list.get(0).getAddress().contains("朝阳区"));
    }
}
