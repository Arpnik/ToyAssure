package com.increff.assure.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Table(
        indexes = @Index(name="uniqueConstraintChannelListing1",columnList = "channelId,channelSkuId,clientId",unique = true)
)
@Getter @Setter @Entity
public class ChannelListingPojo extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long channelId;
    private String channelSkuId;
    private long globalSkuId;
    private long clientId;

}
