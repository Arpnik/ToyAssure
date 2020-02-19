package com.increff.assure.model.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class ChannelItemCheckData {

    private String productName;
    private String brandId;
    private long OrderedQuantity;
    private double sellingPricePerUnit;

}
