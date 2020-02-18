package com.increff.assure.model.data;

import com.increff.assure.model.Pojo.OrderDetailsResult;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class OrderDetailsData extends OrderDetailsResult {


    public OrderDetailsData(String productName, String brandId, long OrderedQuantity, double sellingPricePerUnit, long allocatedQuantity) {
        super(productName, brandId, OrderedQuantity, sellingPricePerUnit, allocatedQuantity);
    }
}
