package com.increff.assure.model.forms;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter @Setter
public class OrderLineItemForm {
    @NotBlank(message = "ClientSku is not valid")
    private String clientSkuId;
    @NotNull(message = "Fill the Selling Price correctly")
    @Positive(message = "Selling Price cannot be less than equal to 0")
    private double sellingPricePerUnit;
    @NotNull(message = "Fill the Quantity correctly")
    @Positive(message = "Quantity Ordered cannot be less than equal to 0")
    private long orderedQuantity;
}
