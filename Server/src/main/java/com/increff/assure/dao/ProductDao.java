package com.increff.assure.dao;

import com.increff.assure.pojo.ProductPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class ProductDao extends AbstractDao<ProductPojo> {

    private static String select_clientId_clientSku="select p from ProductPojo p where p.clientId = :clientId and p.clientSkuId = :clientSkuId";
    private static String select_clientId="select p from ProductPojo p where p.clientId = :clientId";

    public ProductPojo selectByClientIdAndClientSku(long clientId, String clientSkuId) {
        TypedQuery<ProductPojo> query = getQuery(select_clientId_clientSku);
        query.setParameter("clientId", clientId);
        query.setParameter("clientSkuId", clientSkuId);
        return getSingle(query);
    }

    public List<ProductPojo> selectByClientId(long clientId)
    {
        TypedQuery<ProductPojo> query = getQuery(select_clientId);
        query.setParameter("clientId", clientId);
        return query.getResultList();
    }


}
