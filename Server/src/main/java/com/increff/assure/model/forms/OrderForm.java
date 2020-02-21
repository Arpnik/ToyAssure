package com.increff.assure.model.forms;

import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter @Setter
public class OrderForm {
    @NotNull(message = "Client Name is not valid")
    private Long clientId;
    @NotBlank(message = "Channel Order Id is not valid ")
    private String channelOrderId;
    @NotNull(message = "Customer Name is not valid")
    private Long customerId;
    @Valid
    private List<OrderLineItemForm> items;
}
