package com.increff.assure.service;

import com.increff.assure.dao.BinDao;
import com.increff.assure.dao.BinSkuDao;
import com.increff.assure.dao.MemberDao;
import com.increff.assure.dao.ProductDao;
import com.increff.assure.model.constants.MemberTypes;
import com.increff.assure.pojo.*;
import com.increff.assure.util.AbstractUnitTest;
import com.increff.assure.util.dataUtil;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class BinServiceTest extends AbstractUnitTest {
    @Autowired
    private BinDao binDao;

    @Autowired
    private BinSkuDao binSkuDao;

    @Autowired
    private BinService binService;

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private ProductDao productDao;

    private List<BinSkuPojo> pojoList;
    private  BinPojo binPojo;
    private ProductPojo productPojo;
    private MemberPojo memberPojo;

    @Before
    public void init()
    {
        pojoList=new ArrayList<>();
        binPojo=new BinPojo();
        binDao.insert(binPojo);
        memberPojo=dataUtil.createMemberPojo("raju", MemberTypes.CLIENT);
        memberDao.insert(memberPojo);
        productPojo=dataUtil.createProductPojo("sneaker","puma","black_sneaker","shoes black",12.23,memberPojo.getId());
        productDao.insert(productPojo);
        BinSkuPojo item= dataUtil.createBinSkuPojo(10,productPojo.getGlobalSkuId(),binPojo.getBinId());
        pojoList.add(item);
    }

    @Test
    public void testCreateBins()
    {
        long numberOfBins=10;
        binService.createBins(numberOfBins);
        assertEquals(numberOfBins+1,binDao.selectAll().size());
    }

    @Test
    public void testAddInventory()
    {
            binService.AddOrUpdateInventory(pojoList);
            BinSkuPojo item=pojoList.get(0);
            BinSkuPojo skuPojo=binSkuDao.selectAll().get(0);
            assertEquals(item.getBinId(),skuPojo.getBinId());
            assertEquals(item.getGlobalSkuId(),skuPojo.getGlobalSkuId());
            assertEquals(item.getQuantity(),skuPojo.getQuantity());
    }

    @Test
    public void testUpdateInventory()
    {
        binService.AddOrUpdateInventory(pojoList);
        BinSkuPojo item=pojoList.get(0);
        binService.AddOrUpdateInventory(pojoList);
        BinSkuPojo skuPojo=binSkuDao.selectAll().get(0);
        assertEquals(item.getBinId(),skuPojo.getBinId());
        assertEquals(item.getGlobalSkuId(),skuPojo.getGlobalSkuId());
        assertEquals(item.getQuantity(),skuPojo.getQuantity());
    }

    @Test(expected = ApiException.class)
    public void testCheckBinIdInValid() throws ApiException {
        binService.checkBinId(pojoList.get(0).getBinId()+1);
    }

    @Test
    public void testCheckBinId() throws ApiException {
        binService.checkBinId(pojoList.get(0).getBinId());
    }

    @Test
    public void testUpdate() throws ApiException {
        BinSkuPojo item=pojoList.get(0);
        long newQuantity=100;
        binSkuDao.insert(item);
        binService.update(item.getId(),newQuantity);
        assertEquals(newQuantity,binSkuDao.selectAll().get(0).getQuantity());
    }

    @Test(expected = ApiException.class)
    public void testUpdateInvalid() throws ApiException {
        BinSkuPojo item=pojoList.get(0);
        long newQuantity=100;
        binSkuDao.insert(item);
        binService.update(item.getId()+1,newQuantity);
        assertEquals(newQuantity,binSkuDao.selectAll().get(0).getQuantity());
    }

    @Test
    public void testGetProductInfoBinId() throws ApiException {
        BinSkuPojo item=pojoList.get(0);
        binSkuDao.insert(item);
        List<BinFilter> filterList=binService.getProductInfo(item.getBinId(),-1);
        assertEquals(1,filterList.size());
        BinFilter filter=filterList.get(0);
        assertEquals(item.getId(),filter.getBinSkuId());
        assertEquals(memberPojo.getName(),filter.getName());
        assertEquals(productPojo.getClientSkuId(),filter.getClientSkuId());
        assertEquals(item.getQuantity(),filter.getQuantity());
    }

    @Test
    public void testGetProductInfoGlobalSkuId() throws ApiException {
        BinSkuPojo item=pojoList.get(0);
        binSkuDao.insert(item);
        List<BinFilter> filterList=binService.getProductInfo(0,productPojo.getGlobalSkuId());
        assertEquals(1,filterList.size());
        BinFilter filter=filterList.get(0);
        assertEquals(item.getBinId(),filter.getBinId());
        assertEquals(item.getQuantity(),filter.getQuantity());
    }

    @Test
    public void testGetProductInfoBinIdAndGlobalSkuId() throws ApiException {
        BinSkuPojo item=pojoList.get(0);
        binSkuDao.insert(item);
        List<BinFilter> filterList=binService.getProductInfo(item.getBinId(),productPojo.getGlobalSkuId());
        assertEquals(1,filterList.size());
        BinFilter filter=filterList.get(0);
        assertEquals(item.getQuantity(),filter.getQuantity());
    }


}
