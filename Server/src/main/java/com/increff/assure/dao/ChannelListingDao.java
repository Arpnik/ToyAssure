package com.increff.assure.dao;

import com.increff.assure.pojo.ChannelListingPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;

@Repository
public class ChannelListingDao extends AbstractDao<ChannelListingPojo> {
    private static String select_channelId_channelSku_clientId="select p from ChannelListingPojo p where p.channelId=:channelId and p.channelSkuId=:channelSkuId and p.clientId=:clientId";
    private static String select_channelId_globalSkuId="select p from ChannelListingPojo p where p.channelId=:channelId and p.globalSkuId=:globalSkuId";

    public ChannelListingPojo getListingPojoByParams(String channelSkuId, Long channelId, Long clientId)
    {
        TypedQuery<ChannelListingPojo> query=getQuery(select_channelId_channelSku_clientId);
        query.setParameter("channelId",channelId);
        query.setParameter("channelSkuId",channelSkuId);
        query.setParameter("clientId",clientId);
        return getSingle(query);
    }

    public ChannelListingPojo getListingPojoByParams(Long globalSkuId, Long channelId)
    {
        TypedQuery<ChannelListingPojo> query=getQuery(select_channelId_globalSkuId);
        query.setParameter("channelId",channelId);
        query.setParameter("globalSkuId",globalSkuId);
        return getSingle(query);
    }
}
