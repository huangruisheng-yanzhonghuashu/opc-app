package com.opc.core.domain;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

public class CorePackageOrderTest
{
    @Test
    public void testCorePackageOrderGettersAndSetters()
    {
        CorePackageOrder order = new CorePackageOrder();

        order.setId(1L);
        order.setOrderNo("ORDER123456");
        order.setMemberId(100L);
        order.setEmail("test@example.com");
        order.setThirdPartyAccount("twitter_user");
        order.setNickname("Test User");
        order.setPackageId(10L);
        order.setPackageName("VIP Package");
        order.setPackageType("vip");
        order.setPrice(new BigDecimal("99.99"));
        order.setPayTime(LocalDateTime.of(2024, 1, 1, 12, 0, 0));
        order.setPayStatus("1");

        assertEquals(1L, order.getId());
        assertEquals("ORDER123456", order.getOrderNo());
        assertEquals(100L, order.getMemberId());
        assertEquals("test@example.com", order.getEmail());
        assertEquals("twitter_user", order.getThirdPartyAccount());
        assertEquals("Test User", order.getNickname());
        assertEquals(10L, order.getPackageId());
        assertEquals("VIP Package", order.getPackageName());
        assertEquals("vip", order.getPackageType());
        assertEquals(new BigDecimal("99.99"), order.getPrice());
        assertEquals(LocalDateTime.of(2024, 1, 1, 12, 0, 0), order.getPayTime());
        assertEquals("1", order.getPayStatus());
    }

    @Test
    public void testCorePackageOrderToString()
    {
        CorePackageOrder order = new CorePackageOrder();
        order.setId(1L);
        order.setOrderNo("ORDER123456");
        order.setPayStatus("1");

        String str = order.toString();
        assertNotNull(str);
        assertTrue(str.contains("ORDER123456"));
        assertTrue(str.contains("1"));
    }

    @Test
    public void testCorePackageOrderDefaultValues()
    {
        CorePackageOrder order = new CorePackageOrder();

        assertNull(order.getId());
        assertNull(order.getOrderNo());
        assertNull(order.getMemberId());
        assertNull(order.getEmail());
    }

    @Test
    public void testCorePackageOrderPayStatusValues()
    {
        CorePackageOrder order = new CorePackageOrder();

        order.setPayStatus("0");
        assertEquals("0", order.getPayStatus());

        order.setPayStatus("1");
        assertEquals("1", order.getPayStatus());

        order.setPayStatus("2");
        assertEquals("2", order.getPayStatus());
    }
}
