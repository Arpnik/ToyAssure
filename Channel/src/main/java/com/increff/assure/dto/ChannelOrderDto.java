package com.increff.assure.dto;

import com.increff.assure.model.Exception.ApiException;
import com.increff.assure.model.data.ChannelData;
import com.increff.assure.model.data.ChannelItemCheckData;
import com.increff.assure.model.data.InvoiceMetaData;
import com.increff.assure.model.data.MemberData;
import com.increff.assure.model.form.ChannelItemCheckForm;
import com.increff.assure.model.form.ChannelOrderForm;
import com.increff.assure.util.PDFUtility;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
public class ChannelOrderDto  {

    @Value("${channel.placeOrderUri}")
    private String placeOrderUri;

    @Value("${channel.checkOrderUri}")
    private String checkOrderUri;

    @Value("${channel.customerUri}")
    private String customerUri;

    @Value("${channel.clientUri}")
    private String clientUri;

    @Value("${channel.allChannelUri}")
    private String channelUri;

    public void placeOrder(ChannelOrderForm form)
    {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.postForObject(placeOrderUri,form,ChannelOrderForm.class);
    }

    public ChannelItemCheckData checkOrderItem(ChannelItemCheckForm item) throws ApiException {
        RestTemplate restTemplate =new RestTemplate();
        ResponseEntity<ChannelItemCheckData> response=restTemplate.postForEntity(checkOrderUri,item, ChannelItemCheckData.class);
        ChannelItemCheckData data=response.getBody();
        if(data.getOrderedQuantity()<item.getQuantity())
        {
            throw new ApiException("Available Quantity of product with Channel SKU:"+item.getChannelSkuId()+" is:"+data.getOrderedQuantity());
        }
        return data;
    }

    public MemberData[] getClients()
    {
        RestTemplate restTemplate=new RestTemplate();
        return restTemplate.getForObject(clientUri, MemberData[].class);
    }

    public MemberData[] getCustomers()
    {
        RestTemplate restTemplate=new RestTemplate();
        return restTemplate.getForObject(customerUri, MemberData[].class);
    }

    public ChannelData[] getChannels()
    {
        RestTemplate restTemplate=new RestTemplate();
        return restTemplate.getForObject(channelUri, ChannelData[].class);
    }

    public byte[] getInvoice(InvoiceMetaData data) throws Exception {
        return PDFUtility.createPdfForInvoice(data);
    }
}
