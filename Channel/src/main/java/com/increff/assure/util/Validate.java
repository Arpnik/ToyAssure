package com.increff.assure.util;

import com.increff.assure.model.Exception.ApiException;
import com.increff.assure.model.data.InvoiceMetaData;

public class Validate {
    public static void validate(InvoiceMetaData data) throws ApiException {
        if (data == null)
            throw new ApiException("Invoice Data is empty / Order not found");

        if (data.getItems() == null)
            throw new ApiException("Order is empty");
    }
}
