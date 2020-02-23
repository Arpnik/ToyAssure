package com.increff.assure.dao;

import com.increff.assure.pojo.OrderPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

@Repository
public class OrderDao extends AbstractDao<OrderPojo> {
    private static String select_channelId_channelOrderId = "select p from OrderPojo p where p.channelId=:channelId and p.channelOrderId=:channelOrderId";
    private static String select_date= "select p from OrderPojo p where p.createdDate BETWEEN :startDate and :endDate";
    public OrderPojo getByParams(Long channelId, String channelOrderId)
    {
        TypedQuery<OrderPojo> query=getQuery(select_channelId_channelOrderId);
        query.setParameter("channelId",channelId);
        query.setParameter("channelOrderId",channelOrderId);
        return getSingle(query);
    }

    public List<OrderPojo> selectByDate(ZonedDateTime startDate, ZonedDateTime endDate)
    {
        TypedQuery<OrderPojo> query=getQuery(select_date);
        query.setParameter("startDate",startDate);
        query.setParameter("endDate",endDate);
        return query.getResultList();
    }


}
