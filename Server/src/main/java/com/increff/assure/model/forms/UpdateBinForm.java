package com.increff.assure.model.forms;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;


@Getter @Setter
public class UpdateBinForm {
    @NotNull(message = "Please enter the quantity of the product")
    @Positive(message = "Entered quantity cannot be less than equal to zero")
    private Long quantity;
}
