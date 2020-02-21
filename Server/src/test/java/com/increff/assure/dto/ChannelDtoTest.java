package com.increff.assure.dto;

import com.increff.assure.dao.ChannelListingDao;
import com.increff.assure.dao.MemberDao;
import com.increff.assure.dao.ProductDao;
import com.increff.assure.model.Exception.ApiException;
import com.increff.assure.model.constants.InvoiceType;
import com.increff.assure.model.constants.MemberTypes;
import com.increff.assure.model.form.ChannelForm;
import com.increff.assure.model.forms.ChannelListingForm;
import com.increff.assure.model.forms.ClientAndChannelSku;
import com.increff.assure.pojo.ChannelListingPojo;
import com.increff.assure.pojo.ChannelPojo;
import com.increff.assure.pojo.MemberPojo;
import com.increff.assure.pojo.ProductPojo;
import com.increff.assure.service.ChannelService;
import com.increff.assure.spring.ApplicationProperties;
import com.increff.assure.util.AbstractUnitTest;
import com.increff.assure.util.dataUtil;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ChannelDtoTest extends AbstractUnitTest {
    @Autowired
    private ChannelDto dto;

    @Autowired
    private ApplicationProperties properties;

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private ChannelService service;

    @Autowired
    private ChannelListingDao listingDao;

    private ChannelForm form;
    private MemberPojo member;
    private ProductPojo product;
    private List<ClientAndChannelSku> skuList;

    @Before
    public void init() {
        form = dataUtil.createChannelForm("channel", InvoiceType.SELF);
        skuList = new ArrayList<>();
        member = dataUtil.createMemberPojo("Member", MemberTypes.CLIENT);
        memberDao.insert(member);
        product=dataUtil.createProductPojo("Product","brand","clientSku","description",123.2,member.getId());
        productDao.insert(product);
        ClientAndChannelSku sku = dataUtil.createClientAndChannelSku("channelSku1", product.getClientSkuId());
        skuList.add(sku);
    }

    @Test
    public void testAddChannel() throws ApiException {
        dto.addChannel(form);
        assertEquals(1, service.getAllChannels().size());
        ChannelPojo returned = service.getAllChannels().get(0);
        assertEquals(form.getName(), returned.getName());
        assertEquals(form.getInvoiceType(), returned.getInvoiceType());
    }

    @Test(expected = ApiException.class)
    public void testAddChannelInvalid() throws ApiException {
        dto.addChannel(form);
        form.setInvoiceType(InvoiceType.CHANNEL);
        dto.addChannel(form);
    }

    @Test
    public void testAddChannelDefault1() throws ApiException {
        form.setInvoiceType(null);
        form.setName(null);
        dto.addChannel(form);
        assertEquals(1, service.getAllChannels().size());
        ChannelPojo returned = service.getAllChannels().get(0);
        assertEquals(properties.getDefaultName(), returned.getName());
        assertEquals(properties.getDefaultType(), returned.getInvoiceType());
    }

    @Test
    public void testAddChannelDefault2() throws ApiException {
        form.setInvoiceType(null);
        form.setName("  ");
        dto.addChannel(form);
        assertEquals(1, service.getAllChannels().size());
        ChannelPojo returned = service.getAllChannels().get(0);
        assertEquals(properties.getDefaultName(), returned.getName());
        assertEquals(properties.getDefaultType(), returned.getInvoiceType());
    }

    @Test
    public void testGetAllChannel() throws ApiException {
        service.addChannel(dataUtil.createChannelPojo("CHANNEL", InvoiceType.SELF));
        assertEquals(1, dto.getAllChannels().size());
    }

    @Test
    public void testAddChannelListing() throws ApiException {
        ChannelPojo channel = dataUtil.createChannelPojo("Channel", InvoiceType.SELF);
        service.addChannel(channel);
        ChannelListingForm form = dataUtil.createListingForm(member.getId(),channel.getId(),skuList);
        dto.addChannelListing(form);
        assertEquals(1,listingDao.selectAll().size());
        ChannelListingPojo returned=listingDao.selectAll().get(0);
        assertEquals(member.getId(),returned.getClientId());
        assertEquals(channel.getId(),returned.getChannelId());
        assertEquals(skuList.get(0).getChannelSku(),returned.getChannelSkuId());
        assertEquals(product.getGlobalSkuId(),returned.getGlobalSkuId());
    }

    @Test(expected = ApiException.class)
    public void testAddChannelListingInvalid1() throws ApiException {
        ChannelPojo channel = dataUtil.createChannelPojo("Channel", InvoiceType.SELF);
        service.addChannel(channel);
        List<ClientAndChannelSku> list=new ArrayList<>();
        list.add(dataUtil.createClientAndChannelSku(product.getClientSkuId(),"invalid"));
        ChannelListingForm form = dataUtil.createListingForm(member.getId(),channel.getId(),list);
        dto.addChannelListing(form);
    }

    @Test
    public void testAddChannelListingSameListing() throws ApiException {
        ChannelPojo channel = dataUtil.createChannelPojo("Channel", InvoiceType.SELF);
        service.addChannel(channel);
        ChannelListingForm form = dataUtil.createListingForm(member.getId(),channel.getId(),skuList);
        dto.addChannelListing(form);
        dto.addChannelListing(form);
        assertEquals(1,listingDao.selectAll().size());
    }

    @Test(expected = ApiException.class)
    public void testAddChannelListingInvalid() throws ApiException {
        ChannelPojo channel = dataUtil.createChannelPojo("Channel", InvoiceType.SELF);
        service.addChannel(channel);
        List<ClientAndChannelSku> list=new ArrayList<>();
        list.add(dataUtil.createClientAndChannelSku(product.getClientSkuId(),"invalid"));
        ChannelListingForm form = dataUtil.createListingForm(member.getId(),channel.getId(),list);
        dto.addChannelListing(form);
    }


}
