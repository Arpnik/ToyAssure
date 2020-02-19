package com.increff.assure.pojo;

import com.increff.assure.model.constants.OrderStatusType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(
        indexes = @Index(name="channelId_channelOrderId",columnList = "channelId,channelOrderId",unique = true)
)
@Getter @Setter
public class OrderPojo extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long clientId;
    private long channelId;
    private long customerId;
    private String channelOrderId;
    @Column(columnDefinition = "varchar(255) default 'CREATED'")
    @Enumerated(value = EnumType.STRING)
    private OrderStatusType status=OrderStatusType.CREATED;
}
