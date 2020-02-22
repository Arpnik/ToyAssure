package com.increff.assure.dto;

import com.increff.assure.model.Exception.ApiException;
import com.increff.assure.model.data.ChannelData;
import com.increff.assure.model.data.ChannelItemCheckData;
import com.increff.assure.model.data.InvoiceMetaData;
import com.increff.assure.model.data.MemberData;
import com.increff.assure.model.form.ChannelItemCheckForm;
import com.increff.assure.model.form.ChannelOrderForm;
import com.increff.assure.spring.ApplicationProperties;
import com.increff.assure.util.PDFUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;


@Service
public class ChannelOrderDto {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ApplicationProperties properties;

    public void placeOrder(ChannelOrderForm form) {
        restTemplate.postForObject(properties.getServerUri() + "order/channel", form, ChannelOrderForm.class);
    }


    //TODO autowire and bean learn
    public ChannelItemCheckData checkOrderItem(ChannelItemCheckForm item) throws ApiException {
        ResponseEntity<ChannelItemCheckData> response = restTemplate.postForEntity(properties.getServerUri() + "order/product", item, ChannelItemCheckData.class);
        ChannelItemCheckData data = response.getBody();
        if (data.getOrderedQuantity() < item.getQuantity()) {
            throw new ApiException("Available quantity of product with channel SKU:" + item.getChannelSkuId() + " is:" + data.getOrderedQuantity());
        }
        return data;
    }

    public List<MemberData> getClients() {
        return Arrays.asList(restTemplate.getForObject(properties.getServerUri() + "member/client", MemberData[].class));
    }

    public List<MemberData> getCustomers() {
        return Arrays.asList(restTemplate.getForObject(properties.getServerUri() + "member/customer", MemberData[].class));
    }

    public List<ChannelData> getChannels() {
        return Arrays.asList(restTemplate.getForObject(properties.getServerUri() + "channel", ChannelData[].class));
    }

    public byte[] getInvoice(InvoiceMetaData data) throws ApiException {
        return PDFUtility.createPdfForInvoice(data);
    }
}
