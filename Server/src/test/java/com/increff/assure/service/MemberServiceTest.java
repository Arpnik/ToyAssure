package com.increff.assure.service;

import com.increff.assure.dao.MemberDao;
import com.increff.assure.model.constants.MemberTypes;
import com.increff.assure.pojo.MemberPojo;
import com.increff.assure.util.AbstractUnitTest;
import com.increff.assure.util.dataUtil;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class MemberServiceTest extends AbstractUnitTest {

    @Autowired
    private MemberService service;

    @Autowired
    private MemberDao dao;

    private MemberPojo pojo1;

    @Before
    public void init()
    {
        pojo1 = dataUtil.createMemberPojo("xyz", MemberTypes.CLIENT);
        dao.insert(pojo1);

    }

    @Test(expected= ApiException.class)
    public void testAddInvalid() throws ApiException {
        MemberPojo pojo2=dataUtil.createMemberPojo("xyz",MemberTypes.CLIENT);
        service.add(pojo2);
    }

    @Test
    public void testAddValid() throws ApiException {
        MemberPojo pojo2=dataUtil.createMemberPojo("abc",MemberTypes.CLIENT);
        service.add(pojo2);
        assertEquals( 2,dao.selectAll().size());
        List<MemberPojo> pojoList=dao.selectAll();
        MemberPojo returned=pojoList.get(0);
        assertEquals(pojo1.getName(),returned.getName());
        assertEquals(pojo1.getType(),returned.getType());
        returned=pojoList.get(1);
        assertEquals(pojo2.getName(),returned.getName());
        assertEquals(pojo2.getType(),returned.getType());
    }

    @Test
    public void testGet()
    {
        MemberPojo returned=service.get(pojo1.getId());
        assertEquals(pojo1.getName(),returned.getName());
        assertEquals(pojo1.getType(),returned.getType());
    }

//    @Test
//    public void testGetClients() throws ApiException {
//        MemberPojo pojo2=dataUtil.createMemberPojo("abc",MemberTypes.CUSTOMER);
//        dao.insert(pojo2);
//        assertEquals( 1,service.getClients().size());
//        MemberPojo returned=service.getClients().get(0);
//        assertEquals(pojo1.getName(),returned.getName());
//        assertEquals(pojo1.getType(),returned.getType());
//    }

    @Test
    public void testGetAll()
    {
        MemberPojo pojo2=dataUtil.createMemberPojo("abc",MemberTypes.CUSTOMER);
        dao.insert(pojo2);
        assertEquals(2,service.getAll().size());
    }

}
