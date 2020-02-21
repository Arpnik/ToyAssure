package com.increff.assure.spring;

import com.increff.assure.model.constants.InvoiceType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class ApplicationProperties {

    @Value("${app.baseUrl}")
    private String baseUrl;

    @Value("${defaultMember.name}")
    private String defaultName;

    @Value("${defaultMember.invoiceType}")
    private InvoiceType defaultType;

    @Value("${channel.uri}")
    private String channelUri;

    @Value("${defaultChannel.name}")
    private String defaultChannelName;
}
