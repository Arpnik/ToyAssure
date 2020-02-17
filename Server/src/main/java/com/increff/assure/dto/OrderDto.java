package com.increff.assure.dto;

import com.increff.assure.model.constants.MemberTypes;
import com.increff.assure.model.data.ChannelItemCheckData;
import com.increff.assure.model.data.InvoiceMetaData;
import com.increff.assure.model.data.OrderDetailsData;
import com.increff.assure.model.data.OrderDisplayData;
import com.increff.assure.model.form.ChannelItemCheckForm;
import com.increff.assure.model.form.ChannelOrderForm;
import com.increff.assure.model.form.ChannelOrderLineItem;
import com.increff.assure.model.forms.OrderForm;
import com.increff.assure.model.forms.OrderLineItemForm;
import com.increff.assure.pojo.*;
import com.increff.assure.service.*;
import com.increff.assure.util.ConvertGeneric;
import com.increff.assure.util.PDFUtility;
import com.increff.assure.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class OrderDto {
    @Autowired
    private OrderService orderService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private ProductService productService;

    @Autowired
    private ChannelService channelService;

    @Autowired
    private InventoryService inventoryService;


    public void placeOrder(OrderForm form) throws Exception {
        normalize(form);
        memberService.checkMemberAndType(form.getClientId(), MemberTypes.CLIENT);
        memberService.checkMemberAndType(form.getCustomerId(),MemberTypes.CUSTOMER);
        OrderPojo orderPojo= ConvertGeneric.convert(form,OrderPojo.class);
        ChannelPojo channelPojo=channelService.getDefaultChannel();
        validateChannelIdAndChannelOrderId(channelPojo.getId(),form.getChannelOrderId());
        orderPojo.setChannelId(channelPojo.getId());
        long sno=0;
        Set<String> hash_Set = new HashSet<String>();
        List<OrderItemPojo> itemPojoList=new ArrayList<>();
        String errors="";
        for(OrderLineItemForm item:form.getItems())
        {
            try{
                if(hash_Set.contains(item.getClientSkuId()))
                {
                    throw new ApiException("Duplicate ClientSkuID:"+item.getClientSkuId());
                }
                hash_Set.add(item.getClientSkuId());
                OrderItemPojo itemPojo=ConvertGeneric.convert(item,OrderItemPojo.class);
                ProductPojo productPojo= productService.getByClientIdAndClientSku(orderPojo.getClientId(),item.getClientSkuId());
                if(productPojo==null)
                {
                    throw new ApiException("ClientSKu:"+item.getClientSkuId()+" is not valid");
                }
                itemPojo.setGlobalSkuId(productPojo.getGlobalSkuId());
                itemPojoList.add(itemPojo);
             }
            catch(Exception e)
            {
                errors=errors+"\n"+sno+":"+e.getMessage();
            }
            sno+=1;
        }
        if(!errors.isEmpty())
        {
            throw new ApiException(errors);
        }
        orderService.placeOrder(orderPojo,itemPojoList);
    }


    public void placeOrderByChannel(ChannelOrderForm form) throws Exception {
        normalize(form);
        memberService.checkMemberAndType(form.getCustomerId(),MemberTypes.CUSTOMER);
        memberService.checkMemberAndType(form.getClientId(),MemberTypes.CLIENT);
        ChannelPojo channelPojo=channelService.getDefaultChannel();
        validateChannelIdAndChannelOrderId(channelPojo.getId(),form.getChannelOrderId());
        OrderPojo orderPojo= ConvertGeneric.convert(form, OrderPojo.class);
        orderPojo.setChannelId(channelPojo.getId());
        List<OrderItemPojo> itemPojoList=new ArrayList<>();
        long sno=0;
        String errors="";
        Set<String> hash_Set = new HashSet<String>();
        for(ChannelOrderLineItem item: form.getItems())
        {
            try{
                if(hash_Set.contains(item.getChannelSkuId()))
                {
                    throw new ApiException("Duplicate Channel SKU ID:"+item.getChannelSkuId());
                }
                hash_Set.add(item.getChannelSkuId());
                OrderItemPojo pojo=ConvertGeneric.convert(item,OrderItemPojo.class);
                pojo.setOrderedQuantity(item.getQuantity());
                ChannelListingPojo listingPojo=channelService.getByChannelIdAndChannelSkuAndClientId(channelPojo.getId(),item.getChannelSkuId(),form.getClientId());
                pojo.setGlobalSkuId(listingPojo.getGlobalSkuId());
                itemPojoList.add(pojo);
            }
            catch (Exception e)
            {
                errors=errors+"\n"+sno+":"+e.getMessage();
            }
            sno+=1;
        }

        if(!errors.isEmpty())
        {
            throw new ApiException(errors);
        }

        orderService.placeOrder(orderPojo, itemPojoList);
    }

    @Transactional(rollbackFor = ApiException.class)
    public byte[] createInvoice(long orderId) throws Exception {
        OrderPojo order=orderService.fullFillOrder(orderId);

        InvoiceMetaData invoice=ConvertGeneric.convert(order,InvoiceMetaData.class);
        invoice.setClientName( memberService.get( order.getClientId()).getName());
        invoice.setCustomerName( memberService.get( order.getCustomerId()).getName());
        invoice.setChannelName( channelService.getCheck( order.getChannelId()).getName());

        List<OrderDetailsData> items= getOrderDetails(orderId);

        return PDFUtility.createPdfForInvoice(invoice,items);
    }

    public List<OrderDisplayData> getAll() throws Exception {
        List<OrderDisplayData> dataList = new ArrayList<>();
        List<OrderPojo> pojoList = orderService.getAll();
        for(OrderPojo pojo: pojoList)
        {
            OrderDisplayData data = ConvertGeneric.convert(pojo, OrderDisplayData.class);
            data.setOrderId(pojo.getId());
            dataList.add(data);
        }
        return dataList;
    }

    public List<OrderDetailsData> getOrderDetails(long orderId){
        List<OrderDetailsData> dataList = new ArrayList<>();
        List<OrderDetailsResult> resultList = orderService.getOrderDetails(orderId);
        for(OrderDetailsResult res : resultList)
        {
            OrderDetailsData data = ConvertGeneric.convert(res, OrderDetailsData.class);
            dataList.add(data);
        }
        return dataList;
    }

    public ChannelItemCheckData checkOrderedItem(ChannelItemCheckForm form) throws ApiException {
        normalize(form);
        ChannelListingPojo pojo = channelService.getByChannelIdAndChannelSkuAndClientId(channelService.getDefaultChannel().getId(),form.getChannelSkuId(),form.getClientId());
        ChannelItemCheckData data = new ChannelItemCheckData();
        InventoryPojo inventoryPojo = inventoryService.getByGlobalSkuId(pojo.getGlobalSkuId());
        if(inventoryPojo.getAvailableQuantity() <= 0)
        {
            data.setQuantity(0);
            return data;
        }
        data.setQuantity(inventoryPojo.getAvailableQuantity());
        return data;
    }

    public void allocateOrder(long orderId) throws ApiException {
        orderService.allocateOrder(orderId);
    }


    protected static void normalize(OrderForm form) {
       form.setChannelOrderId(form.getChannelOrderId().trim());
       for(OrderLineItemForm item:form.getItems())
       {
           item.setClientSkuId(item.getClientSkuId().trim());
       }
    }


    protected static void normalize(ChannelOrderForm form) {
        form.setChannelName(StringUtil.toUpperCase(form.getChannelName()));
        form.setChannelOrderId(form.getChannelOrderId().trim());
        for(ChannelOrderLineItem item: form.getItems())
        {
            item.setChannelSkuId(item.getChannelSkuId().trim());
        }
    }
    protected static void normalize(ChannelItemCheckForm form)
    {
        form.setChannelSkuId(form.getChannelSkuId().trim());
    }

    protected void validateChannelIdAndChannelOrderId(long channelId, String channelOrderId) throws ApiException {
        OrderPojo pojo = orderService.getByParams(channelId, channelOrderId);
        if(pojo != null)
        {
            throw new ApiException("ChannelOrderId:"+channelOrderId+" for this channel is not unique");
        }
    }
}
