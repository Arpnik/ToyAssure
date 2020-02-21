package com.increff.assure.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
@Getter @Setter
public class OrderItemPojo extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private Long orderId;
    @NotNull
    private Long globalSkuId;
    @NotNull
    private Long orderedQuantity;
    @NotNull
    private Long allocatedQuantity;
    @NotNull
    private Long fulfilledQuantity;
    @NotNull
    private Double sellingPricePerUnit;
}
