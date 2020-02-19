package com.increff.assure.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(
        indexes = @Index(name="channel_listing",columnList = "channelId,channelSkuId,clientId",unique = true)
)
@Getter @Setter
public class ChannelListingPojo extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long channelId;
    private String channelSkuId;
    private long globalSkuId;
    private long clientId;

}
