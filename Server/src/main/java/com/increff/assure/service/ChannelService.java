package com.increff.assure.service;

import com.increff.assure.dao.ChannelDao;
import com.increff.assure.dao.ChannelListingDao;
import com.increff.assure.pojo.ChannelListingPojo;
import com.increff.assure.pojo.ChannelPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ChannelService {
    @Autowired
    private ChannelDao channelDao;

    @Value("${defaultMember.name}")
    private String defaultName;


    @Autowired
    private ChannelListingDao channelListingDao;

    @Transactional(rollbackFor = ApiException.class)
    public void addChannel(ChannelPojo pojo) throws ApiException {
        ChannelPojo existing = channelDao.selectByName(pojo.getName());
        if(existing!=null){
            throw new ApiException("Channel with same name already exists");
        }
        channelDao.insert(pojo);
    }

    @Transactional(rollbackFor = ApiException.class)
    public void checkPresenceOfChannel(long id) throws ApiException {
        ChannelPojo pojo= channelDao.select(id);
        if(pojo==null)
        {
            throw new ApiException("Channel doesn't exists");
        }
    }

    @Transactional(rollbackFor = ApiException.class)
    public void addChannelListing(ChannelListingPojo pojo,boolean callDbPersist) throws ApiException {
        ChannelListingPojo existingChannel=channelListingDao.getListingPojoFromChannelIdAndSkuAndClientId(pojo.getChannelSkuId(),pojo.getChannelId(),pojo.getClientId());
        if(existingChannel!=null)
        {
            if(existingChannel.getGlobalSkuId()==pojo.getGlobalSkuId())
                return;

            throw new ApiException("Channel SKu:"+pojo.getChannelSkuId()+" is not valid.");
        }

        if(callDbPersist){
        channelListingDao.insert(pojo);}
    }

    @Transactional(readOnly = true)
    public List<ChannelPojo> getAllChannels()
    {
        return channelDao.selectAll();
    }

    @Transactional(readOnly = true)
    public ChannelPojo getDefaultChannel() throws ApiException {
        ChannelPojo pojo=getChannelByName(defaultName);
        return pojo;
    }

    @Transactional(rollbackFor = ApiException.class,readOnly = true)
    public ChannelPojo getChannelByName(String name) throws ApiException {
        ChannelPojo pojo=channelDao.selectByName(name);
        if(pojo==null)
        {
            throw new ApiException("Channel Name:"+name+" doesn't exist");
        }
        return pojo;
    }


    public ChannelListingPojo getByChannelIdAndChannelSkuAndClientId(long channelId, String channelSkuId, long clientId) throws ApiException {
        ChannelListingPojo pojo=channelListingDao.getListingPojoFromChannelIdAndSkuAndClientId(channelSkuId,channelId,clientId);
        if(pojo==null)
        {
            throw new ApiException("Channel SKu:"+channelSkuId+" is not valid.");
        }
        return pojo;
    }



}
