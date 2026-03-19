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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CorePackageServiceImplTest
{
    @Mock
    private CorePackageMapper packageMapper;

    @InjectMocks
    private CorePackageServiceImpl packageService;

    @Test
    public void testSelectPackageList()
    {
        CorePackage pkg = new CorePackage();
        pkg.setPackageName("VIP Package");

        CorePackage resultPkg = new CorePackage();
        resultPkg.setId(1L);
        resultPkg.setPackageName("VIP Package");

        when(packageMapper.selectPackageList(any(CorePackage.class))).thenReturn(Arrays.asList(resultPkg));

        List<CorePackage> list = packageService.selectPackageList(pkg);

        assertNotNull(list);
        assertEquals(1, list.size());
        assertEquals("VIP Package", list.get(0).getPackageName());
    }

    @Test
    public void testSelectPackageById()
    {
        CorePackage pkg = new CorePackage();
        pkg.setId(1L);
        pkg.setPackageName("VIP Package");

        when(packageMapper.selectPackageById(1L)).thenReturn(pkg);

        CorePackage result = packageService.selectPackageById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("VIP Package", result.getPackageName());
    }

    @Test
    public void testInsertPackage_WithDefaultStatus()
    {
        CorePackage pkg = new CorePackage();
        pkg.setPackageName("New Package");
        pkg.setPackagePrice(new BigDecimal("99.99"));

        when(packageMapper.insertPackage(any(CorePackage.class))).thenReturn(1);

        int result = packageService.insertPackage(pkg);

        assertEquals(1, result);
        assertEquals("0", pkg.getStatus());
    }

    @Test
    public void testInsertPackage_WithCustomStatus()
    {
        CorePackage pkg = new CorePackage();
        pkg.setPackageName("New Package");
        pkg.setStatus("1");

        when(packageMapper.insertPackage(any(CorePackage.class))).thenReturn(1);

        int result = packageService.insertPackage(pkg);

        assertEquals(1, result);
        assertEquals("1", pkg.getStatus());
    }

    @Test
    public void testUpdatePackage()
    {
        CorePackage pkg = new CorePackage();
        pkg.setId(1L);
        pkg.setPackageName("Updated Package");

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
}
