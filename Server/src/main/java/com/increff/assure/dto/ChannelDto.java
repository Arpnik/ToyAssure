package com.increff.assure.dto;

import com.increff.assure.model.forms.ClientAndChannelSku;
import com.increff.assure.model.constants.InvoiceType;
import com.increff.assure.model.data.ChannelData;
import com.increff.assure.model.forms.ChannelForm;
import com.increff.assure.model.forms.ChannelListingForm;
import com.increff.assure.pojo.ChannelListingPojo;
import com.increff.assure.pojo.ChannelPojo;
import com.increff.assure.pojo.ProductPojo;
import com.increff.assure.service.ApiException;
import com.increff.assure.service.ChannelService;
import com.increff.assure.service.MemberService;
import com.increff.assure.service.ProductService;
import com.increff.assure.util.ConvertGeneric;
import com.increff.assure.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ChannelDto {

    @Value("${defaultMember.name}")
    private String defaultName;

    @Value("${defaultMember.invoiceType}")
    private InvoiceType defaultType;


    @Autowired
    private ChannelService channelService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private ProductService productService;


    public void addChannel(ChannelForm form) throws Exception {
        normalizeAndSetDefaults(form);
        ChannelPojo pojo= ConvertGeneric.convert(form, ChannelPojo.class);
        channelService.addChannel(pojo);
    }

    @Transactional(rollbackFor = ApiException.class)
    public void addChannelListing(ChannelListingForm form) throws Exception {
        memberService.checkPresenceOfClientById(form.getClientId());
        channelService.checkPresenceOfChannel(form.getChannelId());
        Boolean callDbPersist=true;
        String errors="";
        long index=0;
        for(ClientAndChannelSku sku:form.getClientAndChannelSkuList())
        {
            try
            {
                ChannelListingPojo pojo=ConvertGeneric.convert(form,ChannelListingPojo.class);
                normalizeSku(sku);
                pojo.setChannelSkuId(sku.getChannelSku());
                ProductPojo product=getProduct(form.getClientId(),sku.getClientSku());
                pojo.setGlobalSkuId(product.getGlobalSkuId());
                channelService.addChannelListing(pojo,callDbPersist);
            }
            catch(Exception e)
            {
                callDbPersist=false;
                errors=errors+"\n"+index+":"+e.getMessage();
            }
            index+=1;
        }
        if(!callDbPersist)
        {
            throw new ApiException(errors);
        }
    }

    public List<ChannelData> getAllChannels() throws Exception
    {
        List<ChannelPojo> pojoList=channelService.getAllChannels();
        List<ChannelData> dataList=new ArrayList<>();
        for(ChannelPojo pojo:pojoList)
        {
            ChannelData data=ConvertGeneric.convert(pojo,ChannelData.class);
            dataList.add(data);
        }
        return dataList;
    }


    protected void normalizeAndSetDefaults(ChannelForm form)
    {
        form.setName(Optional.ofNullable(StringUtil.toUpperCase(form.getName())).orElse(defaultName));

        if(form.getName().isEmpty())
        {
            form.setName(defaultName);
        }
        form.setInvoiceType(Optional.ofNullable(form.getInvoiceType()).orElse(defaultType));
    }

    protected void normalizeSku(ClientAndChannelSku sku)
    {
        sku.setChannelSku(sku.getChannelSku().trim());
        sku.setClientSku(sku.getClientSku().trim());
    }

    protected ProductPojo getProduct(long clientId,String clientSkuId) throws ApiException {
        ProductPojo product=productService.getByClientIdAndClientSku(clientId,clientSkuId);
        if(product==null)
        {
            throw new ApiException("Client SKu:"+clientSkuId+" doesn't belong to specified client");
        }
        return product;
    }

}
