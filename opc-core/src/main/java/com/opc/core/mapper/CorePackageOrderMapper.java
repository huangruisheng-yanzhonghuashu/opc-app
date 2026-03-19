package com.opc.core.mapper;

import java.util.List;
import com.opc.core.domain.CorePackageOrder;

public interface CorePackageOrderMapper
{
    public List<CorePackageOrder> selectOrderList(CorePackageOrder order);

    public CorePackageOrder selectOrderById(Long id);

    public CorePackageOrder selectOrderByNo(String orderNo);

    public int insertOrder(CorePackageOrder order);

    public int updateOrder(CorePackageOrder order);

    public int deleteOrderById(Long id);

    public int deleteOrderByIds(Long[] ids);
}
