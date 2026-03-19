package com.opc.core.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.opc.core.domain.CorePackageOrder;
import com.opc.core.mapper.CorePackageOrderMapper;
import com.opc.core.service.ICorePackageOrderService;

@Service
public class CorePackageOrderServiceImpl implements ICorePackageOrderService
{
    @Autowired
    private CorePackageOrderMapper orderMapper;

    @Override
    public List<CorePackageOrder> selectOrderList(CorePackageOrder order)
    {
        return orderMapper.selectOrderList(order);
    }

    @Override
    public CorePackageOrder selectOrderById(Long id)
    {
        return orderMapper.selectOrderById(id);
    }

    @Override
    public CorePackageOrder selectOrderByNo(String orderNo)
    {
        return orderMapper.selectOrderByNo(orderNo);
    }

    @Override
    public int insertOrder(CorePackageOrder order)
    {
        if (order.getPayStatus() == null || order.getPayStatus().isEmpty()) {
            order.setPayStatus("0");
        }
        return orderMapper.insertOrder(order);
    }

    @Override
    public int updateOrder(CorePackageOrder order)
    {
        return orderMapper.updateOrder(order);
    }

    @Override
    public int deleteOrderById(Long id)
    {
        return orderMapper.deleteOrderById(id);
    }

    @Override
    public int deleteOrderByIds(Long[] ids)
    {
        return orderMapper.deleteOrderByIds(ids);
    }
}
