package com.increff.assure.model.data;

import com.increff.assure.model.Pojo.OrderDetailsResult;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class OrderDetailsData extends OrderDetailsResult {


    public OrderDetailsData(String productName, String brandId, Long OrderedQuantity, Double sellingPricePerUnit, Long allocatedQuantity) {
        super(productName, brandId, OrderedQuantity, sellingPricePerUnit, allocatedQuantity);
    }
}
