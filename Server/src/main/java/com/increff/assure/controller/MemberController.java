package com.increff.assure.controller;

import com.increff.assure.dto.MemberDto;
import com.increff.assure.model.data.MemberData;
import com.increff.assure.model.forms.MemberForm;
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

    @ApiOperation(value = "Add Member information")
    @RequestMapping(path = "/api/member", method = RequestMethod.POST)
    public void add(@Valid @RequestBody MemberForm form) throws Exception {
        dto.add(form);
    }

    @ApiOperation(value = "Get information of all members")
    @RequestMapping(path = "/api/member", method = RequestMethod.GET)
    public List<MemberData> getAll() throws Exception {
        return dto.getAll();
    }

    @ApiOperation(value="Get all Client Information")
    @RequestMapping(path="/api/member/client",method = RequestMethod.GET)
    public List<MemberData> getClients() throws Exception {
        return dto.getClients();
    }

    @ApiOperation(value="Get all Customer Information")
    @RequestMapping(path="/api/member/customer",method = RequestMethod.GET)
    public List<MemberData> getCustomers() throws Exception {
        return dto.getCustomers();
    }
}
