package com.increff.assure.controller;

import com.increff.assure.dto.MemberDto;
import com.increff.assure.model.data.MemberData;
import com.increff.assure.model.form.MemberForm;
import com.increff.assure.model.Exception.ApiException;
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
public class MemberController {
    @Autowired
    private MemberDto dto;

    @ApiOperation( value = "Add member information")
    @RequestMapping( path = "/api/member", method = RequestMethod.POST)
    public void add( @Valid @RequestBody MemberForm form) throws ApiException {
        dto.add( form);
    }

    @ApiOperation( value = "Get information of all members")
    @RequestMapping( path = "/api/member", method = RequestMethod.GET)
    public List<MemberData> getAll(){
        return dto.getAll();
    }

    @ApiOperation(value="Get all client information")
    @RequestMapping(path="/api/member/client",method = RequestMethod.GET)
    public List<MemberData> getClients(){
        return dto.getClients();
    }

    @ApiOperation(value="Get all customer information")
    @RequestMapping(path="/api/member/customer",method = RequestMethod.GET)
    public List<MemberData> getCustomers(){
        return dto.getCustomers();
    }
}
