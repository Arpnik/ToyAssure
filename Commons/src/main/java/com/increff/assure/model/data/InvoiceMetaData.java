package com.increff.assure.model.data;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class InvoiceMetaData {

    private long id;//orderId
    private String channelOrderId;
    private String customerName;
    private String clientName;
    private String channelName;
    private String orderedDate;
    private List<OrderDetailsData> items;


}
