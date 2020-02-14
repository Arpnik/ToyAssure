package com.increff.assure.spring;

public class WebInitializerExtended extends AbstractWebInitializer {

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[] { SpringConfig.class };
    }
}
