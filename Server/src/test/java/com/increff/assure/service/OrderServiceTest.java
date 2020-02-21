package com.increff.assure.service;

import com.increff.assure.dao.*;
import com.increff.assure.model.Exception.ApiException;
import com.increff.assure.model.constants.OrderStatusType;
import com.increff.assure.pojo.*;
import com.increff.assure.util.AbstractUnitTest;
import com.increff.assure.util.dataUtil;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class OrderServiceTest extends AbstractUnitTest {
    @Autowired
    private OrderService service;

    @Autowired
    private OrderDao dao;

    @Autowired
    private OrderItemDao itemDao;

    @Autowired
    private BinSkuDao binSkuDao;

    @Autowired
    private BinDao binDao;

    @Autowired
    private InventoryDao inventoryDao;

    private OrderPojo order;
    private List<OrderItemPojo> items;
    private BinPojo binPojo;
    private BinSkuPojo binSkuPojo;
    private InventoryPojo inventory;

    @Before
    public void init() {
        items=new ArrayList<>();
        order = dataUtil.createOrderPojo(OrderStatusType.CREATED, new Long(101), "channelOrderId", new Long(1), new Long(2));
        OrderItemPojo item = dataUtil.createOrderItem(new Long(101), new Long(1000), new Long(1), 12.32, new Long(0), new Long(0));
        items.add(item);
        item = dataUtil.createOrderItem(new Long(102), new Long(1001), new Long(2), 9099.32, new Long(0), new Long(0));
        items.add(item);
        inventory=dataUtil.createInventoryPojo(items.get(0).getGlobalSkuId(),items.get(0).getOrderedQuantity()+1);
        binPojo=new BinPojo();
        binDao.insert(binPojo);
        binSkuPojo =dataUtil.createBinSkuPojo(items.get(0).getOrderedQuantity()+1,items.get(0).getGlobalSkuId(),binPojo.getBinId());
        inventory.setAllocatedQuantity(new Long(0));
    }

    @Test
    public void testPlaceOrder() {
        service.placeOrder(order, items);
        assertEquals(1, dao.selectAll().size());
        assertEquals(items.size(), itemDao.selectAll().size());
        OrderPojo returnedOrder = dao.selectAll().get(0);
        assertEquals(returnedOrder.getChannelId(), order.getChannelId());
        assertEquals(returnedOrder.getChannelOrderId(), order.getChannelOrderId());
        assertEquals(returnedOrder.getClientId(), order.getClientId());
        assertEquals(returnedOrder.getCustomerId(), order.getCustomerId());
        List<OrderItemPojo> itemList = itemDao.selectAll();
        OrderItemPojo returnedItem = itemList.get(0);
        OrderItemPojo item = items.get(0);
        assertEquals(item.getGlobalSkuId(), returnedItem.getGlobalSkuId());
        assertEquals(item.getOrderedQuantity(), returnedItem.getOrderedQuantity());
        assertEquals(item.getOrderId(), returnedItem.getOrderId());
        assertEquals(item.getSellingPricePerUnit(), returnedItem.getSellingPricePerUnit());
        assertEquals(item.getAllocatedQuantity(), returnedItem.getAllocatedQuantity());
        returnedItem = itemList.get(1);
        item = items.get(1);
        assertEquals(item.getGlobalSkuId(), returnedItem.getGlobalSkuId());
        assertEquals(item.getOrderedQuantity(), returnedItem.getOrderedQuantity());
        assertEquals(item.getOrderId(), returnedItem.getOrderId());
        assertEquals(item.getSellingPricePerUnit(), returnedItem.getSellingPricePerUnit());
        assertEquals(item.getAllocatedQuantity(), returnedItem.getAllocatedQuantity());
    }

    @Test
    public void testHalfAllocate() throws ApiException {
        dao.insert(order);
        items.get(0).setOrderId(order.getId());
        items.get(0).setOrderedQuantity(new Long(10000));
        inventoryDao.insert(inventory);
        binSkuDao.insert(binSkuPojo);
        itemDao.insert(items.get(0));
        service.allocateOrder(order.getId());
        assertEquals(OrderStatusType.CREATED,dao.selectAll().get(0).getStatus());
        assertEquals(inventory.getAllocatedQuantity(),items.get(0).getAllocatedQuantity());
    }

    @Test
    public void testFullAllocated() throws ApiException {
        dao.insert(order);
        items.get(0).setOrderId(order.getId());
        inventoryDao.insert(inventory);
        binSkuDao.insert(binSkuPojo);
        itemDao.insert(items.get(0));
        service.allocateOrder(order.getId());
        assertEquals(OrderStatusType.CREATED,dao.selectAll().get(0).getStatus());
        assertEquals(items.get(0).getOrderedQuantity(),items.get(0).getAllocatedQuantity());
    }







}
