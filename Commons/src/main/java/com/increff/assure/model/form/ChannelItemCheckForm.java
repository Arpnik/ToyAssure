package com.increff.assure.model.form;

import com.increff.assure.model.data.ChannelItemCheckData;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter @Setter
public class ChannelItemCheckForm extends ChannelItemCheckData {

    @NotNull(message = "Client ID is not valid")
    private long clientId;

    @NotBlank(message = "Channel Sku is not valid")
    private String channelSkuId;



}
