package com.increff.assure.controller;

import com.increff.assure.spring.ApplicationProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;

@Controller
public abstract class AbstractUiController {

	@Autowired
	private ApplicationProperties properties;

	protected ModelAndView modelAndView(String page) {

		// Set info
		ModelAndView mav = new ModelAndView(page);
		mav.addObject("baseUrl", properties.getBaseUrl());
		return mav;
	}

}
