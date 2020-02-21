package com.increff.assure.dto;

import com.increff.assure.dao.BinDao;
import com.increff.assure.dao.InventoryDao;
import com.increff.assure.dao.OrderDao;
import com.increff.assure.dao.OrderItemDao;
import com.increff.assure.model.Exception.ApiException;
import com.increff.assure.model.constants.InvoiceType;
import com.increff.assure.model.constants.MemberTypes;
import com.increff.assure.model.constants.OrderStatusType;
import com.increff.assure.model.data.ChannelItemCheckData;
import com.increff.assure.model.data.OrderDetailsData;
import com.increff.assure.model.data.OrderDisplayData;
import com.increff.assure.model.form.ChannelItemCheckForm;
import com.increff.assure.model.form.ChannelOrderForm;
import com.increff.assure.model.form.ChannelOrderLineItem;
import com.increff.assure.model.forms.OrderForm;
import com.increff.assure.model.forms.OrderLineItemForm;
import com.increff.assure.pojo.*;
import com.increff.assure.service.*;
import com.increff.assure.util.AbstractUnitTest;
import com.increff.assure.util.dataUtil;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class OrderDtoTest extends AbstractUnitTest {
    @Autowired
    private OrderDto dto;

    @Autowired
    private OrderService service;

    @Autowired
    private MemberService memberService;

    @Autowired
    private ProductService productService;

    @Autowired
    private InventoryDao inventoryDao;

    @Autowired
    private BinDao binDao;

    @Autowired
    private OrderItemDao itemDao;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private BinService binService;

    @Autowired
    private ChannelService channelService;

    private List<OrderLineItemForm> items;
    private OrderForm form;
    private ChannelPojo channel;
    private MemberPojo client;
    private MemberPojo customer;
    private ProductPojo product;
    private BinPojo bin;
    private BinSkuPojo binSku;
    private OrderForm order;
    private ChannelOrderForm channelOrder;
    private List<ChannelOrderLineItem> channelItems;
    private ChannelListingPojo listing;

    @Before
    public void init() throws ApiException {
        client = dataUtil.createMemberPojo("client", MemberTypes.CLIENT);
        customer = dataUtil.createMemberPojo("customer", MemberTypes.CUSTOMER);
        memberService.add(client);
        memberService.add(customer);
        channel = dataUtil.createChannelPojo("INTERNAL", InvoiceType.SELF);
        channelService.addChannel(channel);
        channel = dataUtil.createChannelPojo("FLIPKART", InvoiceType.CHANNEL);
        channelService.addChannel(channel);
        product = dataUtil.createProductPojo("product1", "brand", "clientSku", "desc", 34.21, client.getId());
        List<ProductPojo> products = new ArrayList<>();
        products.add(product);
        productService.add(products);
        bin = new BinPojo();
        binDao.insert(bin);
        binSku = dataUtil.createBinSkuPojo(new Long(100), product.getGlobalSkuId(), bin.getBinId());
        List<BinSkuPojo> binList = new ArrayList<>();
        binList.add(binSku);
        binService.AddOrUpdateInventory(binList);
        inventoryDao.insert(dataUtil.createInventoryPojo(product.getGlobalSkuId(),binSku.getQuantity()));
        items = new ArrayList<>();
        OrderLineItemForm item = dataUtil.createOrderLineItem(product.getClientSkuId(), new Long(5), 2347.1);
        items.add(item);
        listing = dataUtil.createListing(products.get(0).getGlobalSkuId(), "channel SKU ID", channel.getId(), client.getId());
        channelService.addChannelListing(listing);
        order = dataUtil.createOrderForm("channelOrderId", client.getId(), customer.getId(), items);
        channelItems = new ArrayList<>();
        ChannelOrderLineItem lineItem = dataUtil.createChannelItem(listing.getChannelSkuId(), new Long(6), 563.1);
        channelItems.add(lineItem);
        channelOrder = dataUtil.createChannelOrderForm("channelOrderID", client.getId(), customer.getId(), channel.getId(), channelItems);
    }

    @Test
    public void testPlaceOrder() throws ApiException {
        dto.placeOrder(order);
        assertEquals(1, service.getAll().size());
        OrderPojo returned = service.getAll().get(0);
        assertEquals(OrderStatusType.CREATED, returned.getStatus());
        assertEquals(customer.getId(), returned.getCustomerId());
        assertEquals(client.getId(), returned.getClientId());
        assertEquals(order.getChannelOrderId(), returned.getChannelOrderId());
        assertEquals(1, itemDao.selectAll().size());
        OrderItemPojo returnedItem = itemDao.selectAll().get(0);
        assertEquals(returned.getId(), returnedItem.getOrderId());
        OrderLineItemForm item = items.get(0);
        assertEquals(item.getSellingPricePerUnit(), returnedItem.getSellingPricePerUnit());
        assertEquals(item.getOrderedQuantity(), returnedItem.getOrderedQuantity());
        assertEquals(new Long(0), returnedItem.getAllocatedQuantity());
        assertEquals(new Long(0), returnedItem.getFulfilledQuantity());
        assertEquals(product.getGlobalSkuId(), returnedItem.getGlobalSkuId());
    }

    @Test(expected = ApiException.class)
    public void testPlaceOrderCustomerInvalid() throws ApiException {
        order.setCustomerId(customer.getId() + 1);
        dto.placeOrder(order);
    }

    @Test(expected = ApiException.class)
    public void testPlaceOrderClientInvalid() throws ApiException {
        order.setClientId(client.getId() + 1);
        dto.placeOrder(order);
    }

    @Test(expected = ApiException.class)
    public void testPlaceOrderItemDuplicate() throws ApiException {
        items.add(items.get(0));
        order.setItems(items);
        dto.placeOrder(order);
    }

    @Test(expected = ApiException.class)
    public void testPlaceOrderInvalidProduct() throws ApiException {
        OrderLineItemForm item = items.get(0);
        item.setClientSkuId(item.getClientSkuId() + "invalid");
        items.add(item);
        order.setItems(items);
        dto.placeOrder(order);
    }

    @Test
    public void testPlaceOrderChannel() throws ApiException {
        dto.placeOrderByChannel(channelOrder);
        assertEquals(1, service.getAll().size());
        OrderPojo returned = service.getAll().get(0);
        assertEquals(OrderStatusType.CREATED, returned.getStatus());
        assertEquals(customer.getId(), returned.getCustomerId());
        assertEquals(client.getId(), returned.getClientId());
        assertEquals(channelOrder.getChannelOrderId(), returned.getChannelOrderId());
        assertEquals(1, itemDao.selectAll().size());
        OrderItemPojo returnedItem = itemDao.selectAll().get(0);
        assertEquals(returned.getId(), returnedItem.getOrderId());
        ChannelOrderLineItem item = channelItems.get(0);
        assertEquals(item.getSellingPricePerUnit(), returnedItem.getSellingPricePerUnit());
        assertEquals(item.getOrderedQuantity(), returnedItem.getOrderedQuantity());
        assertEquals(new Long(0), returnedItem.getAllocatedQuantity());
        assertEquals(new Long(0), returnedItem.getFulfilledQuantity());
        assertEquals(product.getGlobalSkuId(), returnedItem.getGlobalSkuId());
    }

    @Test(expected = ApiException.class)
    public void testPlaceOrderChannelInvalidProduct() throws ApiException {
        ChannelOrderLineItem item = channelItems.get(0);
        item.setChannelSkuId(item.getChannelSkuId() + "Invalid");
        channelItems.add(item);
        dto.placeOrderByChannel(channelOrder);
    }

    @Test
    public void testGetAll() throws ApiException {
        dto.placeOrder(order);
        assertEquals(1, dto.getAll().size());
        OrderDisplayData data = dto.getAll().get(0);
        assertEquals(OrderStatusType.CREATED, data.getStatus());
        assertEquals(orderDao.selectAll().get(0).getId(), data.getOrderId());
        assertEquals(order.getChannelOrderId(), data.getChannelOrderId());
    }

    @Test(expected = ApiException.class)
    public void testCheckOrderedItemInvalid() throws ApiException {
        ChannelItemCheckForm form = dataUtil.createChannelItemCheckForm(new Long(2), client.getId(), channel.getId(), "Invalid");
        ChannelItemCheckData returned = dto.checkOrderedItem(form);
    }

    @Test
    public void testCheckOrderedItem() throws ApiException {
        ChannelItemCheckForm form = dataUtil.createChannelItemCheckForm(new Long(2), client.getId(), channel.getId(), listing.getChannelSkuId());
        ChannelItemCheckData returned = dto.checkOrderedItem(form);
        assertEquals(product.getBrandId(),returned.getBrandId());
        assertEquals(product.getName(),returned.getProductName());
        assertEquals(binSku.getQuantity(),returned.getOrderedQuantity());
    }

    @Test(expected = ApiException.class)
    public void testValidateChannelIdAndChannelOrderIdInvalid() throws ApiException {
        dto.placeOrderByChannel(channelOrder);
        dto.validateChannelIdAndChannelOrderId(channel.getId(),channelOrder.getChannelOrderId());
    }


    @Test
    public void testAllocate() throws ApiException {
        dto.placeOrder(order);
        Long orderId=orderDao.selectAll().get(0).getId();
        dto.allocateOrder(orderId);
        assertEquals(OrderStatusType.ALLOCATED,orderDao.selectAll().get(0).getStatus());
    }

    @Test
    public void testGetOrderDetails() throws ApiException {
        OrderPojo order=dataUtil.createOrderPojo(OrderStatusType.CREATED,channel.getId(),"channelOrderId",customer.getId(),client.getId());
        orderDao.insert(order);
        OrderItemPojo item=dataUtil.createOrderItem(product.getGlobalSkuId(),new Long(1),order.getId(),34.21,new Long(0),new Long(0));
        itemDao.insert(item);
        OrderDetailsData data=dto.getOrderDetails(orderDao.selectAll().get(0).getId()).get(0);
        assertEquals(product.getBrandId(),data.getBrandId());
        assertEquals(product.getName(),data.getProductName());
        assertEquals(item.getSellingPricePerUnit(),data.getSellingPricePerUnit());
        assertEquals(itemDao.selectAll().get(0).getAllocatedQuantity(),data.getAllocatedQuantity());
    }

    @Test
    public void testCreateInvoice() throws Exception {
        dto.placeOrder(order);
        Long orderId=orderDao.selectAll().get(0).getId();
        dto.allocateOrder(orderId);
        assertEquals(OrderStatusType.ALLOCATED,orderDao.selectAll().get(0).getStatus());
        OrderPojo orderPojo=orderDao.selectAll().get(0);
        OrderItemPojo itemPojo=itemDao.selectAll().get(0);
        dto.createInvoice(orderPojo.getId());
    }


}
