package com.increff.assure.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(
        indexes = @Index(name="channel_listing",columnList = "channelId,channelSkuId,clientId",unique = true)
)
@Getter @Setter
//TODO auto identity table
//TODO Long
//TODO not null validations @notnull
//TODO unique
public class ChannelListingPojo extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private Long channelId;
    @NotNull
    private String channelSkuId;
    @NotNull
    private Long globalSkuId;
    @NotNull
    private Long clientId;

}
