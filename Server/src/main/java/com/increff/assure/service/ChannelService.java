package com.increff.assure.service;

import com.increff.assure.dao.ChannelDao;
import com.increff.assure.dao.ChannelListingDao;
import com.increff.assure.model.Exception.ApiException;
import com.increff.assure.pojo.ChannelListingPojo;
import com.increff.assure.pojo.ChannelPojo;
import com.increff.assure.spring.ApplicationProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ChannelService {
    @Autowired
    private ChannelDao channelDao;

    @Autowired
    private ApplicationProperties properties;


    @Autowired
    private ChannelListingDao channelListingDao;

    @Transactional(rollbackFor = ApiException.class)
    public void addChannel(ChannelPojo pojo) throws ApiException {
        ChannelPojo existing = channelDao.selectByName(pojo.getName());
        if (existing != null) {
            throw new ApiException("Channel with name:"+pojo.getName()+" already exists");
        }
        channelDao.insert(pojo);
    }

    @Transactional(rollbackFor = ApiException.class)
    public void addChannelListing(ChannelListingPojo pojo) throws ApiException {
        ChannelListingPojo existingChannel = channelListingDao.getListingPojoByParams(pojo.getChannelSkuId(), pojo.getChannelId(), pojo.getClientId());
        if (existingChannel != null) {
            if (existingChannel.getGlobalSkuId() == pojo.getGlobalSkuId())
                return;

            throw new ApiException("Channel SKU:" + pojo.getChannelSkuId() + " is not valid");
        }

        channelListingDao.insert(pojo);
    }

    @Transactional(readOnly = true)
    public List<ChannelPojo> getAllChannels() {
        return channelDao.selectAll();
    }

    @Transactional(readOnly = true)
    public ChannelPojo getDefaultChannel() throws ApiException {
        return getChannelByName(properties.getDefaultChannelName());
    }

    @Transactional(readOnly = true)
    public ChannelPojo getCheck(Long channelId) throws ApiException {
        ChannelPojo pojo = channelDao.select(channelId);
        if (pojo == null)
            throw new ApiException("Channel doesn't exist for channel ID:" + channelId);
        return pojo;
    }


    @Transactional(readOnly = true)
    public ChannelPojo getChannelByName(String name) throws ApiException {
        ChannelPojo pojo = channelDao.selectByName(name);
        if (pojo == null) {
            throw new ApiException("Channel Name:" + name + " doesn't exist");
        }
        return pojo;
    }


    @Transactional(readOnly = true)
    public ChannelListingPojo getCheckByParams(Long channelId, String channelSkuId, Long clientId) throws ApiException {
        ChannelListingPojo pojo = channelListingDao.getListingPojoByParams(channelSkuId, channelId, clientId);
        if (pojo == null) {
            throw new ApiException("Channel SKu:" + channelSkuId + " is not valid");
        }
        return pojo;
    }


}
