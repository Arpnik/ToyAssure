package com.increff.assure.spring;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class ApplicationProperties {


    @Value("${server.Uri}")
    private String serverUri;


    @Value("${channel.baseUrl}")
    private String baseUrl;

}
