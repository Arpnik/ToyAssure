package com.increff.assure.controller;

import com.increff.assure.dto.BinDto;
import com.increff.assure.model.data.BinFilterData;
import com.increff.assure.model.forms.BinWiseInventoryForm;
import com.increff.assure.model.forms.BinFilterForm;
import com.increff.assure.model.forms.UpdateBinForm;
import com.increff.assure.model.forms.CreateBinForm;
import com.increff.assure.service.ApiException;
import com.increff.assure.util.ValidateRequestBodyList;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api
@RestController
public class BinController {
    @Autowired
    private BinDto dto;

    @ApiOperation(value = "Create Bins")
    @RequestMapping(path = "/api/bin", method = RequestMethod.POST)
    public void addBins(@Valid @RequestBody CreateBinForm form) throws ApiException {
        dto.createBins(form);
    }

    @ApiOperation(value="Get information of Product depending upon BinId, ClientId and ClientSku")
    @RequestMapping(path="/api/bin/product",method = RequestMethod.POST)
    public List<BinFilterData> getProductInfo(@RequestBody BinFilterForm form) throws ApiException {
        return dto.getProductInfo(form);
    }

    @ApiOperation(value ="Update Product Information inside bin using binSkuId")
    @RequestMapping(path="/api/bin/{binSkuId}",method = RequestMethod.PUT)
    public void updateBinInfo(@PathVariable long binSkuId,@Valid @RequestBody UpdateBinForm form) throws Exception{
        dto.updateBin(binSkuId,form);
    }

    @ApiOperation(value="Upload bin wise inventory")
    @RequestMapping(path = "/api/bin/client/{clientId}",method = RequestMethod.POST)
    public void uploadBinWiseInventory(@PathVariable long clientId,@Valid @RequestBody ValidateRequestBodyList<BinWiseInventoryForm> requestBodyList) throws ApiException {
        dto.uploadBinWiseInventory(clientId,requestBodyList.getRequestBody());
    }


}
