package com.increff.assure.controller;

import com.increff.assure.dto.ChannelOrderDto;
import com.increff.assure.model.form.ChannelItemCheckForm;
import com.increff.assure.model.form.ChannelOrderForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Api
@RestController
public class ChannelOrderController {

    @Autowired
    private ChannelOrderDto dto;

    @ApiOperation(value = "Place order through channel")
    @RequestMapping(path = "/api/channel/order", method = RequestMethod.POST)
    public void placeOrder(@Valid @RequestBody ChannelOrderForm form){
        dto.placeOrder(form);
    }

    @ApiOperation(value = "check order fulfillability by channel")
    @RequestMapping(path = "/api/channel/product", method = RequestMethod.POST)
    public void checkOrderFulFillable(@Valid @RequestBody ChannelItemCheckForm item) throws Exception {
        dto.checkOrderItem(item);
    }

}
