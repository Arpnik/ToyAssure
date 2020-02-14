package com.increff.assure.service;

import com.increff.assure.dao.InventoryDao;
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
        InventoryPojo existing=dao.getInventoryPojoFromGlobalSkuId(pojo.getGlobalSkuId());
        if(existing!=null)
        {
            existing.setAvailableQuantity(existing.getAvailableQuantity()+pojo.getAvailableQuantity());
            dao.update(existing);
            return;
        }
        dao.insert(pojo);
    }

    @Transactional
    public InventoryPojo getByGlobalSkuId(long globalSkuId) throws ApiException {
        InventoryPojo pojo=dao.getInventoryPojoFromGlobalSkuId(globalSkuId);
        if(pojo==null)
        {
            throw new ApiException("Product is unavaliable / out of stock");
        }
        return pojo;
    }

}
