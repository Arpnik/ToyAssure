package com.increff.assure.dto;

import com.increff.assure.model.Exception.ApiException;
import com.increff.assure.model.constants.InvoiceType;
import com.increff.assure.model.constants.MemberTypes;
import com.increff.assure.model.data.ChannelData;
import com.increff.assure.model.data.ChannelItemCheckData;
import com.increff.assure.model.data.MemberData;
import com.increff.assure.model.form.ChannelItemCheckForm;
import com.increff.assure.model.form.ChannelOrderForm;
import com.increff.assure.spring.ApplicationProperties;
import com.increff.assure.util.AbstractUnitTest;
import com.increff.assure.util.DataUtil;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class ChannelDtoTest extends AbstractUnitTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ApplicationProperties properties;

    @InjectMocks
    private ChannelOrderDto dto;

    private MemberData[] memberList = new MemberData[2];
    private ChannelData[] channelList = new ChannelData[1];
    private ChannelItemCheckData itemData;
    private ChannelItemCheckForm itemForm;
    private ChannelOrderForm form;


    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        form = DataUtil.createOrder(1, "abc", 2, 3);
        itemForm = DataUtil.createItemForm("sku1", 1L, 2, 3);
        itemData = DataUtil.createChannelItemData("item1", "brand1", 1L, 12.45);
        memberList[0] = DataUtil.createMemberData(new Long(101), "name1", MemberTypes.CLIENT);
        memberList[1] = DataUtil.createMemberData(new Long(102), "name2", MemberTypes.CUSTOMER);
        channelList[0] = DataUtil.createChannelData("name", InvoiceType.SELF);
    }

    @Test
    public void testGetClients() {
        when(restTemplate.getForObject(properties.getServerUri() + "member/client", MemberData[].class)).thenReturn(memberList);
        List<MemberData> dataList = dto.getClients();
        assertEquals(2, dataList.size());
    }

    @Test
    public void testGetCustomers() {
        when(restTemplate.getForObject(properties.getServerUri() + "member/customer", MemberData[].class)).thenReturn(memberList);
        List<MemberData> dataList = dto.getCustomers();
        assertEquals(2, dataList.size());
    }

    @Test
    public void testGetChannel() {
        when(restTemplate.getForObject(properties.getServerUri() + "channel", ChannelData[].class)).thenReturn(channelList);
        List<ChannelData> dataList = dto.getChannels();
        assertEquals(1, dataList.size());
    }

    @Test(expected = ApiException.class)
    public void testCheckOrderItemInvalid() throws ApiException {
        when(restTemplate.postForEntity(properties.getServerUri() + "order/product", itemForm, ChannelItemCheckData.class)).thenReturn(new ResponseEntity(itemData, HttpStatus.OK));
        ChannelItemCheckData data = dto.checkOrderItem(itemForm);
        assertEquals(itemData, data);
    }


    @Test
    public void testCheckOrderItemValid() throws ApiException {
        itemData.setOrderedQuantity(new Long(5));
        when(restTemplate.postForEntity(properties.getServerUri() + "order/product", itemForm, ChannelItemCheckData.class)).thenReturn(new ResponseEntity(itemData, HttpStatus.OK));
        ChannelItemCheckData data = dto.checkOrderItem(itemForm);
        assertEquals(itemData, data);
    }

//    @Test
//    public void testPlaceOrder()
//    {
//        Mockito.when(restTemplate.postForObject("http://localhost:9000/assure/api/order/channel",null, ChannelOrderForm.class));
//        dto.placeOrder(form);
//    }

}
