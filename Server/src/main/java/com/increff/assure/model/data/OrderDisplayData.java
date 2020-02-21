package com.increff.assure.model.data;

import com.increff.assure.model.constants.OrderStatusType;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter @Setter
public class OrderDisplayData {

    private Long orderId;
    private ZonedDateTime createdDate;
    private OrderStatusType status;
    private String channelOrderId;
}
