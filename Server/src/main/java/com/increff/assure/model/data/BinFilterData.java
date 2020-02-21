package com.increff.assure.model.data;

import com.increff.assure.model.forms.BinFilterForm;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BinFilterData extends BinFilterForm {
    private String name;//client name //TODO change nclientNmae
    private Long quantity;
    private Long binSkuId;

}
