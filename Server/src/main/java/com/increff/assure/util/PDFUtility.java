package com.increff.assure.util;

import com.increff.assure.model.data.InvoiceMetaData;
import com.increff.assure.model.data.OrderDetailsData;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;

public class PDFUtility {

    private static final String PATH_TO_Order_XSL = "src/main/resources/com/increff/assure/templateInvoice.xsl" ;


    public static byte[] createPdfForInvoice(InvoiceMetaData data, List<OrderDetailsData> itemData) throws Exception {

        DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();

        DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();

        Document document = documentBuilder.newDocument();
        Element root = document.createElement("InvoiceMetaData");
        document.appendChild(root);
        double sum=0.0;
        int sno=1;
        for(OrderDetailsData item:itemData)
        {
            Element product = document.createElement("invoice");
            root.appendChild(product);
            Element count = document.createElement("sno");
            count.appendChild(document.createTextNode(Integer.toString(sno)));
            product.appendChild(count);
            Element product_name = document.createElement("name");
            product_name.appendChild(document.createTextNode(item.getProductName()));
            product.appendChild(product_name);
            Element barcode = document.createElement("brandId");
            barcode.appendChild(document.createTextNode(item.getBrandId()));
            product.appendChild(barcode);
            Element mrp = document.createElement("mrp");
            mrp.appendChild(document.createTextNode(Double.toString( item.getSellingPricePerUnit())));
            product.appendChild(mrp);
            Element qty = document.createElement("qty");
            qty.appendChild(document.createTextNode(Long.toString(item.getOrderedQuantity())));
            product.appendChild(qty);
            Element totalPrice = document.createElement("amount");
            totalPrice.appendChild(document.createTextNode( Double.toString(item.getOrderedQuantity() * item.getSellingPricePerUnit())));
            product.appendChild(totalPrice);
            sum+=( item.getOrderedQuantity() * item.getSellingPricePerUnit());
            sno+=1;
        }
        Element totalPrice=document.createElement("totalBill");
        totalPrice.appendChild(document.createTextNode(Double.toString(sum)));
        root.appendChild(totalPrice);

        Element orderId=document.createElement("Id");
        orderId.appendChild(document.createTextNode(Long.toString( data.getId())));
        root.appendChild(orderId);

        Element client=document.createElement("clientName");
        client.appendChild(document.createTextNode(data.getClientName()));
        root.appendChild(client);

        Element customer=document.createElement("customerName");
        customer.appendChild(document.createTextNode(data.getCustomerName()));
        root.appendChild(customer);

        Element date=document.createElement("createDate");
        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.FULL);
        date.appendChild(document.createTextNode(data.getCreatedDate().format(formatter)));
        root.appendChild(date);


        Element channelOrderId=document.createElement("channelOrderId");
        channelOrderId.appendChild(document.createTextNode(data.getChannelOrderId()));
        root.appendChild(channelOrderId);


        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource domSource = new DOMSource(document);

        ByteArrayOutputStream xmlBaos = new ByteArrayOutputStream();
        StreamResult streamResult = new StreamResult(xmlBaos);
        transformer.transform(domSource, streamResult);

        FopFactory fopFactory = FopFactory.newInstance(new File(".").toURI());
        ByteArrayOutputStream pdfBaos = new ByteArrayOutputStream();
        Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, pdfBaos);
        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer2 = factory.newTransformer(new StreamSource(PATH_TO_Order_XSL));
        Result res = new SAXResult(fop.getDefaultHandler());
        Source src = new StreamSource(new ByteArrayInputStream(xmlBaos.toByteArray()));
        transformer2.transform(src, res);
        return pdfBaos.toByteArray();
    }

}
