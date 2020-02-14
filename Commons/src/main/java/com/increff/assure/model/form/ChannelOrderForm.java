package com.increff.assure.model.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter @Setter
public class ChannelOrderForm{
    @NotBlank(message = "Channel Name is not valid")
    private String channelName;
    @NotNull(message = "Client ID is not valid")
    private long clientId;
    @NotNull(message = "Customer ID is not valid")
    private long customerId;
    @NotBlank(message = "Channel Order ID is not valid")
    private String channelOrderId;
    @Valid
    private List<ChannelOrderLineItem> items;

}
