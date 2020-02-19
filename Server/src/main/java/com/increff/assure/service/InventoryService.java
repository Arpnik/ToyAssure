package com.increff.assure.service;

import com.increff.assure.dao.InventoryDao;
import com.increff.assure.model.Exception.ApiException;
import com.increff.assure.pojo.InventoryPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InventoryService {
    @Autowired
    private InventoryDao dao;

    @Transactional
    public void addOrUpdateInventory(InventoryPojo pojo)
    {
        InventoryPojo existing=dao.getInventoryByGlobalSku(pojo.getGlobalSkuId());
        if(existing!=null)
        {
            existing.setAvailableQuantity(existing.getAvailableQuantity()+pojo.getAvailableQuantity());
            return;
        }
        dao.insert(pojo);
    }

    @Transactional(readOnly = true)
    public InventoryPojo getByGlobalSkuId(long globalSkuId) throws ApiException {
        InventoryPojo pojo=dao.getInventoryByGlobalSku(globalSkuId);
        if(pojo==null)
        {
            throw new ApiException("Product is unavaliable / out of stock");
        }
        return pojo;
    }


}
