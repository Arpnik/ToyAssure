package com.increff.assure.controller;

import com.increff.assure.dto.OrderDto;
import com.increff.assure.model.Exception.ApiException;
import com.increff.assure.model.data.ChannelItemCheckData;
import com.increff.assure.model.data.OrderDetailsData;
import com.increff.assure.model.data.OrderDisplayData;
import com.increff.assure.model.form.ChannelItemCheckForm;
import com.increff.assure.model.form.ChannelOrderForm;
import com.increff.assure.model.forms.OrderForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@Api
public class OrderController {
    @Autowired
    OrderDto dto;

    @ApiOperation(value = "Place Order using CSV")
    @RequestMapping(path="/api/order",method = RequestMethod.POST)
    public void placeOrder(@Valid @RequestBody OrderForm form) throws ApiException {
        dto.placeOrder(form);
    }

    @ApiOperation(value = "Place Order from channel")
    @RequestMapping(path="/api/channelOrder",method = RequestMethod.POST)
    public void placeOrderByChannel(@Valid @RequestBody ChannelOrderForm form) throws ApiException {
        dto.placeOrderByChannel(form);
    }

    @ApiOperation(value = "Check Order Quantity and ClientId from channel")
    @RequestMapping(path="/api/order/product",method = RequestMethod.POST)
    public ChannelItemCheckData checkOrderedItem(@Valid @RequestBody ChannelItemCheckForm form) throws ApiException {
        return dto.checkOrderedItem(form);
    }

    @ApiOperation(value = "Show All Orders")
    @RequestMapping(path = "/api/order",method = RequestMethod.GET)
    public List<OrderDisplayData> getAllOrders() {
        return dto.getAll();
    }

    @ApiOperation(value="Get order Details from Order ID")
    @RequestMapping(path = "/api/order/{orderId}",method = RequestMethod.GET)
    public List<OrderDetailsData> getOrderDetails(@PathVariable long orderId)
    {
        return dto.getOrderDetails(orderId);
    }

    @ApiOperation(value="Allocate order from Order ID")
    @RequestMapping(path = "/api/order/{orderId}",method = RequestMethod.PUT)
    public void allocateOrder(@PathVariable long orderId) throws ApiException {
         dto.allocateOrder(orderId);
    }

    @ApiOperation(value = "Generate Invoice")
    @RequestMapping(path="api/order/invoice/{orderId}",method = RequestMethod.GET)
    public byte[] createInvoice(@PathVariable long orderId) throws Exception {
        return dto.createInvoice(orderId);
    }

}
