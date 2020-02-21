package com.increff.assure.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/ui")
public class AppUiController extends AbstractUiController {
	// Request mapping get
	@RequestMapping(value = "/home")
	public ModelAndView home() {
		return modelAndView("home.html");
	}

	@RequestMapping(value = "/member")
	public ModelAndView member() {
		return modelAndView("member.html");
	}

	@RequestMapping(value = "/product")
	public ModelAndView product() {
		return modelAndView("product.html");
	}

	@RequestMapping(value = "/bin")
	public ModelAndView updateBinInventory() {
		return modelAndView("bin.html");
	}

	@RequestMapping(value = "/channel")
	public ModelAndView channel() {
		return modelAndView("channel.html");
	}

	@RequestMapping(value = "/order")
	public ModelAndView order() {
		return modelAndView("order.html");
	}


}
