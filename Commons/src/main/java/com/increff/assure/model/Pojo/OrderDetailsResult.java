package com.increff.assure.model.Pojo;

import com.increff.assure.model.data.ChannelItemCheckData;
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
