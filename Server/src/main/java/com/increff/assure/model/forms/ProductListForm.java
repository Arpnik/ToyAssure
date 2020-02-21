package com.increff.assure.model.forms;

import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import java.util.List;

@Getter @Setter
public class ProductListForm {
    @Valid
    List<ProductForm> productList;
}
