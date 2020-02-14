package com.increff.assure.spring;

public class WebInitializerChannel extends AbstractWebInitializer {
    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[] { SpringConfig.class };
    }
}
