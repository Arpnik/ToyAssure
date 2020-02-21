package com.increff.assure.model.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;


@Getter @Setter
public class ChannelOrderLineItem{
    @NotNull(message = "Fill the Selling Price correctly")
    @Positive(message = "Selling Price cannot be less than equal to 0")
    private Double sellingPricePerUnit;
    @NotBlank(message = "Channel Sku is not valid")
    private String channelSkuId;
    @NotNull(message = "Fill the quantity correctly")
    @Positive(message = "quantity cannot be less than equal to 0")
    private Long OrderedQuantity;


}
