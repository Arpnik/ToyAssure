package com.increff.assure.dto;

import com.increff.assure.model.Exception.ApiException;
import com.increff.assure.model.constants.MemberTypes;
import com.increff.assure.model.data.BinFilterData;
import com.increff.assure.model.data.ErrorData;
import com.increff.assure.model.forms.BinFilterForm;
import com.increff.assure.model.forms.BinWiseInventoryForm;
import com.increff.assure.model.forms.CreateBinForm;
import com.increff.assure.model.forms.UpdateBinForm;
import com.increff.assure.pojo.BinFilter;
import com.increff.assure.pojo.BinSkuPojo;
import com.increff.assure.pojo.InventoryPojo;
import com.increff.assure.pojo.ProductPojo;
import com.increff.assure.service.*;
import com.increff.assure.util.ConvertGeneric;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.increff.assure.util.Normalize.normalize;

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

    @Transactional
    public void createBins(CreateBinForm form){
        binService.createBins(form.getNumberOfBins());
    }

    @Transactional(readOnly = true)
    public List<BinFilterData> getProductInfo(BinFilterForm form) throws ApiException {
        validateForm(form);

        if(form.getClientSkuId().isEmpty())
            return convert(binService.getProductInfo(form.getBinId()));

        normalize(form);
        ProductPojo product = getCheckIdAndSku(form.getClientId(),form.getClientSkuId());
        long globalSkuId=product.getGlobalSkuId();
        if(form.getBinId()==0)
            return convert(binService.getProducts(globalSkuId));
        else
            return convert( binService.getProductInfo(form.getBinId(),globalSkuId));

    }

    @Transactional(rollbackFor = ApiException.class)
    public void uploadBinWiseInventory(long clientId, List<BinWiseInventoryForm> formList) throws ApiException {
        memberService.checkMemberType(clientId, MemberTypes.CLIENT);

        List<BinSkuPojo> pojoList = new ArrayList<>();
        List<ErrorData> errorList = new ArrayList<>();

        int sno=-1;
        for(BinWiseInventoryForm form:formList)
        {
            sno+=1;
            form.setClientSkuId(form.getClientSkuId().trim());
            ProductPojo product=null;
            try {
                product = getCheckIdAndSku(clientId, form.getClientSkuId());
                binService.checkBinId(form.getBinId());
            }
            catch (ApiException e)
            {
                ErrorData data=new ErrorData(sno,e.getMessage());
                errorList.add(data);
                continue;
            }

            BinSkuPojo binSkuPojo = ConvertGeneric.convert(form,BinSkuPojo.class);
            binSkuPojo.setGlobalSkuId(product.getGlobalSkuId());
            InventoryPojo inventoryPojo = createInventory(product.getGlobalSkuId(),form.getQuantity());
            inventoryService.addOrUpdateInventory(inventoryPojo);
            pojoList.add(binSkuPojo);
        }

        if(errorList.size() != 0)
            throw new ApiException(ErrorData.convert(errorList));

        binService.AddOrUpdateInventory(pojoList);

    }

    @Transactional(readOnly = true)
    public ProductPojo getCheckIdAndSku(long clientId, String clientSkuId) throws ApiException {
        ProductPojo product=productService.getCheckByParams(clientId,clientSkuId);
        return product;
    }

    @Transactional(rollbackFor = ApiException.class)
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

    protected List<BinFilterData> convert(List<BinFilter> results)
    {
        List<BinFilterData> dataList=new ArrayList<>();
        for(BinFilter res: results)
        {
            BinFilterData data = ConvertGeneric.convert(res, BinFilterData.class);
            dataList.add(data);
        }
        return dataList;
    }


}
