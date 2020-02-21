package com.increff.assure.dto;

import com.increff.assure.dao.BinDao;
import com.increff.assure.dao.BinSkuDao;
import com.increff.assure.dao.MemberDao;
import com.increff.assure.dao.ProductDao;
import com.increff.assure.model.Exception.ApiException;
import com.increff.assure.model.constants.MemberTypes;
import com.increff.assure.model.data.BinFilterData;
import com.increff.assure.model.forms.BinFilterForm;
import com.increff.assure.model.forms.BinInventoryForm;
import com.increff.assure.model.forms.CreateBinForm;
import com.increff.assure.model.forms.UpdateBinForm;
import com.increff.assure.pojo.BinPojo;
import com.increff.assure.pojo.BinSkuPojo;
import com.increff.assure.pojo.MemberPojo;
import com.increff.assure.pojo.ProductPojo;
import com.increff.assure.service.BinService;
import com.increff.assure.util.AbstractUnitTest;
import com.increff.assure.util.dataUtil;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class BinDtoTest extends AbstractUnitTest {
    @Autowired
    private BinDto dto;

    @Autowired
    private BinService service;

    @Autowired
    private BinDao binDao;

    @Autowired
    private BinSkuDao binSKuDao;

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private ProductDao productDao;

    private CreateBinForm numOfBins;
    private List<BinInventoryForm> formList;
    private MemberPojo member;
    private ProductPojo product;
    private BinPojo binPojo;

    @Before
    public void init() {
        numOfBins = dataUtil.createNumberBinForm(new Long(12));
        formList = new ArrayList<>();
        binPojo = new BinPojo();
        binDao.insert(binPojo);
        member = dataUtil.createMemberPojo("Puma", MemberTypes.CLIENT);
        memberDao.insert(member);
        product = dataUtil.createProductPojo("sneaker", "nike", "footwear", "nice shoes", 143.3, member.getId());
        productDao.insert(product);
        BinInventoryForm inventory = dataUtil.createBinInventoryForm(binPojo.getBinId(), product.getClientSkuId(), new Long(10));
        formList.add(inventory);
    }

    @Test
    public void testCreateBin() {
        dto.createBins(numOfBins);
        Long actual = numOfBins.getNumberOfBins() + 1;
        Long expected = Long.valueOf(binDao.selectAll().size());
        assertEquals(actual, expected);
    }


    @Test
    public void testValidateForm() throws ApiException {
        BinFilterForm form = dataUtil.createBinFilterForm(new Long(1), new Long(10), "shoes_black");
        //com.increff.assure.dto.validateForm(form);
    }

    @Test
    public void testUploadInventoryForm() throws ApiException {
        ProductPojo product2 = dataUtil.createProductPojo("product_name","brandId","sku2","description",12783.2,member.getId());
        productDao.insert(product2);
        BinInventoryForm inventory = dataUtil.createBinInventoryForm(binPojo.getBinId(), product2.getClientSkuId(), new Long(10));
        formList.add(inventory);
        dto.uploadBinWiseInventory(member.getId(), formList);
        assertEquals(formList.size(), binSKuDao.selectAll().size());
        BinSkuPojo returned = binSKuDao.selectAll().get(0);
        assertEquals(product.getGlobalSkuId(), returned.getGlobalSkuId());
        assertEquals(formList.get(0).getQuantity(), returned.getQuantity());
        assertEquals(formList.get(0).getBinId(), returned.getBinId());
        returned = binSKuDao.selectAll().get(1);
        assertEquals(product2.getGlobalSkuId(), returned.getGlobalSkuId());
        assertEquals(formList.get(0).getQuantity(), returned.getQuantity());
        assertEquals(formList.get(0).getBinId(), returned.getBinId());
    }

    @Test(expected = ApiException.class)
    public void testAddOrUploadInventoryInvalid() throws ApiException {
        dto.uploadBinWiseInventory(member.getId()+1, formList);
    }

    @Test(expected = ApiException.class)
    public void testUploadInventoryInvalid() throws ApiException {
        formList.get(0).setBinId(new Long(1000101));
        dto.uploadBinWiseInventory(member.getId(),formList);
    }

    @Test(expected = ApiException.class)
    public void testUpdateBinInvalid() throws ApiException {
        UpdateBinForm form=dataUtil.createUpdateBinForm(new Long(101));
        dto.updateBin(new Long(101),form);
    }

    @Test
    public void testUpdateBin() throws ApiException {
        UpdateBinForm form=dataUtil.createUpdateBinForm(new Long(1));
        BinInventoryForm f=formList.get(0);
        BinSkuPojo pojo=dataUtil.createBinSkuPojo(f.getQuantity(),product.getGlobalSkuId(),binPojo.getBinId());
        binSKuDao.insert(pojo);
        dto.updateBin(pojo.getId(),form);
        assertEquals(form.getQuantity(),binSKuDao.select(pojo.getId()).getQuantity());
    }

    @Test(expected = ApiException.class)
    public void testBinFilterForm() throws ApiException {
        BinSkuPojo pojo=dataUtil.createBinSkuPojo(new Long(101),product.getGlobalSkuId(),binPojo.getBinId());
        binSKuDao.insert(pojo);
        BinFilterForm form=dataUtil.createBinFilterForm(binPojo.getBinId(),null,product.getClientSkuId());
        dto.getProductInfo(form);
    }

    @Test(expected = ApiException.class)
    public void testGetProductInfoInvalid2() throws ApiException {
        BinSkuPojo pojo=dataUtil.createBinSkuPojo(new Long(101),product.getGlobalSkuId(),binPojo.getBinId());
        binSKuDao.insert(pojo);
        BinFilterForm form=dataUtil.createBinFilterForm(binPojo.getBinId(),member.getId(),null);
        dto.getProductInfo(form);
    }

    @Test(expected = ApiException.class)
    public void testGetProductInfoInvalid3() throws ApiException {
        BinSkuPojo pojo=dataUtil.createBinSkuPojo(new Long(101),product.getGlobalSkuId(),binPojo.getBinId());
        binSKuDao.insert(pojo);
        BinFilterForm form=dataUtil.createBinFilterForm(null,null,null);
        dto.getProductInfo(form);
    }

    @Test
    public void testGetProductInfo1() throws ApiException {
        BinSkuPojo pojo=dataUtil.createBinSkuPojo(new Long(101),product.getGlobalSkuId(),binPojo.getBinId());
        binSKuDao.insert(pojo);
        BinFilterForm form=dataUtil.createBinFilterForm(binPojo.getBinId(),member.getId(),product.getClientSkuId());
        List<BinFilterData> dataList=dto.getProductInfo(form);
        assertEquals(1,dataList.size());
        BinFilterData data=dataList.get(0);
        assertEquals(product.getClientSkuId(),data.getClientSkuId());
        assertEquals(member.getId(),data.getClientId());
        assertEquals(pojo.getId(),data.getBinSkuId());
    }

    @Test
    public void testGetProductInfo2() throws ApiException {
        BinSkuPojo pojo=dataUtil.createBinSkuPojo(new Long(101),product.getGlobalSkuId(),binPojo.getBinId());
        binSKuDao.insert(pojo);
        BinFilterForm form=dataUtil.createBinFilterForm(binPojo.getBinId(),null,null);
        List<BinFilterData> dataList=dto.getProductInfo(form);
        assertEquals(1,dataList.size());
        BinFilterData data=dataList.get(0);
        assertEquals(product.getClientSkuId(),data.getClientSkuId());
        assertEquals(member.getId(),data.getClientId());
        assertEquals(pojo.getId(),data.getBinSkuId());
    }

    @Test
    public void testGetProductInfo3() throws ApiException {
        BinSkuPojo pojo=dataUtil.createBinSkuPojo(new Long(101),product.getGlobalSkuId(),binPojo.getBinId());
        binSKuDao.insert(pojo);
        BinFilterForm form=dataUtil.createBinFilterForm(null,member.getId(),product.getClientSkuId());
        List<BinFilterData> dataList=dto.getProductInfo(form);
        assertEquals(1,dataList.size());
        BinFilterData data=dataList.get(0);
        assertEquals(pojo.getBinId(),data.getBinId());
        assertEquals(pojo.getId(),data.getBinSkuId());
    }










}
