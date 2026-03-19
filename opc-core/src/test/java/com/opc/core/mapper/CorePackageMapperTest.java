package com.opc.core.mapper;

import com.opc.core.domain.CorePackage;
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
public class CorePackageMapperTest
{
    @Mock
    private CorePackageMapper packageMapper;

    @Test
    public void testSelectPackageList()
    {
        CorePackage pkg = new CorePackage();
        pkg.setPackageName("VIP Package");

        CorePackage resultPkg = new CorePackage();
        resultPkg.setId(1L);
        resultPkg.setPackageName("VIP Package");

        when(packageMapper.selectPackageList(any(CorePackage.class))).thenReturn(Arrays.asList(resultPkg));

        List<CorePackage> list = packageMapper.selectPackageList(pkg);

        assertNotNull(list);
        assertEquals(1, list.size());
        assertEquals("VIP Package", list.get(0).getPackageName());
        verify(packageMapper, times(1)).selectPackageList(pkg);
    }

    @Test
    public void testSelectPackageById()
    {
        CorePackage pkg = new CorePackage();
        pkg.setId(1L);
        pkg.setPackageName("VIP Package");

        when(packageMapper.selectPackageById(1L)).thenReturn(pkg);

        CorePackage result = packageMapper.selectPackageById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("VIP Package", result.getPackageName());
        verify(packageMapper, times(1)).selectPackageById(1L);
    }

    @Test
    public void testInsertPackage()
    {
        CorePackage pkg = new CorePackage();
        pkg.setPackageName("New Package");
        pkg.setPackagePrice(new BigDecimal("99.99"));

        when(packageMapper.insertPackage(any(CorePackage.class))).thenReturn(1);

        int result = packageMapper.insertPackage(pkg);

        assertEquals(1, result);
        verify(packageMapper, times(1)).insertPackage(pkg);
    }

    @Test
    public void testUpdatePackage()
    {
        CorePackage pkg = new CorePackage();
        pkg.setId(1L);
        pkg.setPackageName("Updated Package");

        when(packageMapper.updatePackage(any(CorePackage.class))).thenReturn(1);

        int result = packageMapper.updatePackage(pkg);

        assertEquals(1, result);
        verify(packageMapper, times(1)).updatePackage(pkg);
    }

    @Test
    public void testDeletePackageById()
    {
        when(packageMapper.deletePackageById(1L)).thenReturn(1);

        int result = packageMapper.deletePackageById(1L);

        assertEquals(1, result);
        verify(packageMapper, times(1)).deletePackageById(1L);
    }

    @Test
    public void testDeletePackageByIds()
    {
        Long[] ids = {1L, 2L, 3L};
        when(packageMapper.deletePackageByIds(ids)).thenReturn(3);

        int result = packageMapper.deletePackageByIds(ids);

        assertEquals(3, result);
        verify(packageMapper, times(1)).deletePackageByIds(ids);
    }
}
