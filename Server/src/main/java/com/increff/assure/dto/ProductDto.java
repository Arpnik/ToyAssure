package com.increff.assure.dto;

import com.increff.assure.model.Exception.ApiException;
import com.increff.assure.model.constants.MemberTypes;
import com.increff.assure.model.data.ErrorData;
import com.increff.assure.model.data.ProductData;
import com.increff.assure.model.forms.ProductForm;
import com.increff.assure.model.forms.UpdateProductForm;
import com.increff.assure.pojo.ProductPojo;
import com.increff.assure.service.MemberService;
import com.increff.assure.service.ProductService;
import com.increff.assure.util.ConvertGeneric;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.increff.assure.util.Normalize.normalize;
import static com.increff.assure.util.Normalize.normalizeProductForm;

@Service
public class ProductDto {

    @Autowired
    ProductService productService;

    @Autowired
    MemberService memberService;

    @Transactional(rollbackFor = ApiException.class)
    public void add(List<ProductForm> formList,long clientId) throws ApiException {
        memberService.checkMemberType(clientId, MemberTypes.CLIENT);

        List<ProductPojo> pojoList = new ArrayList<>();
        List<ErrorData> errorList = new ArrayList<>();

        int sno = 0;
        for(ProductForm form: formList){

            normalizeProductForm(form);
            ProductPojo pojo = ConvertGeneric.convert(form,ProductPojo.class);
            pojo.setClientId( clientId);
            try {
                checkClientIdAndSku(pojo);
            }
            catch (ApiException e)
            {
                ErrorData data=new ErrorData(sno,e.getMessage());
                errorList.add(data);
            }
                pojoList.add(pojo);
                sno+=1;
        }

        if(errorList.size()!=0)
            throw new ApiException(ErrorData.convert(errorList));

        productService.add(pojoList);
    }

    @Transactional(readOnly = true)
    public List<ProductData> getAllByClientId(long clientId) throws ApiException {
        memberService.checkMemberType(clientId, MemberTypes.CLIENT);
        List<ProductPojo> pojoList= productService.getAllById(clientId);
        List<ProductData> dataList=new ArrayList<>();
        for(ProductPojo pojo:pojoList)
        {
            ProductData data=ConvertGeneric.convert(pojo,ProductData.class);
            dataList.add(data);
        }
        return dataList;
    }

    @Transactional(rollbackFor = ApiException.class)
    public void update(long id, UpdateProductForm form) throws ApiException {
        normalize(form);
        ProductPojo pojo = ConvertGeneric.convert(form,ProductPojo.class);
        productService.update(id, pojo);
    }

    @Transactional(readOnly = true)
    public ProductData get(long globalSkuId) throws ApiException {
        ProductPojo pojo = productService.getCheck(globalSkuId);
        ProductData data = ConvertGeneric.convert(pojo, ProductData.class);
        return data;
    }

    @Transactional(readOnly = true)
    protected void checkClientIdAndSku(ProductPojo pojo) throws ApiException {
        ProductPojo existing=productService.get(pojo.getClientId(),pojo.getClientSkuId());
        if(existing!=null)
        {
            throw new ApiException("Client SKU ID:"+pojo.getClientSkuId()+" for Client:"+memberService.get(pojo.getClientId()).getName()+" are not unique");
        }
    }

}
