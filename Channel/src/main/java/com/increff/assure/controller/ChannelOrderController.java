package com.increff.assure.controller;

import com.increff.assure.dto.ChannelOrderDto;
import com.increff.assure.model.data.ChannelData;
import com.increff.assure.model.data.ChannelItemCheckData;
import com.increff.assure.model.data.InvoiceMetaData;
import com.increff.assure.model.data.MemberData;
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

    @ApiOperation(value = "Check order fulfillability")
    @RequestMapping(path = "/api/channel/product", method = RequestMethod.POST)
    public ChannelItemCheckData checkOrderFulFillable(@Valid @RequestBody ChannelItemCheckForm item) throws Exception {
        return dto.checkOrderItem(item);
    }

    @ApiOperation(value = "Get all clients")
    @RequestMapping(path="/api/channel/client",method = RequestMethod.GET)
    public MemberData[] getClients()
    {
        return dto.getClients();
    }

    @ApiOperation(value = "Get all customers")
    @RequestMapping(path="/api/channel/customer",method = RequestMethod.GET)
    public MemberData[] getCustomers()
    {
        return dto.getCustomers();
    }

    @ApiOperation(value = "Get all channels")
    @RequestMapping(path="/api/channel",method = RequestMethod.GET)
    public ChannelData[] getChannels()
    {
        return dto.getChannels();
    }

    @ApiOperation(value = "generate invoice and send to server")
    @RequestMapping(path="api/channel/invoice",method = RequestMethod.POST)
    public byte[] getInvoice(@RequestBody InvoiceMetaData data) throws Exception {
        return dto.getInvoice(data);
    }

}
