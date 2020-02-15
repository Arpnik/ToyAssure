package com.increff.assure.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class OrderDetailsResult {

    private String productName;
    private String brandId;
    private long OrderedQuantity;
    private long allocatedQuantity;
    private double sellingPricePerUnit;

}
