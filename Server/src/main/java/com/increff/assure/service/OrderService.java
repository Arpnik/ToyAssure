package com.increff.assure.service;

import com.increff.assure.dao.OrderDao;
import com.increff.assure.dao.OrderItemDao;
import com.increff.assure.pojo.OrderItemPojo;
import com.increff.assure.pojo.OrderPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderService {
    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderItemDao orderItemDao;


    @Transactional
    public void placeOrder(OrderPojo orderPojo, List<OrderItemPojo> itemList)
    {
        orderDao.insert(orderPojo);
        for(OrderItemPojo item:itemList)
        {
            item.setOrderId(orderPojo.getId());
            orderItemDao.insert(item);
        }

    }


}
