package com.increff.assure.pojo;

import com.increff.assure.model.constants.OrderStatusType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(
        indexes = @Index(name="channelId_channelOrderId",columnList = "channelId,channelOrderId",unique = true)
)
@Getter
@Setter
public class OrderPojo extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private Long clientId;
    @NotNull
    private Long channelId;
    @NotNull
    private Long customerId;
    @NotNull
    private String channelOrderId;
    @NotNull
    @Enumerated(value = EnumType.STRING)
    private OrderStatusType status=OrderStatusType.CREATED;
}
