package com.increff.assure.dto;

import com.increff.assure.model.constants.MemberTypes;
import com.increff.assure.model.forms.MemberForm;
import com.increff.assure.pojo.MemberPojo;
import com.increff.assure.service.ApiException;
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

    @Before
    public void init() throws Exception {
        form1= dataUtil.createMemberForm("    Jain SaAB  ", MemberTypes.CLIENT);
        pojo1= dataUtil.createMemberPojo("    SaAB  ", MemberTypes.CLIENT);
        pojo2= dataUtil.createMemberPojo("    Maan  ", MemberTypes.CUSTOMER);
    }

    @Test
    public void testAdd() throws Exception {
        dto.add(form1);
        assertEquals(1,service.getAll().size());
        MemberPojo returned=service.getAll().get(0);
        assertEquals(form1.getType(),returned.getType());
        assertEquals(form1.getName().trim().toLowerCase(),returned.getName());
    }

    @Test(expected = ApiException.class)
    public void testAddInvalid() throws Exception {
        dto.add(form1);
        dto.add(form1);
    }

    @Test
    public void testGetClients() throws Exception {
        service.add(pojo1);
        service.add(pojo2);
        assertEquals(1,dto.getClients().size());
    }

    @Test
    public void testGetAll() throws Exception {
        service.add(pojo1);
        service.add(pojo2);
        assertEquals(2,dto.getAll().size());
    }




}
