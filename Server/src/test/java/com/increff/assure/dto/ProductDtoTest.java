package com.increff.assure.dto;

import com.increff.assure.dao.MemberDao;
import com.increff.assure.model.constants.MemberTypes;
import com.increff.assure.model.data.ProductData;
import com.increff.assure.model.forms.ProductForm;
import com.increff.assure.model.forms.UpdateProductForm;
import com.increff.assure.pojo.MemberPojo;
import com.increff.assure.pojo.ProductPojo;
import com.increff.assure.service.ApiException;
import com.increff.assure.service.ProductService;
import com.increff.assure.util.AbstractUnitTest;
import util.ConvertGeneric;
import com.increff.assure.util.dataUtil;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ProductDtoTest extends AbstractUnitTest{

    @Autowired
    ProductDto dto;

    @Autowired
    ProductService service;

    @Autowired
    MemberDao dao;

    private MemberPojo member;
    private List<ProductForm> formList;

    @Before
    public void init()
    {
        formList=new ArrayList<>();
        member= dataUtil.createMemberPojo("flipkart", MemberTypes.CLIENT);
        dao.insert(member);
        ProductForm form1=dataUtil.createProductForm("sneakers","puma","black_sneakers","black sneakers with red stripes",12.4445);
        formList.add(form1);
        form1=dataUtil.createProductForm("watch","fossil","red_watch","nice watch",12.45);
        formList.add(form1);
    }

    @Test
    public void testAdd() throws Exception {
        dto.add(formList,member.getId());
        List<ProductPojo> pojoList=service.getAllByClientId(member.getId());
        assertEquals(formList.size(),pojoList.size());
        ProductForm form1=formList.get(0);
        ProductPojo pojo1=pojoList.get(0);
        assertEquals(form1.getClientSkuId(),pojo1.getClientSkuId());
        assertEquals(form1.getBrandId(),pojo1.getBrandId());
        assertEquals(form1.getDescription(),pojo1.getDescription());
        assertEquals(form1.getMrp(),pojo1.getMrp(),0.01);
        assertEquals(form1.getName(),pojo1.getName());
        pojo1=pojoList.get(1);
        form1=formList.get(1);
        assertEquals(form1.getClientSkuId(),pojo1.getClientSkuId());
        assertEquals(form1.getBrandId(),pojo1.getBrandId());
        assertEquals(form1.getDescription(),pojo1.getDescription());
        assertEquals(form1.getMrp(),pojo1.getMrp(),0.01);
        assertEquals(form1.getName(),pojo1.getName());
    }

//    @Test(expected = ApiException.class)
//    public void testCheckPresenceOfClient() throws Exception {
//    //    dto.checkPresenceOfClient(member.getId()+1);
//    }

    @Test
    public void testFormNormalize()
    {
        ProductForm form=dataUtil.createProductForm("SnEaKers    "," PUmA","black_shOES ","sda asd",100);
        dto.normalize(form);
        assertEquals("SnEaKers    ".toLowerCase().trim(),form.getName());
        assertEquals(" PUmA".toLowerCase().trim(),form.getBrandId());
        assertEquals("sda asd".toLowerCase().trim(),form.getDescription());
    }

    @Test
    public void testCheckClientIdAndClientSkuInvalid() throws ApiException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        ProductPojo pojo=ConvertGeneric.convert(formList.get(0),ProductPojo.class);
        pojo.setClientId(member.getId());
        pojo.setClientSkuId("iujht");
        dto.checkClientIdAndClientSku(pojo);
    }

    @Test
    public void testGet() throws Exception {
        ProductForm form=formList.get(0);
        ProductPojo pojo=ConvertGeneric.convert(form,ProductPojo.class);
        pojo.setClientId(member.getId());
        List<ProductPojo> pojoList=new ArrayList<>();
        pojoList.add(pojo);
        service.add(pojoList);
        ProductData data=dto.get(pojoList.get(0).getGlobalSkuId());
        assertEquals(pojo.getBrandId(),data.getBrandId());
        assertEquals(pojo.getClientSkuId(),data.getClientSkuId());
        assertEquals(pojo.getMrp(),data.getMrp(),0.001);
        assertEquals(pojo.getDescription(),data.getDescription());
    }

    @Test
    public void testGetAllByClientId() throws Exception {
        ProductPojo pojo=ConvertGeneric.convert(formList.get(0),ProductPojo.class);
        List<ProductPojo> pojoList=new ArrayList<>();
        pojo.setClientId(member.getId());
        pojoList.add(pojo);
        service.add(pojoList);
        List<ProductData> dataList=dto.getAllByClientId(member.getId());
        assertEquals(1,dataList.size());
    }

    @Test
    public void testUpdate() throws Exception {
        ProductPojo pojo=ConvertGeneric.convert(formList.get(0),ProductPojo.class);
        List<ProductPojo> pojoList=new ArrayList<>();
        pojo.setClientId(member.getId());
        pojoList.add(pojo);
        service.add(pojoList);
        UpdateProductForm updateProductForm=ConvertGeneric.convert(formList.get(0),UpdateProductForm.class);
        updateProductForm.setMrp(1000);
        updateProductForm.setDescription("kli jhD");
        dto.update(pojo.getGlobalSkuId(),updateProductForm);
        ProductPojo returned=service.get(pojo.getGlobalSkuId());
        assertEquals(updateProductForm.getDescription().toLowerCase().trim(),returned.getDescription());
        assertEquals(updateProductForm.getMrp(),returned.getMrp(),0.001);
    }


}
