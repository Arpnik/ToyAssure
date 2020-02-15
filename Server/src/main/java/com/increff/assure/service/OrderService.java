package com.increff.assure.service;

import com.increff.assure.dao.BinSkuDao;
import com.increff.assure.dao.OrderDao;
import com.increff.assure.dao.OrderItemDao;
import com.increff.assure.model.constants.OrderStatusType;
import com.increff.assure.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderService {
    @Autowired
    private OrderDao dao;

    @Autowired
    private OrderItemDao itemDao;

    @Autowired
    private InventoryService invService;

    @Autowired
    private BinService binService;

    @Transactional
    public void placeOrder(OrderPojo orderPojo, List<OrderItemPojo> itemList)
    {
        dao.insert(orderPojo);
        for(OrderItemPojo item:itemList)
        {
            item.setOrderId(orderPojo.getId());
            itemDao.insert(item);
        }

    }

    @Transactional(rollbackFor = ApiException.class)
    public void allocateOrder(long orderId) throws ApiException {
        List<OrderItemPojo> itemList = itemDao.getOrderedItems(orderId);

        if(itemList.size()==0)
            throw new ApiException("Order Doesn't exist");

        boolean allocated = true;
        for(OrderItemPojo item: itemList)
        {
            InventoryPojo inventory = invService.getByGlobalSkuId(item.getGlobalSkuId());
            long allocatedQuantity = Math.min( inventory.getAvailableQuantity(), item.getOrderedQuantity());
            inventory.setAvailableQuantity( inventory.getAvailableQuantity() - allocatedQuantity);
            inventory.setAllocatedQuantity( inventory.getAllocatedQuantity() + allocatedQuantity);
            item.setAllocatedQuantity(allocatedQuantity);

            List<BinSkuPojo> binList = binService.getBins( item.getGlobalSkuId());
            allocateItems( binList, allocatedQuantity);

            if(allocated && allocatedQuantity != item.getOrderedQuantity())
                allocated=false;

        }

        if(allocated)
        {
            OrderPojo order=dao.select(orderId);
            order.setStatus(OrderStatusType.ALLOCATED);
        }
    }

    @Transactional(readOnly = true)
    public List<OrderDetailsResult> getOrderDetails( long orderId)
    {
        return itemDao.getDetailsByOrderId( orderId);
    }

    @Transactional(readOnly = true)
    public List<OrderPojo> getAll()
    {
        return dao.selectAll();
    }

    @Transactional(readOnly = true)
    public OrderPojo getByParams( long channelId, String channelOrderId)
    {
        return dao.getBychannelIdAndChannelOrderId( channelId, channelOrderId);
    }

    private void allocateItems( List<BinSkuPojo> bins, long qty)
    {
        for(BinSkuPojo bin: bins)
        {
            if( bin.getQuantity() > qty)
            {
                bin.setQuantity( bin.getQuantity() - qty);
                return;
            }

            qty -= bin.getQuantity();
            bin.setQuantity(0);
        }
    }

}
