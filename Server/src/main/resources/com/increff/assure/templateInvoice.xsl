<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:fo="http://www.w3.org/1999/XSL/Format">
    <!-- Attribute used for table border -->
    <xsl:attribute-set name="tableBorder">
        <xsl:attribute name="border">solid 0.3mm white</xsl:attribute>
    </xsl:attribute-set>
    <xsl:template match="InvoiceMetaData">
        <fo:root>
            <fo:layout-master-set>
                <fo:simple-page-master master-name="simpleA4"
                                       page-height="29.7cm" page-width="25.0cm" margin="1cm">
                    <fo:region-body/>
                </fo:simple-page-master>
            </fo:layout-master-set>
            <fo:page-sequence master-reference="simpleA4">
                <fo:flow flow-name="xsl-region-body">
                    <fo:block space-after="5mm"> 
                          <fo:external-graphic src="https://cdn.skillenza.com/files/60f1ed0d-2b2f-4514-9e89-fa84bb23cb80/increff_community_card_banner.png" content-height="scale-to-fit" height="3cm"  content-width="6cm" scaling="non-uniform" />
                          <fo:inline font-size="32pt" font-family="Helvetica" font-weight="bold" text-align="center">&#160; &#160; &#160; Order Invoice-<xsl:value-of select="Id"/></fo:inline>
                    </fo:block>


                    <fo:block space-after="3mm">
                        <fo:table table-layout="fixed" width="50%" border-collapse="separate">
                            <fo:table-column column-width="5cm"/>
                            <fo:table-column column-width="12cm"/>
                            <fo:table-body>

                                <fo:table-row>
                                    <fo:table-cell margin-left="1mm" background-color="#000080" font-weight="bold" color="white" xsl:use-attribute-sets="tableBorder">
                                        <fo:block font-size="15pt" text-align="left">Channel OrderID</fo:block>
                                    </fo:table-cell>
                                    <fo:table-cell margin-left="1mm">
                                        <fo:block font-size="15pt" text-align="left">  <xsl:value-of select="channelOrderId"/></fo:block>
                                    </fo:table-cell>
                                </fo:table-row>
                                
                                <fo:table-row>
                                    <fo:table-cell margin-left="1mm" background-color="#000080" font-weight="bold" color="white" xsl:use-attribute-sets="tableBorder">
                                        <fo:block font-size="15pt" text-align="left">Client Name</fo:block>
                                    </fo:table-cell>
                                    <fo:table-cell margin-left="1mm">
                                        <fo:block font-size="15pt" text-align="left">  <xsl:value-of select="clientName"/></fo:block>
                                    </fo:table-cell>
                                </fo:table-row>

                                <fo:table-row>
                                    <fo:table-cell margin-left="1mm" background-color="#000080" font-weight="bold" color="white" xsl:use-attribute-sets="tableBorder">
                                        <fo:block font-size="15pt" text-align="left">Customer Name</fo:block>
                                    </fo:table-cell>
                                    <fo:table-cell margin-left="1mm">
                                        <fo:block font-size="15pt" text-align="left">  <xsl:value-of select="customerName"/></fo:block>
                                    </fo:table-cell>
                                </fo:table-row>

                                <fo:table-row>
                                    <fo:table-cell margin-left="1mm" background-color="#000080" font-weight="bold" color="white" xsl:use-attribute-sets="tableBorder">
                                        <fo:block font-size="15pt" text-align="left">Order Date</fo:block>
                                    </fo:table-cell>
                                    <fo:table-cell margin-left="1mm">
                                        <fo:block font-size="15pt" text-align="left">  <xsl:value-of select="createDate"/></fo:block>
                                    </fo:table-cell>
                                </fo:table-row>
                                    
                            </fo:table-body>
                        </fo:table>

                    </fo:block>

                    <fo:block font-size="10pt">
                        <fo:table table-layout="fixed" width="100%" border-collapse="separate">
                            <fo:table-column column-width="2cm"/>
                            <fo:table-column column-width="4cm"/>
                            <fo:table-column column-width="3cm"/>
                            <fo:table-column column-width="3cm"/>
                            <fo:table-column column-width="3cm"/>
                            <fo:table-column column-width="6cm"/>
                            <fo:table-header font-weight="bold">
                                <fo:table-cell  background-color="#000080" color="white" xsl:use-attribute-sets="tableBorder">
                                    <fo:block font-size="15pt" text-align="center" font-weight="bold">S.NO.</fo:block>
                                </fo:table-cell>
                                <fo:table-cell  background-color="#000080" color="white" xsl:use-attribute-sets="tableBorder">
                                    <fo:block font-size="15pt" text-align="center" font-weight="bold">ProductName</fo:block>
                                </fo:table-cell>
                                <fo:table-cell  background-color="#000080" color="white" xsl:use-attribute-sets="tableBorder">
                                    <fo:block font-size="15pt" text-align="center" font-weight="bold">BrandId</fo:block>
                                </fo:table-cell>
                                <fo:table-cell  background-color="#000080" color="white" xsl:use-attribute-sets="tableBorder">
                                    <fo:block font-size="15pt"  text-align="center" font-weight="bold">Unit Price</fo:block>
                                </fo:table-cell>
                                <fo:table-cell  background-color="#000080" color="white" xsl:use-attribute-sets="tableBorder">
                                    <fo:block font-size="15pt" text-align="center" font-weight="bold">Quantity</fo:block>
                                </fo:table-cell>
                                <fo:table-cell  background-color="#000080" color="white" xsl:use-attribute-sets="tableBorder">
                                    <fo:block font-size="15pt" text-align="center" font-weight="bold">Amount</fo:block>
                                </fo:table-cell>
                            </fo:table-header>
                            <fo:table-footer font-weight="bold">
                                <fo:table-row>
                                    <fo:table-cell  background-color="#000080"  color="white" number-columns-spanned="5" >
                                        <fo:block font-size="15pt" text-align="right">Total Amount:</fo:block>
                                    </fo:table-cell>
                                    <fo:table-cell  background-color="#000080" color="white"  number-columns-spanned="1" >
                                        <fo:block font-size="15pt" text-align="center">Rs. <xsl:value-of select="totalBill"/> /-</fo:block>
                                    </fo:table-cell>
                                </fo:table-row>
                            </fo:table-footer>
                            <fo:table-body>
                                <xsl:apply-templates select="invoice"/> <!-- branch tag is taken and pasted in below used template 46 line-->
                            </fo:table-body>
                        </fo:table>
                    </fo:block>
                </fo:flow>
            </fo:page-sequence>
        </fo:root>
    </xsl:template>
    <xsl:template match="invoice">
        <fo:table-row>
            <fo:table-cell  xsl:use-attribute-sets="tableBorder">
                <fo:block text-align="center" font-size="15pt">
                    <xsl:value-of select="sno"/>
                </fo:block>
            </fo:table-cell>
            <fo:table-cell  xsl:use-attribute-sets="tableBorder">
                <fo:block text-align="center" font-size="15pt">
                    <xsl:value-of select="name"/>
                </fo:block>
            </fo:table-cell>

            <fo:table-cell  xsl:use-attribute-sets="tableBorder">
                <fo:block text-align="center" font-size="15pt">
                    <xsl:value-of select="brandId"/>
                </fo:block>
            </fo:table-cell>
            <fo:table-cell  xsl:use-attribute-sets="tableBorder">
                <fo:block text-align="center" font-size="15pt">
                    <xsl:value-of select="mrp"/>
                </fo:block>
            </fo:table-cell>
            <fo:table-cell  xsl:use-attribute-sets="tableBorder">
                <fo:block text-align="center" font-size="15pt">
                    <xsl:value-of select="qty"/>
                </fo:block>
            </fo:table-cell>
            <fo:table-cell  xsl:use-attribute-sets="tableBorder">
                <fo:block text-align="center" font-size="15pt">
                    <xsl:value-of select="amount"/>
                </fo:block>
            </fo:table-cell>
        </fo:table-row>
    </xsl:template>
</xsl:stylesheet>