package com.opc.core.mapper;

import com.opc.core.domain.CoreCommunity;
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
public class CoreCommunityMapperTest
{
    @Mock
    private CoreCommunityMapper communityMapper;

    @Test
    public void testSelectCommunityList()
    {
        CoreCommunity community = new CoreCommunity();
        community.setName("测试社区");

        CoreCommunity resultCommunity = new CoreCommunity();
        resultCommunity.setId(1L);
        resultCommunity.setName("测试社区");

        when(communityMapper.selectCommunityList(any(CoreCommunity.class))).thenReturn(Arrays.asList(resultCommunity));

        List<CoreCommunity> list = communityMapper.selectCommunityList(community);

        assertNotNull(list);
        assertEquals(1, list.size());
        assertEquals("测试社区", list.get(0).getName());
        verify(communityMapper, times(1)).selectCommunityList(community);
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

        List<CoreCommunity> list = communityMapper.selectCommunityList(community);

        assertNotNull(list);
        assertEquals(1, list.size());
        assertEquals("0", list.get(0).getStatus());
        verify(communityMapper, times(1)).selectCommunityList(community);
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

        CoreCommunity result = communityMapper.selectCommunityById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("测试社区", result.getName());
        assertEquals("北京市朝阳区", result.getAddress());
        assertEquals(new BigDecimal("116.397428"), result.getLongitude());
        assertEquals(new BigDecimal("39.90923"), result.getLatitude());
        verify(communityMapper, times(1)).selectCommunityById(1L);
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

        int result = communityMapper.insertCommunity(community);

        assertEquals(1, result);
        verify(communityMapper, times(1)).insertCommunity(community);
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

        int result = communityMapper.updateCommunity(community);

        assertEquals(1, result);
        verify(communityMapper, times(1)).updateCommunity(community);
    }

    @Test
    public void testDeleteCommunityById()
    {
        when(communityMapper.deleteCommunityById(1L)).thenReturn(1);

        int result = communityMapper.deleteCommunityById(1L);

        assertEquals(1, result);
        verify(communityMapper, times(1)).deleteCommunityById(1L);
    }

    @Test
    public void testDeleteCommunityByIds()
    {
        Long[] ids = {1L, 2L, 3L};
        when(communityMapper.deleteCommunityByIds(ids)).thenReturn(3);

        int result = communityMapper.deleteCommunityByIds(ids);

        assertEquals(3, result);
        verify(communityMapper, times(1)).deleteCommunityByIds(ids);
    }

    @Test
    public void testCheckCommunityNameUnique()
    {
        CoreCommunity community = new CoreCommunity();
        community.setId(1L);
        community.setName("已存在的社区");

        when(communityMapper.checkCommunityNameUnique("已存在的社区")).thenReturn(community);

        CoreCommunity result = communityMapper.checkCommunityNameUnique("已存在的社区");

        assertNotNull(result);
        assertEquals("已存在的社区", result.getName());
        verify(communityMapper, times(1)).checkCommunityNameUnique("已存在的社区");
    }

    @Test
    public void testCheckCommunityNameUniqueNotFound()
    {
        when(communityMapper.checkCommunityNameUnique(anyString())).thenReturn(null);

        CoreCommunity result = communityMapper.checkCommunityNameUnique("不存在的社区");

        assertNull(result);
        verify(communityMapper, times(1)).checkCommunityNameUnique("不存在的社区");
    }

    @Test
    public void testSelectCommunityListWithAddressFilter()
    {
        CoreCommunity community = new CoreCommunity();
        community.setAddress("朝阳区");

        CoreCommunity resultCommunity = new CoreCommunity();
        resultCommunity.setId(1L);
        resultCommunity.setName("朝阳社区");
        resultCommunity.setAddress("北京市朝阳区");

        when(communityMapper.selectCommunityList(any(CoreCommunity.class))).thenReturn(Arrays.asList(resultCommunity));

        List<CoreCommunity> list = communityMapper.selectCommunityList(community);

        assertNotNull(list);
        assertEquals(1, list.size());
        assertTrue(list.get(0).getAddress().contains("朝阳区"));
        verify(communityMapper, times(1)).selectCommunityList(community);
    }
}
