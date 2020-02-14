package com.increff.assure.controller;

import com.increff.assure.dto.ChannelDto;
import com.increff.assure.model.data.ChannelData;
import com.increff.assure.model.forms.ChannelForm;
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
public class ChannelController {
    @Autowired
    private ChannelDto dto;

    @ApiOperation(value = "Add channel information")
    @RequestMapping(path = "/api/channel", method = RequestMethod.POST)
    public void addChannel(@Valid @RequestBody ChannelForm form) throws Exception {
        dto.addChannel(form);
    }

    @ApiOperation(value = "Add channel listing")
    @RequestMapping(path="/api/channel/listing",method = RequestMethod.POST)
    public void addChannelListing(@Valid @RequestBody ChannelListingForm form) throws Exception {
        dto.addChannelListing(form);
    }

    @ApiOperation(value = "Get All channel Information")
    @RequestMapping(path="/api/channel",method = RequestMethod.GET)
    public List<ChannelData> getAllChannels() throws Exception {
       return dto.getAllChannels();
    }


}
