package com.increff.assure.util;

import com.increff.assure.model.Exception.ApiException;
import com.increff.assure.model.data.InvoiceMetaData;
import com.increff.assure.model.forms.BinFilterForm;

import java.util.Optional;

public class Validate {

    public static void validate(BinFilterForm form) throws ApiException {
        if(form.getClientSkuId() != null && form.getClientSkuId().trim().length()==0)
        {
            form.setClientSkuId(null);
        }
        if(form.getClientId()!=null && form.getClientId()==0)
        {
            form.setClientId(null);
        }
        if ((form.getClientSkuId() != null && form.getClientId() == null) || (form.getClientSkuId() == null && form.getClientId() != null)) {
            throw new ApiException("Select appropriate client name and client SKU");
        }

        if (form.getBinId() == null && form.getClientSkuId() == null) {
            throw new ApiException("Set some values in the  filter form");
        }

    }

    public static void validate(InvoiceMetaData data) throws ApiException {
        if(data == null)
            throw new ApiException("Invoice Data is empty / Order not found");

        if(data.getItems() == null)
            throw new ApiException("Order is empty");
    }
}
