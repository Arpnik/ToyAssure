package com.increff.assure.service;

import com.increff.assure.dao.MemberDao;
import com.increff.assure.model.constants.MemberTypes;
import com.increff.assure.pojo.MemberPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class MemberService {
    @Autowired
    MemberDao dao;

    @Transactional(rollbackFor = ApiException.class)
    public void add(MemberPojo pojo) throws ApiException {
        MemberPojo existing = dao.selectByNameType(pojo.getName(), pojo.getType());
        if(existing!=null){
            throw new ApiException("Member with same name and Type already exists");
        }
        dao.insert(pojo);
    }

    @Transactional(rollbackFor = ApiException.class,readOnly = true)
    public void checkPresenceOfClientById(long id) throws ApiException {
        MemberPojo pojo=get(id);
        if(pojo==null || pojo.getType()!= MemberTypes.CLIENT)
        {
            throw new ApiException("Client ID doesn't exist");
        }
    }

    @Transactional(rollbackFor = ApiException.class,readOnly = true)
    public void checkPresenceOfCustomerById(long id) throws ApiException {
        MemberPojo pojo=get(id);
        if(pojo==null || pojo.getType()!= MemberTypes.CUSTOMER)
        {
            throw new ApiException("Customer ID doesn't exist");
        }
    }

    @Transactional(readOnly = true)
    public MemberPojo get(long id)
    {
        return dao.select(id);
    }

    @Transactional(readOnly = true)
    public List<MemberPojo> getAll(){
        return dao.selectAll();
    }

    @Transactional(readOnly = true)
    public List<MemberPojo> getClients(){
        return dao.selectAllClients();
    }


}
