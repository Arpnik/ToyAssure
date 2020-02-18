package com.increff.assure.dto;

import com.increff.assure.model.constants.MemberTypes;
import com.increff.assure.model.data.MemberData;
import com.increff.assure.model.form.MemberForm;
import com.increff.assure.pojo.MemberPojo;
import com.increff.assure.model.Exception.ApiException;
import com.increff.assure.service.MemberService;
import com.increff.assure.util.ConvertGeneric;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.increff.assure.util.Normalize.normalize;

@Service
public class MemberDto {

    @Autowired
    MemberService service;


    @Transactional(rollbackFor = ApiException.class)
    public void add( MemberForm form) throws ApiException {
        normalize( form);
        MemberPojo pojo = ConvertGeneric.convert( form, MemberPojo.class);
        service.add( pojo);
    }

    @Transactional(readOnly = true)
    public List<MemberData> getAll(){
        List<MemberPojo> pojoList = service.getAll();
        List<MemberData> dataList = new ArrayList<>();
        for(MemberPojo pojo: pojoList)
        {
            MemberData data = ConvertGeneric.convert( pojo, MemberData.class);
            dataList.add( data);
        }

        return dataList;
    }

    @Transactional(readOnly = true)
    public List<MemberData> getClients(){
        List<MemberPojo> pojoList = service.getByType( MemberTypes.CLIENT);
        List<MemberData> dataList = new ArrayList<>();
        for(MemberPojo pojo: pojoList)
        {
            MemberData data = ConvertGeneric.convert( pojo, MemberData.class);
            dataList.add( data);
        }
        return dataList;
    }

    @Transactional(readOnly = true)
    public List<MemberData> getCustomers(){
        List<MemberPojo> pojoList = service.getByType( MemberTypes.CUSTOMER);
        List<MemberData> dataList = new ArrayList<>();
        for(MemberPojo pojo: pojoList)
        {
            MemberData data = ConvertGeneric.convert( pojo, MemberData.class);
            dataList.add( data);
        }
        return dataList;
    }


}
