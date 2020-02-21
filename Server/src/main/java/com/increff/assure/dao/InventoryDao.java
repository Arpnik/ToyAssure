package com.increff.assure.dao;

import com.increff.assure.pojo.InventoryPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;

@Repository
public class InventoryDao extends AbstractDao<InventoryPojo> {
    private static String select_globalSkuId="select p from InventoryPojo p where p.globalSkuId=:globalSkuId";

    public InventoryPojo getInventoryByGlobalSku(Long globalSkuId)
    {
        TypedQuery<InventoryPojo> query=getQuery(select_globalSkuId);
        query.setParameter("globalSkuId",globalSkuId);
        return getSingle(query);
    }

}
