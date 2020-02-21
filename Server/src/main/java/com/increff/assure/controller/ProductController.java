package com.increff.assure.controller;

import com.increff.assure.dto.ProductDto;
import com.increff.assure.model.Exception.ApiException;
import com.increff.assure.model.data.ProductData;
import com.increff.assure.model.forms.ProductListForm;
import com.increff.assure.model.forms.UpdateProductForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api
@RestController
@RequestMapping("/api/product")
public class ProductController {

    @Autowired
    private ProductDto dto;

    @ApiOperation(value = "Add product information")
    @RequestMapping(path = "/{clientId}", method = RequestMethod.POST)
    public void add( @PathVariable Long clientId,@Valid @RequestBody ProductListForm list) throws ApiException {
        dto.add(list.getProductList(), clientId);
    }

    @ApiOperation(value = "Get all products by clientId")
    @RequestMapping(path = "/client/{clientId}", method = RequestMethod.GET)
    public List<ProductData> getAllProductsByClientId( @PathVariable Long clientId) throws ApiException {
       return dto.getAllByClientId(clientId);
    }

    @ApiOperation(value = "Get Product information by Global SkuId")
    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public ProductData getProduct(@PathVariable Long id) throws ApiException {
        return dto.get(id);
    }

    @ApiOperation(value="Update Product Information by global SkuId")
    @RequestMapping(path="/{id}",method = RequestMethod.PUT)
    public void updateProduct(@PathVariable Long id,@Valid @RequestBody UpdateProductForm form) throws ApiException {
        dto.update(id,form);
    }

}
