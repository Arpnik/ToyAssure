package com.increff.assure.service;

import com.increff.assure.dao.ChannelDao;
import com.increff.assure.dao.ChannelListingDao;
import com.increff.assure.model.Exception.ApiException;
import com.increff.assure.model.constants.InvoiceType;
import com.increff.assure.pojo.ChannelListingPojo;
import com.increff.assure.pojo.ChannelPojo;
import com.increff.assure.spring.ApplicationProperties;
import com.increff.assure.util.AbstractUnitTest;
import com.increff.assure.util.dataUtil;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;

public class ChannelServiceTest extends AbstractUnitTest {

    @Autowired
    private ChannelService service;

    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    private ChannelDao dao;

    @Autowired
    private ChannelListingDao listingDao;

    private ChannelPojo channel;
    private ChannelListingPojo listing;

    @Before
    public void init()
    {
        channel= dataUtil.createChannelPojo("flipkart", InvoiceType.SELF);
        listing=dataUtil.createListing(new Long(101),"channelSku",new Long(1),new Long(2));
    }

    @Test
    public void testAddChannel() throws ApiException {
        service.addChannel(channel);
        ChannelPojo returned=dao.select(channel.getId());
        assertEquals(channel.getName(),returned.getName());
        assertEquals(channel.getInvoiceType(),returned.getInvoiceType());

    }

    @Test(expected = ApiException.class)
    public void testAddChannelInvalid() throws ApiException {
        dao.insert(channel);
        service.addChannel(channel);
    }

    @Test(expected = ApiException.class)
    public void testCheckPresenceInvalid() throws ApiException {
        service.getCheck(new Long(101));
    }

    @Test
    public void testCheckPresenceValid() throws ApiException {
        dao.insert(channel);
        ChannelPojo returned=service.getCheck(channel.getId());
        assertEquals(channel.getName(),returned.getName());
        assertEquals(channel.getInvoiceType(),returned.getInvoiceType());
    }

    @Test
    public void testGetAllChannels()
    {
        dao.insert(channel);
        assertEquals(1,service.getAllChannels().size());
    }

    @Test
    public void testGetByName() throws ApiException {
        dao.insert(channel);
        ChannelPojo returned=service.getChannelByName(channel.getName());
        assertEquals(channel.getInvoiceType(),returned.getInvoiceType());
        assertEquals(channel.getId(),returned.getId());
        assertEquals(channel.getName(),returned.getName());
    }

    @Test(expected = ApiException.class)
    public void testGetByNameInvalid() throws ApiException {
        service.getChannelByName(channel.getName());
    }

    @Test
    public void testAddChannelListing() throws ApiException {
        service.addChannelListing(listing);
        assertEquals(1,listingDao.selectAll().size());
        ChannelListingPojo returned=listingDao.selectAll().get(0);
        assertEquals(listing.getChannelId(),returned.getChannelId());
        assertEquals(listing.getChannelSkuId(),returned.getChannelSkuId());
        assertEquals(listing.getClientId(),returned.getClientId());
        assertEquals(listing.getGlobalSkuId(),returned.getGlobalSkuId());
    }

    @Test
    public void testAddChannelListingRepeated() throws ApiException {
       listingDao.insert(listing);
        service.addChannelListing(listing);
        assertEquals(1,listingDao.selectAll().size());
    }

    @Test(expected = ApiException.class)
    public void testAddChannelListingInvalid() throws ApiException {
        listingDao.insert(listing);
        ChannelListingPojo listing2=dataUtil.createListing(new Long(1),listing.getChannelSkuId(),listing.getChannelId(),listing.getClientId());
        service.addChannelListing(listing2);
    }

    @Test
    public void testGetListingByParams() throws ApiException {
        listingDao.insert(listing);
        ChannelListingPojo returned=service.getCheckByParams(listing.getChannelId(),listing.getChannelSkuId(),listing.getClientId());
        assertEquals(listing.getGlobalSkuId(),returned.getGlobalSkuId());
        assertEquals(listing.getId(),listing.getId());
        assertEquals(listing.getChannelId(),returned.getChannelId());
        assertEquals(listing.getChannelSkuId(),returned.getChannelSkuId());
        assertEquals(listing.getClientId(),returned.getClientId());
    }

    @Test(expected = ApiException.class)
    public void testGetListingByParamsInvalid() throws ApiException {
        listingDao.insert(listing);
        service.getCheckByParams(listing.getChannelId()+1,listing.getChannelSkuId(),listing.getClientId());
    }


}
