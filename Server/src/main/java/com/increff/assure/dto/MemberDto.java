package com.increff.assure.dto;

import com.increff.assure.model.constants.MemberTypes;
import com.increff.assure.model.data.MemberData;
import com.increff.assure.model.forms.MemberForm;
import com.increff.assure.pojo.MemberPojo;
import com.increff.assure.service.MemberService;
import com.increff.assure.util.ConvertGeneric;
import com.increff.assure.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MemberDto {

    @Autowired
    MemberService service;

    public void add( MemberForm form) throws Exception {
        normalize(form);
        MemberPojo pojo = ConvertGeneric.convert(form, MemberPojo.class);
        service.add(pojo);
    }

    public List<MemberData> getAll() throws Exception{
        List<MemberPojo> pojoList = service.getAll();
        List<MemberData> dataList = new ArrayList<>();
        for(MemberPojo pojo: pojoList)
        {
            MemberData data = ConvertGeneric.convert(pojo, MemberData.class);
            dataList.add(data);
        }

        return dataList;
    }

    public List<MemberData> getClients() throws Exception{
        List<MemberPojo> pojoList = service.getByType(MemberTypes.CLIENT);
        List<MemberData> dataList = new ArrayList<>();
        for(MemberPojo pojo: pojoList)
        {
            MemberData data = ConvertGeneric.convert(pojo, MemberData.class);
            dataList.add(data);
        }
        return dataList;
    }

    public List<MemberData> getCustomers() throws Exception{
        List<MemberPojo> pojoList = service.getByType(MemberTypes.CUSTOMER);
        List<MemberData> dataList = new ArrayList<>();
        for(MemberPojo pojo: pojoList)
        {
            MemberData data = ConvertGeneric.convert(pojo, MemberData.class);
            dataList.add(data);
        }
        return dataList;
    }

    protected static void normalize(MemberForm form)
    {
        form.setName(StringUtil.toLowerCase(form.getName()));
    }


}
