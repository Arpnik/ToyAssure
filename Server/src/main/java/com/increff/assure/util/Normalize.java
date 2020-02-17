package com.increff.assure.util;

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

    public static void normalize(BinFilterForm form)
    {
        form.setClientSkuId(form.getClientSkuId().trim());
    }
}
