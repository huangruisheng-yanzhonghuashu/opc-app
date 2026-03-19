package com.opc.core.domain;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

public class CorePackageTest
{
    @Test
    public void testCorePackageGettersAndSetters()
    {
        CorePackage pkg = new CorePackage();

        pkg.setId(1L);
        pkg.setPackageName("VIP Package");
        pkg.setPackagePrice(new BigDecimal("99.99"));
        pkg.setPackageType("vip");
        pkg.setDescription("VIP membership package");
        pkg.setStatus("0");

        assertEquals(1L, pkg.getId());
        assertEquals("VIP Package", pkg.getPackageName());
        assertEquals(new BigDecimal("99.99"), pkg.getPackagePrice());
        assertEquals("vip", pkg.getPackageType());
        assertEquals("VIP membership package", pkg.getDescription());
        assertEquals("0", pkg.getStatus());
    }

    @Test
    public void testCorePackageToString()
    {
        CorePackage pkg = new CorePackage();
        pkg.setId(1L);
        pkg.setPackageName("VIP Package");
        pkg.setPackageType("vip");

        String str = pkg.toString();
        assertNotNull(str);
        assertTrue(str.contains("VIP Package"));
        assertTrue(str.contains("vip"));
    }

    @Test
    public void testCorePackageDefaultValues()
    {
        CorePackage pkg = new CorePackage();

        assertNull(pkg.getId());
        assertNull(pkg.getPackageName());
        assertNull(pkg.getPackagePrice());
        assertNull(pkg.getPackageType());
        assertNull(pkg.getDescription());
        assertNull(pkg.getStatus());
    }

    @Test
    public void testCorePackageStatusValues()
    {
        CorePackage pkg = new CorePackage();

        pkg.setStatus("0");
        assertEquals("0", pkg.getStatus());

        pkg.setStatus("1");
        assertEquals("1", pkg.getStatus());
    }

    @Test
    public void testCorePackageTypeValues()
    {
        CorePackage pkg = new CorePackage();

        pkg.setPackageType("normal");
        assertEquals("normal", pkg.getPackageType());

        pkg.setPackageType("vip");
        assertEquals("vip", pkg.getPackageType());

        pkg.setPackageType("svip");
        assertEquals("svip", pkg.getPackageType());
    }
}
