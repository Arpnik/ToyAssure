package com.increff.assure.model.data;

import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter @Setter
public class InvoiceMetaData {

    private long orderId;
    private String channelOrderId;
    private long clientId;
    private long customerId;
    private String customerName;
    private String clientName;
    private String channelName;
    private ZonedDateTime createdDate;


}
