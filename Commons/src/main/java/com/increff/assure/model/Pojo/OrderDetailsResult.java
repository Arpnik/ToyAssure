package com.increff.assure.model.Pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class OrderDetailsResult{

    private String productName;
    private String brandId;
    private long OrderedQuantity;
    private double sellingPricePerUnit;
    private long allocatedQuantity;


}
