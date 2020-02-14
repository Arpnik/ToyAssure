package com.increff.assure.model.data;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter @Setter
public class ChannelItemCheckData {
    @NotNull(message = "Quantity is not valid")
    @Positive(message = "Quantity cannot be less than or equal to zero")
    private long quantity;

}
