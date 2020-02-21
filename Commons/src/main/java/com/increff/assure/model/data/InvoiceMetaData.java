package com.increff.assure.model.data;

import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.util.List;

@Getter @Setter
//TODO naming validation
public class InvoiceMetaData {

    private Long id;//orderId
    private String channelOrderId;
    private String customerName;
    private String clientName;
    private String channelName;
    private String orderedDate;
    @Valid @Size(min=1,message = "ordered Items cannot be less than 1")
    private List<OrderDetailsData> items;


}
