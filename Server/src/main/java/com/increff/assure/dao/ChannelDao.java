package com.increff.assure.dao;

import com.increff.assure.pojo.ChannelPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;

@Repository
public class ChannelDao extends AbstractDao<ChannelPojo> {

    private static String select_name="select p from ChannelPojo p where p.name=:name";

    public ChannelPojo selectByName(String name) {
        TypedQuery<ChannelPojo> query=getQuery(select_name);
        query.setParameter("name",name);
        return getSingle(query);
    }
}
