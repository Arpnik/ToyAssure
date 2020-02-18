package com.increff.assure.util;

import com.increff.assure.model.form.ChannelItemCheckForm;
import com.increff.assure.model.form.ChannelOrderForm;
import com.increff.assure.model.form.ChannelOrderLineItem;
import com.increff.assure.model.form.MemberForm;
import com.increff.assure.model.forms.*;

public class Normalize {


    public static void normalize(MemberForm form)
    {
        form.setName(StringUtil.toLowerCase(form.getName()));
    }

    public static void normalize(UpdateProductForm form)
    {
        form.setDescription(StringUtil.toLowerCase(form.getDescription()));
        form.setName(StringUtil.toLowerCase(form.getName()));
        form.setBrandId(StringUtil.toLowerCase(form.getBrandId()));
    }

    public static void normalizeProductForm(ProductForm form)
    {
        normalize(form);
        form.setClientSkuId(form.getClientSkuId().trim());
    }

    public static void normalizeSku(ClientAndChannelSku sku)
    {
        sku.setChannelSku(sku.getChannelSku().trim());
        sku.setClientSku(sku.getClientSku().trim());
    }

    public  static void normalize(ChannelOrderForm form) {
        form.setChannelOrderId(form.getChannelOrderId().trim());
        for(ChannelOrderLineItem item: form.getItems())
        {
            item.setChannelSkuId(item.getChannelSkuId().trim());
        }
    }


    public static void normalize(OrderForm form) {
        form.setChannelOrderId(form.getChannelOrderId().trim());
        for(OrderLineItemForm item:form.getItems())
        {
            item.setClientSkuId(item.getClientSkuId().trim());
        }
    }

    public static void normalize(ChannelItemCheckForm form)
    {
        form.setChannelSkuId(form.getChannelSkuId().trim());
    }


    public static void normalize(BinFilterForm form)
    {
        form.setClientSkuId(form.getClientSkuId().trim());
    }
}
