package com.increff.assure.model.forms;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@Setter
public class UpdateProductForm {

    @NotBlank(message="Please enter the correct product name")
    private String name;
    @NotBlank(message="Please enter the correct brand information")
    private String brandId;
    @NotNull(message = "Please fill the mrp correctly")
    @Positive(message = "MRP cannot be less than equal to 0")
    private double mrp;
    @NotBlank(message = "Please enter some description of the product")
    private String description;
}
