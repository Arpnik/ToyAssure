package com.increff.assure.util;

import com.increff.assure.model.constants.InvoiceType;
import com.increff.assure.model.constants.MemberTypes;
import com.increff.assure.model.data.ChannelData;
import com.increff.assure.model.data.ChannelItemCheckData;
import com.increff.assure.model.data.MemberData;
import com.increff.assure.model.form.ChannelItemCheckForm;
import com.increff.assure.model.form.ChannelOrderForm;

public class DataUtil {

    public static MemberData createMemberData(Long id, String name, MemberTypes type) {
        MemberData data = new MemberData();
        data.setId(id);
        data.setName(name);
        data.setType(type);
        return data;
    }

    public static ChannelData createChannelData(String name, InvoiceType type) {
        ChannelData data = new ChannelData();
        data.setInvoiceType(type);
        data.setName(name);
        return data;
    }

    public static ChannelItemCheckData createChannelItemData(String name, String brandId, Long qty, Double price) {
        ChannelItemCheckData data = new ChannelItemCheckData();
        data.setProductName(name);
        data.setBrandId(brandId);
        data.setOrderedQuantity(qty);
        data.setSellingPricePerUnit(price);
        return data;
    }

    //TODO github conflict learn
    public static ChannelItemCheckForm createItemForm(String skuId, Long channelId, long clientId, long qty) {
        ChannelItemCheckForm form = new ChannelItemCheckForm();
        form.setChannelSkuId(skuId);
        form.setChannelId(channelId);
        form.setClientId(clientId);
        form.setQuantity(qty);
        return form;
    }


    public static ChannelOrderForm createOrder(long channelId, String channelOrderId, long clientId, long customerId) {
        ChannelOrderForm form = new ChannelOrderForm();
        form.setChannelId(channelId);
        form.setChannelOrderId(channelOrderId);
        form.setClientId(clientId);
        form.setCustomerId(customerId);
        form.setItems(null);
        return form;
    }

}
