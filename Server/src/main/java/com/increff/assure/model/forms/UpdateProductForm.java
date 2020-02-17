package com.increff.assure.model.forms;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter @Setter
public class UpdateProductForm {

    @NotBlank(message="Enter valid Product Name")
    private String name;
    @NotBlank(message="Enter valid Brand Information")
    private String brandId;
    @NotNull(message = "Enter the MRP correctly")
    @Positive(message = "MRP cannot be less than equal to 0")
    private double mrp;
    @NotBlank(message = "Enter some Description of the product")
    private String description;
}
