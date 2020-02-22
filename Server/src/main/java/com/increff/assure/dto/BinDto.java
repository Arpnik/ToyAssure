package com.increff.assure.dto;

import com.increff.assure.model.Exception.ApiException;
import com.increff.assure.model.constants.MemberTypes;
import com.increff.assure.model.data.BinFilterData;
import com.increff.assure.model.data.ErrorData;
import com.increff.assure.model.forms.BinFilterForm;
import com.increff.assure.model.forms.BinInventoryForm;
import com.increff.assure.model.forms.CreateBinForm;
import com.increff.assure.model.forms.UpdateBinForm;
import com.increff.assure.pojo.BinSkuPojo;
import com.increff.assure.pojo.InventoryPojo;
import com.increff.assure.pojo.ProductPojo;
import com.increff.assure.service.BinService;
import com.increff.assure.service.InventoryService;
import com.increff.assure.service.MemberService;
import com.increff.assure.service.ProductService;
import com.increff.assure.util.ConvertGeneric;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.increff.assure.util.Normalize.normalize;
import static com.increff.assure.util.Validate.validate;

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
    public void createBins(CreateBinForm form) {
        binService.createBins(form.getNumberOfBins());
    }

    @Transactional(readOnly = true)
    public List<BinFilterData> getProductInfo(BinFilterForm form) throws ApiException {
        validate(form);

        if (form.getClientSkuId() == null)
            return binService.getProductInfo(form.getBinId()).stream().map(x -> ConvertGeneric.convert(x, BinFilterData.class)).collect(Collectors.toList());

        normalize(form);
        ProductPojo product = productService.getCheckByParams(form.getClientId(), form.getClientSkuId());
        Long globalSkuId = product.getGlobalSkuId();
        if (form.getBinId() == null)
            return binService.getProducts(globalSkuId).stream().map(x -> ConvertGeneric.convert(x, BinFilterData.class)).collect(Collectors.toList());

        return binService.getProductInfo(form.getBinId(), globalSkuId).stream().map(x -> ConvertGeneric.convert(x, BinFilterData.class)).collect(Collectors.toList());

    }

    @Transactional(rollbackFor = ApiException.class)
    public void uploadBinWiseInventory(Long clientId, List<BinInventoryForm> formList) throws ApiException {
        memberService.checkMemberType(clientId, MemberTypes.CLIENT);

        List<BinSkuPojo> pojoList = new ArrayList<>();
        List<ErrorData> errorList = new ArrayList<>();

        int sno = -1;
        for (BinInventoryForm form : formList) {
            sno += 1;
            form.setClientSkuId(form.getClientSkuId().trim());
            ProductPojo product = null;
            try {
                product = productService.getCheckByParams(clientId, form.getClientSkuId());
                binService.checkBinId(form.getBinId());
            } catch (ApiException e) {
                ErrorData data = new ErrorData(new Long(sno), e.getMessage());
                errorList.add(data);
                continue;
            }
            //TODO commenting
            //TODO see duplicate element in bin same
            BinSkuPojo binSkuPojo = ConvertGeneric.convert(form, BinSkuPojo.class);
            binSkuPojo.setGlobalSkuId(product.getGlobalSkuId());
            InventoryPojo inventoryPojo = createInventory(product.getGlobalSkuId(), form.getQuantity());
            inventoryService.addOrUpdateInventory(inventoryPojo);
            pojoList.add(binSkuPojo);
        }

        if (errorList.size() != 0)
            throw new ApiException(ErrorData.convert(errorList));

        binService.AddOrUpdateInventory(pojoList);
    }


    @Transactional(rollbackFor = ApiException.class)
    public void updateBin(Long binSkuId, UpdateBinForm form) throws ApiException {
        BinSkuPojo existing = binService.getCheck(binSkuId);
        binService.update(binSkuId, form.getQuantity());
        InventoryPojo inventoryPojo = createInventory(existing.getGlobalSkuId(), form.getQuantity() - existing.getQuantity());
        inventoryService.addOrUpdateInventory(inventoryPojo);
    }


    protected InventoryPojo createInventory(Long globalSkuId, Long quantity) {
        InventoryPojo pojo = new InventoryPojo();
        pojo.setAvailableQuantity(quantity);
        pojo.setGlobalSkuId(globalSkuId);
        return pojo;
    }


}
