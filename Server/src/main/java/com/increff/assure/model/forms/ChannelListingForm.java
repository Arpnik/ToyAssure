package com.increff.assure.model.forms;

import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Getter @Setter
public class ChannelListingForm {
    @NotNull(message = "Client is not valid")
    private Long clientId;
    @NotNull(message = "Channel is not valid")
    private Long channelId;

    @Valid @Size(min=1,max=5000,message = "Number of rows needs to be between 1 and 5,000")
    private List<ClientAndChannelSku> clientAndChannelSkuList;

}
