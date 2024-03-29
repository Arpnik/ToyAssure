package com.increff.assure.controller;

import com.increff.assure.dto.ChannelDto;
import com.increff.assure.model.Exception.ApiException;
import com.increff.assure.model.data.ChannelData;
import com.increff.assure.model.form.ChannelForm;
import com.increff.assure.model.forms.ChannelListingForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@Api
@RestController
@RequestMapping("/api/channel")
public class ChannelController {
    @Autowired
    private ChannelDto dto;

    @ApiOperation(value = "Add channel information")
    @RequestMapping(path = "", method = RequestMethod.POST)
    public void addChannel(@Valid @RequestBody ChannelForm form) throws ApiException {
        dto.addChannel(form);
    }

    @ApiOperation(value = "Add channel listing")
    @RequestMapping(path="/listing",method = RequestMethod.POST)
    public void addChannelListing(@Valid @RequestBody ChannelListingForm form) throws ApiException {
        dto.addChannelListing(form);
    }

    @ApiOperation(value = "Get All channel Information")
    @RequestMapping(path="",method = RequestMethod.GET)
    public List<ChannelData> getAllChannels() {
       return dto.getAllChannels();
    }


}
