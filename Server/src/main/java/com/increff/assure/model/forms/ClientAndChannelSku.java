package com.increff.assure.model.forms;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;


@Getter @Setter
public class ClientAndChannelSku {
    @NotBlank(message = "Client Sku is not valid")
    private String clientSku;
    @NotBlank(message = "Channel SKu is not valid")
    private String channelSku;
}
