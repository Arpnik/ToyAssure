package com.increff.assure.model.forms;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter @Setter
public class ProductForm extends UpdateProductForm {

    @NotBlank(message="Please enter the correct client SKU information")
    private String clientSkuId;

}
