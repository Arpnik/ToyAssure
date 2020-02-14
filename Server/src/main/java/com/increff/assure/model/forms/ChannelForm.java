package com.increff.assure.model.forms;

import com.increff.assure.model.constants.InvoiceType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChannelForm {

    private String name;
    private InvoiceType invoiceType;

}
