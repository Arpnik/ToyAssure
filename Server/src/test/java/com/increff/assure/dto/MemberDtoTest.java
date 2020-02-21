package com.increff.assure.dto;

import com.increff.assure.model.Exception.ApiException;
import com.increff.assure.model.constants.MemberTypes;
import com.increff.assure.model.form.MemberForm;
import com.increff.assure.pojo.MemberPojo;
import com.increff.assure.service.MemberService;
import com.increff.assure.util.AbstractUnitTest;
import com.increff.assure.util.dataUtil;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;

public class MemberDtoTest extends AbstractUnitTest {

    @Autowired
    private  MemberDto dto;

    @Autowired
    private MemberService service;

    private MemberForm form1;
    private MemberPojo pojo1;
    private MemberPojo pojo2;
    private MemberPojo pojo3;

    @Before
    public void init() throws Exception {
        form1= dataUtil.createMemberForm("    jkl  ", MemberTypes.CLIENT);
        pojo1= dataUtil.createMemberPojo("    abc  ", MemberTypes.CLIENT);
        pojo2= dataUtil.createMemberPojo("    xyz  ", MemberTypes.CUSTOMER);
        pojo3= dataUtil.createMemberPojo("    tuv  ", MemberTypes.CUSTOMER);
    }

    @Test
    public void testAdd() throws ApiException {
        dto.add(form1);
        assertEquals(1,service.getAll().size());
        MemberPojo returned=service.getAll().get(0);
        assertEquals(form1.getType(),returned.getType());
        assertEquals(form1.getName().trim().toLowerCase(),returned.getName());
    }

    @Test(expected = ApiException.class)
    public void testAddInvalid() throws ApiException {
        dto.add(form1);
        dto.add(form1);
    }

    @Test
    public void testGetClients() throws ApiException {
        service.add(pojo1);
        service.add(pojo2);
        service.add(pojo3);
        assertEquals(1,dto.getClients().size());
    }

    @Test
    public void testGetAll() throws ApiException {
        service.add(pojo1);
        service.add(pojo2);
        service.add(pojo3);
        assertEquals(3,dto.getAll().size());
    }

    @Test
    public void testGetCustomers() throws ApiException {
        service.add(pojo1);
        service.add(pojo2);
        service.add(pojo3);
        assertEquals(2,dto.getCustomers().size());
    }




}
