package com.increff.assure.dao;

import com.increff.assure.pojo.OrderPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;

@Repository
public class OrderDao extends AbstractDao<OrderPojo> {
    private static String select_channelId_channelOrderId = "select p from OrderPojo p where p.channelId=:channelId and p.channelOrderId=:channelOrderId";

    public OrderPojo getBychannelIdAndChannelOrderId(long channelId,String channelOrderId)
    {
        TypedQuery<OrderPojo> query=getQuery(select_channelId_channelOrderId);
        query.setParameter("channelId",channelId);
        query.setParameter("channelOrderId",channelOrderId);
        return getSingle(query);
    }


}
