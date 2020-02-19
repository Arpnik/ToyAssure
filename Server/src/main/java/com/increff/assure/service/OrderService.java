package com.increff.assure.service;

import com.increff.assure.dao.OrderDao;
import com.increff.assure.dao.OrderItemDao;
import com.increff.assure.model.Exception.ApiException;
import com.increff.assure.model.Pojo.OrderDetailsResult;
import com.increff.assure.model.constants.OrderStatusType;
import com.increff.assure.pojo.BinSkuPojo;
import com.increff.assure.pojo.InventoryPojo;
import com.increff.assure.pojo.OrderItemPojo;
import com.increff.assure.pojo.OrderPojo;
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

        validate(orderId);
        List<OrderItemPojo> itemList = itemDao.getOrderedItems(orderId);
        boolean allocated = true;
        for(OrderItemPojo item: itemList)
        {
            InventoryPojo inventory = invService.getByGlobalSkuId(item.getGlobalSkuId());
            long allocatedQuantity = Math.min( inventory.getAvailableQuantity(),item.getOrderedQuantity() - item.getAllocatedQuantity());
            inventory.setAvailableQuantity( inventory.getAvailableQuantity() - allocatedQuantity);
            inventory.setAllocatedQuantity( inventory.getAllocatedQuantity() + allocatedQuantity);
            item.setAllocatedQuantity(item.getAllocatedQuantity() + allocatedQuantity);

            List<BinSkuPojo> binList = binService.getBins( item.getGlobalSkuId());
            allocateItems( binList, allocatedQuantity);

            if(allocated && item.getOrderedQuantity() != item.getAllocatedQuantity())
                allocated=false;

        }

        if(allocated)
        {
            OrderPojo order=dao.select(orderId);
            if(order.getStatus()==OrderStatusType.CREATED)
                order.setStatus(OrderStatusType.ALLOCATED);
        }
    }

    @Transactional(readOnly = true)
    public List<OrderDetailsResult> getOrderDetails(long orderId)
    {
        return itemDao.getDetailsByOrderId(orderId);

    }

    @Transactional(rollbackFor = ApiException.class)
    public OrderPojo fullFillOrder(long orderId) throws ApiException {
        OrderPojo order = getOrder(orderId);
        if(order.getStatus() == OrderStatusType.FULFILLED)
            return order;

        updateStatus(order);

        List<OrderItemPojo> itemList = itemDao.getOrderedItems(orderId);
        for(OrderItemPojo item: itemList)
        {
            InventoryPojo inventory = invService.getByGlobalSkuId(item.getGlobalSkuId());
            inventory.setAllocatedQuantity( inventory.getAllocatedQuantity() - item.getOrderedQuantity());
            inventory.setFulfilledQuantity( inventory.getFulfilledQuantity() + item.getOrderedQuantity());

            item.setAllocatedQuantity(0);
            item.setFulfilledQuantity( item.getOrderedQuantity());
        }

        return order;

    }

    @Transactional(rollbackFor = ApiException.class)
    public void updateStatus(OrderPojo order) throws ApiException {

        if(order.getStatus() == OrderStatusType.CREATED)
            throw new ApiException("Order cannot be fulfilled since it is not allocated");

        order.setStatus(OrderStatusType.FULFILLED);
    }

    @Transactional(readOnly = true)
    protected void validate(long orderId) throws ApiException {
        OrderPojo pojo=dao.select(orderId);

        if(pojo == null)
            throw new ApiException("Order doesn't exist");

        if(pojo.getStatus() != OrderStatusType.CREATED)
            throw new ApiException("Order is already allocated");
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

    @Transactional(readOnly = true)
    public OrderPojo getOrder(long orderId) throws ApiException {
        OrderPojo pojo = dao.select(orderId);

        if(pojo == null)
        {
            throw new ApiException("Order doesn't exist");
        }
        return pojo;
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
