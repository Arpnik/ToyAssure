package com.increff.assure.dto;

import com.increff.assure.model.data.BinFilterData;
import com.increff.assure.service.ApiException;
import com.increff.assure.model.forms.BinWiseInventoryForm;
import com.increff.assure.model.forms.BinFilterForm;
import com.increff.assure.model.forms.UpdateBinForm;
import com.increff.assure.model.forms.CreateBinForm;
import com.increff.assure.pojo.BinFilter;
import com.increff.assure.pojo.BinSkuPojo;
import com.increff.assure.pojo.InventoryPojo;
import com.increff.assure.pojo.ProductPojo;
import com.increff.assure.service.*;
import util.ConvertGeneric;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

@Service
public class BinDto {

    @Autowired
    private BinService binService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private ProductService productService;

    @Autowired
    private InventoryService inventoryService;

    public void createBins(CreateBinForm form){
        binService.createBins(form.getNumberOfBins());
    }

    public List<BinFilterData> getProductInfo(BinFilterForm form) throws Exception {
        validateForm(form);
        //todo keep boolean flag instead of sending hardcoded values.
        long globalSkuId=-1;
        if(!form.getClientSkuId().isEmpty())
        { normalize(form);
           ProductPojo productPojo= getCheckClientIdAndClientSku(form.getClientId(),form.getClientSkuId());
           globalSkuId=productPojo.getGlobalSkuId();
        }
        List<BinFilter> results=binService.getProductInfo(form.getBinId(),globalSkuId);
        //todo bin filter can be returned here instead of bin filter data
        List<BinFilterData> dataList=new ArrayList<>();
        for(BinFilter res:results)
        {
            BinFilterData data=ConvertGeneric.convert(res, BinFilterData.class);
            dataList.add(data);
        }
        return dataList;
    }

    public void uploadBinWiseInventory(long clientId, List<BinWiseInventoryForm> formList) throws ApiException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        memberService.checkPresenceOfClientById(clientId);
        List<BinSkuPojo> pojoList=new ArrayList<>();
        for(BinWiseInventoryForm form:formList)
        {
            form.setClientSkuId(form.getClientSkuId().trim());
            ProductPojo product= getCheckClientIdAndClientSku(clientId,form.getClientSkuId());
            binService.checkBinId(form.getBinId());
            BinSkuPojo binSkuPojo=ConvertGeneric.convert(form,BinSkuPojo.class);
            binSkuPojo.setGlobalSkuId(product.getGlobalSkuId());
            InventoryPojo inventoryPojo=createInventory(product.getGlobalSkuId(),form.getQuantity());
            inventoryService.addOrUpdateInventory(inventoryPojo);
            pojoList.add(binSkuPojo);
        }
        binService.AddOrUpdateInventory(pojoList);

    }

    public ProductPojo getCheckClientIdAndClientSku(long clientId, String clientSkuId) throws ApiException {
        ProductPojo product=productService.getByClientIdAndClientSku(clientId,clientSkuId);
        if(product==null)
        {
            throw new ApiException("No Product with Client SKU ID: "+clientSkuId+" and Client Name:" +memberService.get(clientId).getName()+" exists.");
        }
        return product;
    }

    public void updateBin(long binSkuId, UpdateBinForm form) throws ApiException {
        BinSkuPojo existing=binService.getCheck(binSkuId);
        binService.update(binSkuId,form.getQuantity());
        InventoryPojo inventoryPojo=createInventory(existing.getGlobalSkuId(),form.getQuantity()-existing.getQuantity());
        inventoryService.addOrUpdateInventory(inventoryPojo);
    }

    public void validateForm(BinFilterForm form) throws ApiException {
        if((form.getBinId() == 0) && form.getClientId()==0 && (form.getClientSkuId()==null||form.getClientSkuId().isEmpty()))
        {
            throw new ApiException("Please set some values in the  filter form");
        }
        if((!(form.getClientSkuId()==null||form.getClientSkuId().isEmpty())&& form.getClientId()==0)|| ((form.getClientSkuId()==null||form.getClientSkuId().isEmpty())&&form.getClientId()>0))
        {
            throw new ApiException("PLease select appropriate client Name and Client SKU");
        }
    }

    protected InventoryPojo createInventory(long globalSkuId,long quantity)
    {
        InventoryPojo pojo=new InventoryPojo();
        pojo.setAvailableQuantity(quantity);
        pojo.setGlobalSkuId(globalSkuId);
        return pojo;
    }

    protected void normalize(BinFilterForm form)
    {
        form.setClientSkuId(form.getClientSkuId().trim());
    }

}
