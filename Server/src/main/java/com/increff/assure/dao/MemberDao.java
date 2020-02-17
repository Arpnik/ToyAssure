package com.increff.assure.dao;

import com.increff.assure.model.constants.MemberTypes;
import com.increff.assure.pojo.MemberPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class MemberDao  extends AbstractDao<MemberPojo>{

    private static String select_name_type="select m from MemberPojo m where m.name=:name and m.type=:type";
    private static String select_type ="select m from MemberPojo m where m.type=:type";

    public MemberPojo selectByParams( String name, MemberTypes type) {
        TypedQuery<MemberPojo> query = getQuery( select_name_type);
        query.setParameter("name", name);
        query.setParameter("type", type);
        return getSingle(query);
    }

    public List<MemberPojo> selectAllByType(MemberTypes type)
    {
        TypedQuery<MemberPojo> query = getQuery(select_type);
        query.setParameter("type",type);
        return query.getResultList();
    }


}
