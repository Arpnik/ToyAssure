package com.increff.assure.service;

import com.increff.assure.dao.InventoryDao;
import com.increff.assure.model.Exception.ApiException;
import com.increff.assure.pojo.InventoryPojo;
import com.increff.assure.util.AbstractUnitTest;
import com.increff.assure.util.dataUtil;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;

public class InventoryServiceTest extends AbstractUnitTest {
    @Autowired
    private InventoryService service;

    @Autowired
    private InventoryDao dao;

    private InventoryPojo pojo;

    @Before
    public void init()
    {
        pojo= dataUtil.createInventoryPojo(new Long(101),new Long(10));
    }

    @Test
    public void testAddOrUpdate()
    {
        service.addOrUpdateInventory(pojo);
        assertEquals(1,dao.selectAll().size());
        InventoryPojo returned=dao.selectAll().get(0);
        assertEquals(pojo.getGlobalSkuId(),returned.getGlobalSkuId());
        assertEquals(pojo.getAvailableQuantity(),returned.getAvailableQuantity());
    }

    @Test
    public void testAddOrUpdateDuplicate()
    {
        dao.insert(pojo);
        InventoryPojo pojo2=dataUtil.createInventoryPojo(new Long(101),new Long(20));
        service.addOrUpdateInventory(pojo2);
        assertEquals(1,dao.selectAll().size());
        InventoryPojo returned=dao.selectAll().get(0);
        assertEquals(pojo.getGlobalSkuId(),returned.getGlobalSkuId());
        assertEquals(pojo.getAvailableQuantity(),returned.getAvailableQuantity());
    }

    @Test
    public void testGetValid() throws ApiException {
        dao.insert(pojo);
        InventoryPojo returned=service.getByGlobalSkuId(pojo.getGlobalSkuId());
        assertEquals(pojo.getGlobalSkuId(),returned.getGlobalSkuId());
        assertEquals(pojo.getAvailableQuantity(),returned.getAvailableQuantity());
    }

    @Test(expected = ApiException.class)
    public void testGetInValid() throws ApiException {
        dao.insert(pojo);
        InventoryPojo returned=service.getByGlobalSkuId(pojo.getGlobalSkuId()+1);
    }




}
