package com.increff.assure.dto;

import com.increff.assure.model.data.ChannelItemCheckData;
import com.increff.assure.model.form.ChannelItemCheckForm;
import com.increff.assure.model.form.ChannelOrderForm;
import com.increff.assure.service.ApiException;
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

    public void placeOrder(ChannelOrderForm form)
    {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.postForObject(placeOrderUri,form,ChannelOrderForm.class);
    }

    public void checkOrderItem(ChannelItemCheckForm item) throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<ChannelItemCheckData> response=restTemplate.postForEntity(checkOrderUri,item, ChannelItemCheckData.class);
        ChannelItemCheckData data=response.getBody();
        if(response.getStatusCode().is4xxClientError()||response.getStatusCode().is5xxServerError())
        {
            System.out.println(response.getBody().toString());
            return;
        }
        if(data.getQuantity()<item.getQuantity())
        {
            throw new ApiException("Avaliable Quantity of product with Channel SKU:"+item.getChannelSkuId()+" is:"+data.getQuantity());
        }
    }
}
