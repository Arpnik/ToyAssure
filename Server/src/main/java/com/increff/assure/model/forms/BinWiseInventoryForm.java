package com.increff.assure.model.forms;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter @Setter
public class BinWiseInventoryForm extends UpdateBinForm{

    @NotNull(message = "BinId is not valid")
    private long binId;
    @NotBlank(message="Client Sku information is incomplete")
    private String clientSkuId;

}
