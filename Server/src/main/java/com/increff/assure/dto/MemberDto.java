package com.increff.assure.dto;

import com.increff.assure.model.Exception.ApiException;
import com.increff.assure.model.constants.MemberTypes;
import com.increff.assure.model.data.MemberData;
import com.increff.assure.model.form.MemberForm;
import com.increff.assure.pojo.MemberPojo;
import com.increff.assure.service.MemberService;
import com.increff.assure.util.ConvertGeneric;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.increff.assure.util.Normalize.normalize;

@Service
public class MemberDto {

    @Autowired
    private MemberService service;


    @Transactional(rollbackFor = ApiException.class)
    public void add(MemberForm form) throws ApiException {
        normalize(form);
        MemberPojo pojo = ConvertGeneric.convert(form, MemberPojo.class);
        service.add(pojo);
    }

    @Transactional(readOnly = true)
    public List<MemberData> getAll() {
        List<MemberPojo> pojoList = service.getAll();
        return pojoList.stream().map(x -> ConvertGeneric.convert(x, MemberData.class)).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MemberData> getClients() {
        List<MemberPojo> pojoList = service.getByType(MemberTypes.CLIENT);
        return pojoList.stream().map(x -> ConvertGeneric.convert(x, MemberData.class)).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MemberData> getCustomers() {
        List<MemberPojo> pojoList = service.getByType(MemberTypes.CUSTOMER);
        return pojoList.stream().map(x -> ConvertGeneric.convert(x, MemberData.class)).collect(Collectors.toList());
    }


}
