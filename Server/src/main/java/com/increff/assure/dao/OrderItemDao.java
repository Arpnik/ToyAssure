package com.increff.assure.dao;

import com.increff.assure.model.Pojo.OrderDetailsResult;
import com.increff.assure.pojo.OrderItemPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class OrderItemDao extends AbstractDao<OrderItemPojo> {

    private static String select_orderId="Select new com.increff.assure.model.Pojo.OrderDetailsResult(p.name,p.brandId,i.orderedQuantity,i.sellingPricePerUnit, i.allocatedQuantity) from OrderItemPojo i,ProductPojo p where i.orderId=:orderId and p.globalSkuId=i.globalSkuId";
    private static String select="Select p from OrderItemPojo p where p.orderId=:orderId";

    public List<OrderDetailsResult> getDetailsByOrderId(long orderId)
    {
        Query query=em().createQuery(select_orderId);
        query.setParameter("orderId",orderId);
        return query.getResultList();
    }

    public List<OrderItemPojo> getOrderedItems(long orderId)
    {
        TypedQuery<OrderItemPojo> query=getQuery(select);
        query.setParameter("orderId",orderId);
        return query.getResultList();
    }

}
