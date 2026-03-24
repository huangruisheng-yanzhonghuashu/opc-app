package com.opc.core.mapper;

import com.opc.core.domain.CorePackageOrder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
public class CorePackageOrderMapperTest
{
    @Mock
    private CorePackageOrderMapper orderMapper;

    @Test
    public void testSelectOrderList()
    {
        CorePackageOrder order = new CorePackageOrder();
        order.setOrderNo("ORDER123");

        CorePackageOrder resultOrder = new CorePackageOrder();
        resultOrder.setId(1L);
        resultOrder.setOrderNo("ORDER123");

        when(orderMapper.selectOrderList(any(CorePackageOrder.class))).thenReturn(Arrays.asList(resultOrder));

        List<CorePackageOrder> list = orderMapper.selectOrderList(order);

        assertNotNull(list);
        assertEquals(1, list.size());
        assertEquals("ORDER123", list.get(0).getOrderNo());
        verify(orderMapper, times(1)).selectOrderList(order);
    }

    @Test
    public void testSelectOrderById()
    {
        CorePackageOrder order = new CorePackageOrder();
        order.setId(1L);
        order.setOrderNo("ORDER123");

        when(orderMapper.selectOrderById(1L)).thenReturn(order);

        CorePackageOrder result = orderMapper.selectOrderById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("ORDER123", result.getOrderNo());
        verify(orderMapper, times(1)).selectOrderById(1L);
    }

    @Test
    public void testSelectOrderByNo()
    {
        CorePackageOrder order = new CorePackageOrder();
        order.setId(1L);
        order.setOrderNo("ORDER123");

        when(orderMapper.selectOrderByNo("ORDER123")).thenReturn(order);

        CorePackageOrder result = orderMapper.selectOrderByNo("ORDER123");

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("ORDER123", result.getOrderNo());
        verify(orderMapper, times(1)).selectOrderByNo("ORDER123");
    }

    @Test
    public void testInsertOrder()
    {
        CorePackageOrder order = new CorePackageOrder();
        order.setOrderNo("ORDER456");
        order.setPrice(new BigDecimal("99.99"));

        when(orderMapper.insertOrder(any(CorePackageOrder.class))).thenReturn(1);

        int result = orderMapper.insertOrder(order);

        assertEquals(1, result);
        verify(orderMapper, times(1)).insertOrder(order);
    }

    @Test
    public void testUpdateOrder()
    {
        CorePackageOrder order = new CorePackageOrder();
        order.setId(1L);
        order.setOrderNo("ORDER123");

        when(orderMapper.updateOrder(any(CorePackageOrder.class))).thenReturn(1);

        int result = orderMapper.updateOrder(order);

        assertEquals(1, result);
        verify(orderMapper, times(1)).updateOrder(order);
    }

    @Test
    public void testDeleteOrderById()
    {
        when(orderMapper.deleteOrderById(1L)).thenReturn(1);

        int result = orderMapper.deleteOrderById(1L);

        assertEquals(1, result);
        verify(orderMapper, times(1)).deleteOrderById(1L);
    }

    @Test
    public void testDeleteOrderByIds()
    {
        Long[] ids = {1L, 2L, 3L};
        when(orderMapper.deleteOrderByIds(ids)).thenReturn(3);

        int result = orderMapper.deleteOrderByIds(ids);

        assertEquals(3, result);
        verify(orderMapper, times(1)).deleteOrderByIds(ids);
    }
}
