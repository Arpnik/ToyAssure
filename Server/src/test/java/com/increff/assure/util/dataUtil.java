package com.increff.assure.util;

import com.increff.assure.model.constants.InvoiceType;
import com.increff.assure.model.constants.MemberTypes;
import com.increff.assure.model.constants.OrderStatusType;
import com.increff.assure.model.form.*;
import com.increff.assure.model.forms.*;
import com.increff.assure.pojo.*;

import java.util.List;

public class dataUtil {
    public static MemberPojo createMemberPojo(String name, MemberTypes type) {
        MemberPojo member = new MemberPojo();
        member.setName(name);
        member.setType(type);
        return member;
    }

    public static BinSkuPojo createBinSkuPojo(Long qty, Long globalSku, Long binId) {
        BinSkuPojo pojo = new BinSkuPojo();
        pojo.setQuantity(qty);
        pojo.setGlobalSkuId(globalSku);
        pojo.setBinId(binId);
        return pojo;
    }

    public static OrderPojo createOrderPojo(OrderStatusType status, Long channelId, String channelOrderId, Long customerId, Long clientId) {
        OrderPojo pojo = new OrderPojo();
        pojo.setStatus(status);
        pojo.setChannelId(channelId);
        pojo.setChannelOrderId(channelOrderId);
        pojo.setCustomerId(customerId);
        pojo.setClientId(clientId);
        return pojo;
    }

    public static ChannelListingPojo createListing(Long globalSkuId, String channelSkuId, Long channelId, Long clientId) {
        ChannelListingPojo pojo = new ChannelListingPojo();
        pojo.setGlobalSkuId(globalSkuId);
        pojo.setChannelSkuId(channelSkuId);
        pojo.setChannelId(channelId);
        pojo.setClientId(clientId);
        return pojo;
    }

    public static InventoryPojo createInventoryPojo(Long globalSkuId, Long qty) {
        InventoryPojo pojo = new InventoryPojo();
        pojo.setGlobalSkuId(globalSkuId);
        pojo.setAllocatedQuantity(new Long(0));
        pojo.setFulfilledQuantity(new Long(0));
        pojo.setAvailableQuantity(qty);
        return pojo;
    }

    public static ChannelPojo createChannelPojo(String name, InvoiceType type) {
        ChannelPojo pojo = new ChannelPojo();
        pojo.setInvoiceType(type);
        pojo.setName(name);
        return pojo;
    }

    public static BinInventoryForm createBinInventoryForm(Long binId, String clientSkuId, Long qty) {
        BinInventoryForm form = new BinInventoryForm();
        form.setBinId(binId);
        form.setClientSkuId(clientSkuId);
        form.setQuantity(qty);
        return form;
    }

    public static ChannelForm createChannelForm(String name, InvoiceType type) {
        ChannelForm form = new ChannelForm();
        form.setInvoiceType(type);
        form.setName(name);
        return form;
    }

    public static ChannelListingForm createListingForm(Long clientId, Long channelId, List<ClientAndChannelSku> list) {
        ChannelListingForm form = new ChannelListingForm();
        form.setChannelId(channelId);
        form.setClientAndChannelSkuList(list);
        form.setClientId(clientId);
        return form;
    }

    public static OrderForm createOrderForm(String channelOrderId, Long clientId, Long customerId, List<OrderLineItemForm> items) {
        OrderForm form = new OrderForm();
        form.setChannelOrderId(channelOrderId);
        form.setClientId(clientId);
        form.setCustomerId(customerId);
        form.setItems(items);
        return form;
    }

    public static OrderLineItemForm createOrderLineItem(String clientSkuId, Long qty, Double price) {
        OrderLineItemForm form = new OrderLineItemForm();
        form.setClientSkuId(clientSkuId);
        form.setOrderedQuantity(qty);
        form.setSellingPricePerUnit(price);
        return form;
    }

    public static ChannelOrderForm createChannelOrderForm(String channelOrderId, Long clientId, Long customerId,Long channelId, List<ChannelOrderLineItem> items) {
        ChannelOrderForm form = new ChannelOrderForm();
        form.setItems(items);
        form.setCustomerId(customerId);
        form.setClientId(clientId);
        form.setChannelId(channelId);
        form.setChannelOrderId(channelOrderId);
        return form;
    }

    public static ChannelItemCheckForm createChannelItemCheckForm(Long qty,Long clientId,Long channelId,String channelSkuId)
    {
        ChannelItemCheckForm form=new ChannelItemCheckForm();
        form.setQuantity(qty);
        form.setClientId(clientId);
        form.setChannelId(channelId);
        form.setChannelSkuId(channelSkuId);
        return form;
    }

    public static ChannelOrderLineItem createChannelItem(String channelSkuId, Long qty, Double price) {
        ChannelOrderLineItem item = new ChannelOrderLineItem();
        item.setChannelSkuId(channelSkuId);
        item.setOrderedQuantity(qty);
        item.setSellingPricePerUnit(price);
        return item;
    }

    public static ClientAndChannelSku createClientAndChannelSku(String channelSKu, String clientSku) {
        ClientAndChannelSku sku = new ClientAndChannelSku();
        sku.setClientSku(clientSku);
        sku.setChannelSku(channelSKu);
        return sku;
    }

    public static UpdateBinForm createUpdateBinForm(Long qty) {
        UpdateBinForm form = new UpdateBinForm();
        form.setQuantity(qty);
        return form;
    }

    public static OrderItemPojo createOrderItem(Long globalSkuId, Long qty, Long orderId, Double price, Long allocated, Long fulfilled) {
        OrderItemPojo pojo = new OrderItemPojo();
        pojo.setGlobalSkuId(globalSkuId);
        pojo.setOrderedQuantity(qty);
        pojo.setOrderId(orderId);
        pojo.setSellingPricePerUnit(price);
        pojo.setAllocatedQuantity(allocated);
        pojo.setFulfilledQuantity(fulfilled);
        return pojo;
    }

    public static CreateBinForm createNumberBinForm(Long numberOfBins) {
        CreateBinForm form = new CreateBinForm();
        form.setNumberOfBins(numberOfBins);
        return form;
    }

    public static MemberForm createMemberForm(String name, MemberTypes type) {
        MemberForm form = new MemberForm();
        form.setName(name);
        form.setType(type);
        return form;
    }

    public static ProductForm createProductForm(String name, String brandId, String clientSkuId, String desc, Double mrp) {
        ProductForm form = new ProductForm();
        form.setBrandId(brandId);
        form.setDescription(desc);
        form.setClientSkuId(clientSkuId);
        form.setMrp(mrp);
        form.setName(name);
        return form;
    }

    public static ProductPojo createProductPojo(String name, String brandId, String clientSkuId, String desc, Double mrp, Long clientId) {
        ProductPojo pojo = new ProductPojo();
        pojo.setName(name);
        pojo.setMrp(mrp);
        pojo.setBrandId(brandId);
        pojo.setDescription(desc);
        pojo.setClientId(clientId);
        pojo.setClientSkuId(clientSkuId);
        return pojo;
    }

    public static BinFilterForm createBinFilterForm(Long binId, Long clientId, String clientSkuId) {
        BinFilterForm form = new BinFilterForm();
        form.setBinId(binId);
        form.setClientId(clientId);
        form.setClientSkuId(clientSkuId);
        return form;

    }
}
