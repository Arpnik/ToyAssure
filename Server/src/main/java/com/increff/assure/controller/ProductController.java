package com.increff.assure.controller;

import com.increff.assure.dto.ProductDto;
import com.increff.assure.model.data.ProductData;
import com.increff.assure.model.forms.ProductForm;
import com.increff.assure.model.forms.UpdateProductForm;
import com.increff.assure.util.ValidateRequestBodyList;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.util.List;

@Api
@RestController
public class ProductController {

    @Autowired
    private ProductDto dto;

    @ApiOperation(value = "Add Product information")
    @RequestMapping(path = "/api/product/{clientId}", method = RequestMethod.POST)
    public void add(@PathVariable long clientId,@Size(min=1,max=2,message = "Number of rows needs to be between 1 and 5,000") @Valid @RequestBody  ValidateRequestBodyList<ProductForm> requestBodyList) throws Exception {
        List<ProductForm> formList= requestBodyList.getRequestBody();
        dto.add(formList,clientId);
    }

    @ApiOperation(value = "Get all Product information with Client Id")
    @RequestMapping(path = "/api/product/client/{clientId}", method = RequestMethod.GET)
    public List<ProductData> getAllProductsByClientId(@PathVariable long clientId) throws Exception {
       return dto.getAllByClientId(clientId);
    }

    @ApiOperation(value = "Get Product information by Global SkuId")
    @RequestMapping(path = "/api/product/{id}", method = RequestMethod.GET)
    public ProductData getProduct(@PathVariable long id) throws Exception {
        return dto.get(id);
    }

    @ApiOperation(value="Update Product Information by global SkuId")
    @RequestMapping(path="/api/product/{id}",method = RequestMethod.PUT)
    public void updateProduct(@PathVariable long id,@Valid @RequestBody UpdateProductForm form) throws Exception {
        dto.update(id,form);
    }

}
