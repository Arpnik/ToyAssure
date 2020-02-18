package com.increff.assure.dto;

import com.increff.assure.model.constants.InvoiceType;
import com.increff.assure.model.constants.MemberTypes;
import com.increff.assure.model.data.ChannelData;
import com.increff.assure.model.data.ErrorData;
import com.increff.assure.model.form.ChannelForm;
import com.increff.assure.model.forms.ChannelListingForm;
import com.increff.assure.model.forms.ClientAndChannelSku;
import com.increff.assure.pojo.ChannelListingPojo;
import com.increff.assure.pojo.ChannelPojo;
import com.increff.assure.pojo.ProductPojo;
import com.increff.assure.model.Exception.ApiException;
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

import static com.increff.assure.util.Normalize.normalizeSku;

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


    @Transactional(rollbackFor = ApiException.class)
    public void addChannel(ChannelForm form) throws ApiException {
        normalizeAndSetDefaults(form);
        ChannelPojo pojo= ConvertGeneric.convert(form, ChannelPojo.class);
        channelService.addChannel(pojo);
    }

    @Transactional(rollbackFor = ApiException.class)
    public void addChannelListing(ChannelListingForm form) throws ApiException {
        memberService.checkMemberType(form.getClientId(), MemberTypes.CLIENT);
        channelService.checkPresenceOfChannel(form.getChannelId());
        Boolean callDbPersist=true;
        List<ErrorData> errorList=new ArrayList<>();
        long index=0;
        for(ClientAndChannelSku sku:form.getClientAndChannelSkuList())
        {
            ChannelListingPojo pojo=ConvertGeneric.convert(form,ChannelListingPojo.class);
            normalizeSku(sku);
            pojo.setChannelSkuId(sku.getChannelSku());
            try
            {
                ProductPojo product=productService.getCheckByParams(form.getClientId(),sku.getClientSku());
                pojo.setGlobalSkuId(product.getGlobalSkuId());
                channelService.addChannelListing(pojo,callDbPersist);
            }
            catch(ApiException e)
            {
                callDbPersist=false;
                errorList.add(new ErrorData(index,e.getMessage()));
            }
            index+=1;
        }

        if(!callDbPersist)
        {
            throw new ApiException(ErrorData.convert(errorList));
        }
    }


    @Transactional(readOnly = true)
    public List<ChannelData> getAllChannels()
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

}
