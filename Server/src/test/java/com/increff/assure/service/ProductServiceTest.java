package com.increff.assure.service;

import com.increff.assure.dao.ProductDao;
import com.increff.assure.model.Exception.ApiException;
import com.increff.assure.pojo.ProductPojo;
import com.increff.assure.spring.ApplicationProperties;
import com.increff.assure.util.AbstractUnitTest;
import com.increff.assure.util.dataUtil;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ProductServiceTest extends AbstractUnitTest {
    @Autowired
    ProductService service;

    @Autowired
    ProductDao productDao;

    private List<ProductPojo> pojoList;

    @Autowired
    private ApplicationProperties applicationProperties;



    @Before
    public void init() {
        pojoList = new ArrayList<>();
        ProductPojo pojo = dataUtil.createProductPojo("sneakers", "puma", "black_sneakers", "black sneakers with red stripes", 12.4445, new Long(1));
        pojoList.add(pojo);
        ProductPojo pojo2 = dataUtil.createProductPojo("watch", "fossil", "red_watch", "nice watch", 12.45, new Long(2));
        pojoList.add(pojo2);
    }

    @Test
    public void testAdd() {
        applicationProperties.getChannelUri();
        service.add(pojoList);
        assertEquals(pojoList.size(), productDao.selectAll().size());
        ProductPojo pojo1 = pojoList.get(0);
        ProductPojo returned = productDao.selectAll().get(0);
        assertEquals(pojo1.getBrandId(), returned.getBrandId());
        assertEquals(pojo1.getClientId(), returned.getClientId());
        assertEquals(pojo1.getClientSkuId(), returned.getClientSkuId());
        assertEquals(pojo1.getDescription(), returned.getDescription());
        assertEquals(pojo1.getMrp(), returned.getMrp(), 0.001);
        returned = productDao.selectAll().get(1);
        pojo1 = pojoList.get(1);
        assertEquals(pojo1.getBrandId(), returned.getBrandId());
        assertEquals(pojo1.getClientId(), returned.getClientId());
        assertEquals(pojo1.getClientSkuId(), returned.getClientSkuId());
        assertEquals(pojo1.getDescription(), returned.getDescription());
        assertEquals(pojo1.getMrp(), returned.getMrp(), 0.001);
    }

    @Test
    public void testGetAllByClientId() {
        productDao.insert(pojoList.get(0));
        productDao.insert(pojoList.get(1));
        productDao.insert(dataUtil.createProductPojo("high end watch", "golden", "jidsak", "golden dial watch", 125.6, new Long(2)));
        List<ProductPojo> returnedList = service.getAllById(new Long(1));
        assertEquals(1, returnedList.size());
    }

    @Test
    public void testGetCheckValid() throws ApiException {
        productDao.insert(pojoList.get(0));
        ProductPojo returned = service.getCheck(pojoList.get(0).getGlobalSkuId());
        ProductPojo pojo1 = pojoList.get(0);
        assertEquals(pojo1.getBrandId(), returned.getBrandId());
        assertEquals(pojo1.getClientId(), returned.getClientId());
        assertEquals(pojo1.getClientSkuId(), returned.getClientSkuId());
        assertEquals(pojo1.getDescription(), returned.getDescription());
        assertEquals(pojo1.getMrp(), returned.getMrp(), 0.001);
    }

    @Test(expected = ApiException.class)
    public void testGetCheckInValid() throws ApiException {
        productDao.insert(pojoList.get(0));
        service.getCheck(pojoList.get(0).getGlobalSkuId() + 100);
    }

    @Test
    public void testGetUpdate() throws ApiException {
        productDao.insert(pojoList.get(0));
        ProductPojo existing = pojoList.get(0);
        ProductPojo pojo1 = dataUtil.createProductPojo(existing.getName() + " latest", existing.getBrandId() + "updated one", existing.getClientSkuId(), existing.getDescription() + " this is updated text", existing.getMrp() + 100, existing.getClientId());
        service.update(existing.getGlobalSkuId(), pojo1);
        ProductPojo returned = productDao.select(existing.getGlobalSkuId());
        assertEquals(pojo1.getBrandId(), returned.getBrandId());
        assertEquals(pojo1.getClientId(), returned.getClientId());
        assertEquals(pojo1.getClientSkuId(), returned.getClientSkuId());
        assertEquals(pojo1.getDescription(), returned.getDescription());
        assertEquals(pojo1.getMrp(), returned.getMrp(), 0.001);
    }

    @Test
    public void testGetByParamsValid() throws ApiException {
        productDao.insert(pojoList.get(0));
        ProductPojo pojo1 = pojoList.get(0);
        ProductPojo returned = service.getCheckByParams(pojo1.getClientId(), pojo1.getClientSkuId());
        assertEquals(pojo1.getBrandId(), returned.getBrandId());
        assertEquals(pojo1.getClientId(), returned.getClientId());
        assertEquals(pojo1.getClientSkuId(), returned.getClientSkuId());
        assertEquals(pojo1.getDescription(), returned.getDescription());
        assertEquals(pojo1.getMrp(), returned.getMrp(), 0.001);
    }

    @Test(expected = ApiException.class)
    public void testGetCheckByParamsInvalid() throws ApiException {
        productDao.insert(pojoList.get(0));
        ProductPojo pojo1 = pojoList.get(0);
        service.getCheckByParams(pojo1.getClientId() + 100, pojo1.getClientSkuId());

    }


}
