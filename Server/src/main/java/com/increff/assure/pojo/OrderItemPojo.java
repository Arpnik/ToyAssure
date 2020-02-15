package com.increff.assure.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity @Getter @Setter
//TODO unique constraint orderID
public class OrderItemPojo extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long orderId;
    private long globalSkuId;
    private long orderedQuantity;
    private long allocatedQuantity;
    private long fulfilledQuantity;
    private double sellingPricePerUnit;
}
