package com.increff.assure.controller;

import com.increff.assure.dto.BinDto;
import com.increff.assure.model.Exception.ApiException;
import com.increff.assure.model.data.BinFilterData;
import com.increff.assure.model.forms.BinFilterForm;
import com.increff.assure.model.forms.BinWiseInventoryForm;
import com.increff.assure.model.forms.CreateBinForm;
import com.increff.assure.model.forms.UpdateBinForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api
@RestController
@RequestMapping("/api/bin")
public class BinController {
    @Autowired
    private BinDto dto;

    @ApiOperation(value = "Create bins")
    @RequestMapping(path = "", method = RequestMethod.POST)
    public void addBins(@Valid @RequestBody CreateBinForm form)  {
        dto.createBins(form);
    }

    @ApiOperation(value="Get information of product depending upon binId, clientId and clientSku")
    @RequestMapping(path="/product", method = RequestMethod.POST)
    public List<BinFilterData> getProductInfo(@RequestBody BinFilterForm form) throws ApiException {
        return dto.getProductInfo(form);
    }

    @ApiOperation(value ="Update product information inside bin using binSkuId")
    @RequestMapping(path="/{binSkuId}", method = RequestMethod.PUT)
    public void updateBinInfo(@PathVariable Long binSkuId, @Valid @RequestBody UpdateBinForm form) throws Exception{
        dto.updateBin(binSkuId,form);
    }

    //TODO Restfull services learn

    @ApiOperation(value="Upload bin wise inventory")
    @RequestMapping(path = "/client/{clientId}",method = RequestMethod.POST)
    public void uploadBinWiseInventory(@PathVariable Long clientId, @Valid @RequestBody BinWiseInventoryForm list) throws ApiException {
        dto.uploadBinWiseInventory(clientId,list.getBinList());
    }


}
