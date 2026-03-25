package com.opc.core.service.impl;

import com.opc.core.domain.CorePackage;
import com.opc.core.mapper.CorePackageMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CorePackageServiceTest
{
    @Mock
    private CorePackageMapper packageMapper;

    @InjectMocks
    private CorePackageServiceImpl packageService;

    private CorePackage createTestPackage()
    {
        CorePackage pkg = new CorePackage();
        pkg.setId(1L);
        pkg.setPackageName("VIP会员套餐");
        pkg.setPackagePrice(new BigDecimal("99.99"));
        pkg.setPackageType(2);
        pkg.setStatus("0");
        pkg.setDescription("VIP会员专属套餐");
        pkg.setImageUrl("/images/vip.png");
        return pkg;
    }

    @Test
    public void testSelectPackageList()
    {
        CorePackage pkg = createTestPackage();
        List<CorePackage> packageList = Arrays.asList(pkg);

        when(packageMapper.selectPackageList(any(CorePackage.class))).thenReturn(packageList);

        List<CorePackage> result = packageService.selectPackageList(new CorePackage());

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("VIP会员套餐", result.get(0).getPackageName());
    }

    @Test
    public void testSelectPackageById()
    {
        CorePackage pkg = createTestPackage();

        when(packageMapper.selectPackageById(1L)).thenReturn(pkg);

        CorePackage result = packageService.selectPackageById(1L);

        assertNotNull(result);
        assertEquals("VIP会员套餐", result.getPackageName());
        assertEquals(new BigDecimal("99.99"), result.getPackagePrice());
    }

    @Test
    public void testInsertPackage()
    {
        CorePackage pkg = createTestPackage();
        pkg.setStatus(null);

        when(packageMapper.insertPackage(any(CorePackage.class))).thenReturn(1);

        int result = packageService.insertPackage(pkg);

        assertEquals(1, result);
        assertEquals("0", pkg.getStatus());
    }

    @Test
    public void testInsertPackageWithStatus()
    {
        CorePackage pkg = createTestPackage();
        pkg.setStatus("1");

        when(packageMapper.insertPackage(any(CorePackage.class))).thenReturn(1);

        int result = packageService.insertPackage(pkg);

        assertEquals(1, result);
        assertEquals("1", pkg.getStatus());
    }

    @Test
    public void testUpdatePackage()
    {
        CorePackage pkg = createTestPackage();
        pkg.setPackageName("超级VIP会员套餐");

        when(packageMapper.updatePackage(any(CorePackage.class))).thenReturn(1);

        int result = packageService.updatePackage(pkg);

        assertEquals(1, result);
    }

    @Test
    public void testDeletePackageById()
    {
        when(packageMapper.deletePackageById(1L)).thenReturn(1);

        int result = packageService.deletePackageById(1L);

        assertEquals(1, result);
    }

    @Test
    public void testDeletePackageByIds()
    {
        Long[] ids = {1L, 2L, 3L};

        when(packageMapper.deletePackageByIds(ids)).thenReturn(3);

        int result = packageService.deletePackageByIds(ids);

        assertEquals(3, result);
    }

    @Test
    public void testSelectPackageListEmpty()
    {
        when(packageMapper.selectPackageList(any(CorePackage.class))).thenReturn(Arrays.asList());

        List<CorePackage> result = packageService.selectPackageList(new CorePackage());

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testSelectPackageByIdNotFound()
    {
        when(packageMapper.selectPackageById(999L)).thenReturn(null);

        CorePackage result = packageService.selectPackageById(999L);

        assertNull(result);
    }

    @Test
    public void testInsertPackageMultiple()
    {
        CorePackage pkg1 = createTestPackage();
        CorePackage pkg2 = createTestPackage();
        pkg2.setId(2L);
        pkg2.setPackageName("普通会员套餐");

        when(packageMapper.insertPackage(any(CorePackage.class))).thenReturn(1);

        int result1 = packageService.insertPackage(pkg1);
        int result2 = packageService.insertPackage(pkg2);

        assertEquals(1, result1);
        assertEquals(1, result2);
    }

    @Test
    public void testUpdatePackageNotFound()
    {
        CorePackage pkg = createTestPackage();
        pkg.setId(999L);

        when(packageMapper.updatePackage(any(CorePackage.class))).thenReturn(0);

        int result = packageService.updatePackage(pkg);

        assertEquals(0, result);
    }

    @Test
    public void testDeletePackageByIdNotFound()
    {
        when(packageMapper.deletePackageById(999L)).thenReturn(0);

        int result = packageService.deletePackageById(999L);

        assertEquals(0, result);
    }

    @Test
    public void testPackageTypeValues()
    {
        CorePackage pkg1 = createTestPackage();
        pkg1.setPackageType(1);
        CorePackage pkg2 = createTestPackage();
        pkg2.setPackageType(2);
        CorePackage pkg3 = createTestPackage();
        pkg3.setPackageType(3);

        when(packageMapper.insertPackage(any(CorePackage.class))).thenReturn(1);

        packageService.insertPackage(pkg1);
        packageService.insertPackage(pkg2);
        packageService.insertPackage(pkg3);

        assertEquals(1, pkg1.getPackageType());
        assertEquals(2, pkg2.getPackageType());
        assertEquals(3, pkg3.getPackageType());
    }

    @Test
    public void testPackagePricePrecision()
    {
        CorePackage pkg = createTestPackage();
        pkg.setPackagePrice(new BigDecimal("999.9999"));

        when(packageMapper.insertPackage(any(CorePackage.class))).thenReturn(1);

        packageService.insertPackage(pkg);

        assertEquals(new BigDecimal("999.9999"), pkg.getPackagePrice());
    }
}
