package com.increff.assure.dto;

import com.increff.assure.model.Exception.ApiException;
import com.increff.assure.model.Pojo.OrderDetailsResult;
import com.increff.assure.model.constants.InvoiceType;
import com.increff.assure.model.constants.MemberTypes;
import com.increff.assure.model.data.*;
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
        long channelId=channelService.getDefaultChannel().getId();
        //clientId,customerId,channelOrderID for channel unique
        validate(form.getCustomerId(),form.getClientId(), channelId, form.getChannelOrderId());
        OrderPojo orderPojo= ConvertGeneric.convert(form,OrderPojo.class);
        orderPojo.setChannelId(channelId);

        long sno = -1;
        Set<String> hash_Set = new HashSet<>();
        List<OrderItemPojo> pojoList = new ArrayList<>();
        List<ErrorData> errors = new ArrayList<>();//make error list

        for(OrderLineItemForm item:form.getItems())
        {
            sno+=1;
            //check duplicate
            if(hash_Set.contains(item.getClientSkuId())){
                errors.add(new ErrorData(sno,"Duplicate Client Sku ID:"+item.getClientSkuId()));
                continue;
            }
            hash_Set.add(item.getClientSkuId());

            OrderItemPojo itemPojo = ConvertGeneric.convert(item,OrderItemPojo.class);
            try{
                ProductPojo productPojo= productService.getCheckByParams(orderPojo.getClientId(), item.getClientSkuId());
                itemPojo.setGlobalSkuId(productPojo.getGlobalSkuId());
             }
            catch(ApiException e)
            {
                errors.add(new ErrorData(sno, e.getMessage()));
            }
            pojoList.add(itemPojo);
        }

        if(!errors.isEmpty())
        {
            throw new ApiException(ErrorData.convert(errors));
        }

        orderService.placeOrder(orderPojo, pojoList);
    }


    public void placeOrderByChannel(ChannelOrderForm form) throws ApiException {
        normalize(form);
        validate(form.getCustomerId(), form.getClientId(), form.getChannelId(), form.getChannelOrderId());
        OrderPojo orderPojo= ConvertGeneric.convert(form, OrderPojo.class);
        orderPojo.setChannelId(form.getChannelId());

        long sno = -1;
        Set<String> hash_Set = new HashSet<>();
        List<OrderItemPojo> pojoList = new ArrayList<>();
        List<ErrorData> errors = new ArrayList<>();

        for(ChannelOrderLineItem item: form.getItems())
        {
            sno+=1;

            if(hash_Set.contains(item.getChannelSkuId())){
                errors.add(new ErrorData(sno,"Duplicate Channel Sku ID:"+item.getChannelSkuId()));
                continue;
            }
            hash_Set.add(item.getChannelSkuId());

            OrderItemPojo pojo=ConvertGeneric.convert(item,OrderItemPojo.class);
            pojo.setOrderedQuantity(item.getOrderedQuantity());
            try{
                ChannelListingPojo listingPojo=channelService.getCheckByParams(form.getChannelId(),item.getChannelSkuId(),form.getClientId());
                pojo.setGlobalSkuId(listingPojo.getGlobalSkuId());
            }
            catch (Exception e)
            {
                errors.add(new ErrorData(sno, e.getMessage()));
            }
            pojoList.add(pojo);

        }

        if(!errors.isEmpty())
        {
            throw new ApiException(ErrorData.convert(errors));
        }

        orderService.placeOrder(orderPojo, pojoList);
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
        ChannelListingPojo pojo = channelService.getCheckByParams(form.getChannelId(),form.getChannelSkuId(),form.getClientId());
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

    protected void validate(long customerId, long clientId, long channelId, String channelOrderId) throws ApiException {
        memberService.checkMemberType(customerId,MemberTypes.CUSTOMER);
        memberService.checkMemberType(clientId,MemberTypes.CLIENT);
        channelService.getCheck(channelId);
        validateChannelIdAndChannelOrderId(channelId, channelOrderId);

    }

    protected void validateChannelIdAndChannelOrderId(long channelId, String channelOrderId) throws ApiException {
        OrderPojo pojo = orderService.getByParams(channelId, channelOrderId);
        if(pojo != null)
        {
            throw new ApiException("ChannelOrderId:"+channelOrderId+" for this channel is not unique");
        }
    }

}
