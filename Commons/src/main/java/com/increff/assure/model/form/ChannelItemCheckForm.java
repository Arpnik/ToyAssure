package com.increff.assure.model.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter @Setter
public class ChannelItemCheckForm{

    @NotNull(message = "Client ID is not valid")
    private Long clientId;

    @NotBlank(message = "Channel Sku is not valid")
    private String channelSkuId;

    @NotNull(message = "ChannelId is not valid")
    private Long channelId;

    @NotNull(message = "Quantity is not valid")
    @Positive(message = "Quantity cannot be less than or equal to zero")
    private Long quantity;



}
