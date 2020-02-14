package com.increff.assure.controller;

import com.increff.assure.model.data.AboutAppData;
import com.increff.assure.service.AboutAppService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Api
@RestController
public class AboutAppController {
    @Api
    @RestController
    public class AboutApiController {

        @Autowired
        private AboutAppService service;

        @ApiOperation(value = "Gives application name and version")
        @RequestMapping(path = "/api/about", method = RequestMethod.GET)
        public AboutAppData getDetails() {
            AboutAppData d = new AboutAppData();
            d.setName(service.getName());
            d.setVersion(service.getVersion());
            return d;
        }

    }
}
