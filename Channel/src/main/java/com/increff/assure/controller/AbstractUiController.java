package com.increff.assure.controller;

import com.increff.assure.spring.ApplicationProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;


public abstract class AbstractUiController {

    @Autowired
    private ApplicationProperties properties;

    protected ModelAndView mav(String page) {

        // Set info
        ModelAndView mav = new ModelAndView(page);
        mav.addObject("baseUrl", properties.getBaseUrl());
        return mav;
    }

}

