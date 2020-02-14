package com.increff.assure.controller;

import com.increff.assure.dto.OrderDto;
import com.increff.assure.model.data.ChannelItemCheckData;
import com.increff.assure.model.form.ChannelItemCheckForm;
import com.increff.assure.model.form.ChannelOrderForm;
import com.increff.assure.model.forms.OrderForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@Api
public class OrderController {
    @Autowired
    OrderDto dto;

    @ApiOperation(value = "Place Order using CSV")
    @RequestMapping(path="/api/order",method = RequestMethod.POST)
    public void placeOrder(@Valid @RequestBody OrderForm form) throws Exception {
        dto.placeOrder(form);
    }

    @ApiOperation(value = "Place Order from channel")
    @RequestMapping(path="/api/channelOrder",method = RequestMethod.POST)
    public void placeOrderByChannel(@Valid @RequestBody ChannelOrderForm form) throws Exception {
        dto.placeOrderByChannel(form);
    }

    @ApiOperation(value = "Check Order Quantity and ClientId from channel")
    @RequestMapping(path="/api/order/product",method = RequestMethod.POST)
    public ChannelItemCheckData checkOrderedItem(@Valid @RequestBody ChannelItemCheckForm form) throws Exception {
        return dto.checkOrderedItem(form);
    }

}
