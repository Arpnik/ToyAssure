package com.increff.assure.dto;

import com.increff.assure.model.data.ProductData;
import com.increff.assure.model.forms.ProductForm;
import com.increff.assure.model.forms.UpdateProductForm;
import com.increff.assure.pojo.ProductPojo;
import com.increff.assure.service.ApiException;
import com.increff.assure.service.MemberService;
import com.increff.assure.service.ProductService;
import util.ConvertGeneric;
import com.increff.assure.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductDto {

    @Autowired
    ProductService productService;

    @Autowired
    MemberService memberService;

    @Transactional(rollbackFor = ApiException.class)
    public void add(List<ProductForm> formList,long clientId) throws Exception {
        memberService.checkPresenceOfClientById(clientId);
        List<ProductPojo> pojoList=new ArrayList<>();
        for(ProductForm form:formList){
            normalizeProductForm(form);
            ProductPojo pojo=ConvertGeneric.convert(form,ProductPojo.class);
            pojo.setClientId(clientId);
            checkClientIdAndClientSku(pojo);
            pojoList.add(pojo);
        }
        productService.add(pojoList);
    }

    public List<ProductData> getAllByClientId(long clientId) throws Exception {
        memberService.checkPresenceOfClientById(clientId);
        List<ProductPojo> pojoList= productService.getAllByClientId(clientId);
        List<ProductData> dataList=new ArrayList<>();
        for(ProductPojo pojo:pojoList)
        {
            ProductData data=ConvertGeneric.convert(pojo,ProductData.class);
            dataList.add(data);
        }
        return dataList;
    }

    public void update(long id, UpdateProductForm form) throws Exception {
        normalize(form);
        ProductPojo pojo=ConvertGeneric.convert(form,ProductPojo.class);
        productService.update(id,pojo);
    }

    public ProductData get(long globalSkuId)throws Exception
    {
        ProductPojo pojo= productService.getCheck(globalSkuId);
        ProductData data=ConvertGeneric.convert(pojo,ProductData.class);
        return data;
    }



    protected void checkClientIdAndClientSku(ProductPojo pojo) throws ApiException {
        ProductPojo existing=productService.getByClientIdAndClientSku(pojo.getClientId(),pojo.getClientSkuId());
        if(existing!=null)
        {
            throw new ApiException("Client SKU ID:"+pojo.getClientSkuId()+" for Client,"+memberService.get(pojo.getClientId()).getName()+" are not unique, Product Name:"+pojo.getName());
        }
    }

    protected void normalize(UpdateProductForm form)
    {
        form.setDescription(StringUtil.toLowerCase(form.getDescription()));
        form.setName(StringUtil.toLowerCase(form.getName()));
        form.setBrandId(StringUtil.toLowerCase(form.getBrandId()));
    }

    protected void normalizeProductForm(ProductForm form)
    {
        normalize(form);
        form.setClientSkuId(form.getClientSkuId().trim());
    }
}
