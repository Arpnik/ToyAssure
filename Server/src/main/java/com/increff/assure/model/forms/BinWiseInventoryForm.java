package com.increff.assure.model.forms;

import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import java.util.List;

@Getter @Setter
public class BinWiseInventoryForm {
    @Valid
    List<BinInventoryForm> binList;
}
