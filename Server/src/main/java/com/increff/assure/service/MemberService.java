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

    @Transactional( rollbackFor = ApiException.class)
    public void add( MemberPojo pojo) throws ApiException {
        MemberPojo existing = dao.selectByParams( pojo.getName(), pojo.getType());
        if( existing != null){
            throw new ApiException( "Member with same name and Type already exists");
        }
        dao.insert( pojo);
    }

    @Transactional(readOnly = true)
    public void checkMemberAndType(long id, MemberTypes type) throws ApiException {
        MemberPojo pojo=get(id);
        if(pojo == null || pojo.getType() != type)
        {
            throw new ApiException("Member with ID: "+id+" and Type: "+type+" doesn't exist");
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
    public List<MemberPojo> getByType(MemberTypes type){
        return dao.selectAllByType(type);
    }



}
