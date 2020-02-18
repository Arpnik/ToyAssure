package com.increff.assure.dto;

import com.increff.assure.model.Pojo.OrderDetailsResult;
import com.increff.assure.model.constants.InvoiceType;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.increff.assure.util.Normalize.normalize;

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

    @Value("${channel.uri}")
    private String channelUri;

    public void placeOrder(OrderForm form) throws ApiException {
        normalize(form);
        memberService.checkMemberAndType(form.getClientId(), MemberTypes.CLIENT);
        memberService.checkMemberAndType(form.getCustomerId(),MemberTypes.CUSTOMER);
        OrderPojo orderPojo= ConvertGeneric.convert(form,OrderPojo.class);
        ChannelPojo channelPojo=channelService.getDefaultChannel();
        validateChannelIdAndChannelOrderId(channelPojo.getId(),form.getChannelOrderId());
        orderPojo.setChannelId(channelPojo.getId());
        long sno = 0;
        Set<String> hash_Set = new HashSet<>();
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
                ProductPojo productPojo= productService.getCheckByParams(orderPojo.getClientId(),item.getClientSkuId());
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


    public void placeOrderByChannel(ChannelOrderForm form) throws ApiException {
        normalize(form);
        memberService.checkMemberAndType(form.getCustomerId(),MemberTypes.CUSTOMER);
        memberService.checkMemberAndType(form.getClientId(),MemberTypes.CLIENT);
        validateChannelIdAndChannelOrderId(form.getChannelId(),form.getChannelOrderId());
        OrderPojo orderPojo= ConvertGeneric.convert(form, OrderPojo.class);
        orderPojo.setChannelId(form.getChannelId());
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
                pojo.setOrderedQuantity(item.getOrderedQuantity());
                ChannelListingPojo listingPojo=channelService.getByParams(form.getChannelId(),item.getChannelSkuId(),form.getClientId());
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
        InvoiceMetaData invoice=Convert(order);
        invoice.setItems(getOrderDetails(orderId));

        if(channelService.getCheck(order.getChannelId()).getInvoiceType() == InvoiceType.CHANNEL)
        {
            return getInvoice(invoice);
        }

        return PDFUtility.createPdfForInvoice(invoice);
    }

    public byte[] getInvoice(InvoiceMetaData invoice)
    {
        RestTemplate restTemplate=new RestTemplate();
        return restTemplate.postForObject(channelUri,invoice,byte[].class);
    }

    public List<OrderDisplayData> getAll() {
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

    protected InvoiceMetaData Convert(OrderPojo order) throws ApiException {
        InvoiceMetaData invoice=new InvoiceMetaData();
        invoice.setClientName( memberService.get( order.getClientId()).getName());
        invoice.setCustomerName( memberService.get( order.getCustomerId()).getName());
        invoice.setChannelName( channelService.getCheck( order.getChannelId()).getName());
        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.FULL);
        invoice.setOrderedDate(order.getCreatedDate().format(formatter));
        invoice.setId(order.getId());
        invoice.setChannelOrderId(order.getChannelOrderId());
        return invoice;
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
        ChannelListingPojo pojo = channelService.getByParams(form.getChannelId(),form.getChannelSkuId(),form.getClientId());
        InventoryPojo inventoryPojo = inventoryService.getByGlobalSkuId(pojo.getGlobalSkuId());

        if(inventoryPojo.getAvailableQuantity() <= 0)
        {
            ChannelItemCheckData data = new ChannelItemCheckData();
            data.setOrderedQuantity(0);
            return data;
        }

        ProductPojo product=productService.getCheck(pojo.getGlobalSkuId());
        return makeChannelData(product,inventoryPojo);
    }

    public static ChannelItemCheckData makeChannelData(ProductPojo product,InventoryPojo inventoryPojo)
    {
        ChannelItemCheckData data = ConvertGeneric.convert(product,ChannelItemCheckData.class);
        data.setProductName(product.getName());
        data.setOrderedQuantity(inventoryPojo.getAvailableQuantity());
        return data;
    }

    public void allocateOrder(long orderId) throws ApiException {
        orderService.allocateOrder(orderId);
    }


    protected void validateChannelIdAndChannelOrderId(long channelId, String channelOrderId) throws ApiException {
        OrderPojo pojo = orderService.getByParams(channelId, channelOrderId);
        if(pojo != null)
        {
            throw new ApiException("ChannelOrderId:"+channelOrderId+" for this channel is not unique");
        }
    }
}
