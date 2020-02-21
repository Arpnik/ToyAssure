package com.increff.assure.model.Pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class OrderDetailsResult{

    @NotNull
    private String productName;
    @NotNull
    private String brandId;
    @NotNull
    private Long OrderedQuantity;
    @NotNull
    private Double sellingPricePerUnit;
    @NotNull
    private Long allocatedQuantity;


}
