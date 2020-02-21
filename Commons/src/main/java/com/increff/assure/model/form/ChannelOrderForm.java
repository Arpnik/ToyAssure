package com.increff.assure.model.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter @Setter
public class ChannelOrderForm{
    @NotNull(message = "Client ID is not valid")
    private Long channelId;
    @NotNull(message = "Client ID is not valid")
    private Long clientId;
    @NotNull(message = "Customer ID is not valid")
    private Long customerId;
    @NotBlank(message = "Channel Order ID is not valid")
    private String channelOrderId;
    @Valid
    private List<ChannelOrderLineItem> items;

}
