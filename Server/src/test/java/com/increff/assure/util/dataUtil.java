package com.increff.assure.util;

import com.increff.assure.model.constants.MemberTypes;
import com.increff.assure.model.forms.BinFilterForm;
import com.increff.assure.model.forms.CreateBinForm;
import com.increff.assure.model.forms.MemberForm;
import com.increff.assure.model.forms.ProductForm;
import com.increff.assure.pojo.BinSkuPojo;
import com.increff.assure.pojo.MemberPojo;
import com.increff.assure.pojo.ProductPojo;

public class dataUtil {
    public static MemberPojo createMemberPojo(String name, MemberTypes type) {
        MemberPojo member=new MemberPojo();
        member.setName(name);
        member.setType(type);
        return member;
    }

    public static BinSkuPojo createBinSkuPojo(long qty,long globalSku,long binId)
    {
        BinSkuPojo pojo=new BinSkuPojo();
        pojo.setQuantity(qty);
        pojo.setGlobalSkuId(globalSku);
        pojo.setBinId(binId);
        return pojo;
    }

    public static CreateBinForm createNumberBinForm(long numberOfBins)
    {
        CreateBinForm form=new CreateBinForm();
        form.setNumberOfBins(numberOfBins);
        return form;
    }

    public static MemberForm createMemberForm(String name,MemberTypes type)
    {
        MemberForm form=new MemberForm();
        form.setName(name);
        form.setType(type);
        return form;
    }

    public static ProductForm createProductForm(String name,String brandId,String clientSkuId,String desc,double mrp)
    {
        ProductForm form=new ProductForm();
        form.setBrandId(brandId);
        form.setDescription(desc);
        form.setClientSkuId(clientSkuId);
        form.setMrp(mrp);
        form.setName(name);
        return form;
    }

    public static ProductPojo createProductPojo(String name,String brandId,String clientSkuId,String desc,double mrp,long clientId)
    {
        ProductPojo pojo=new ProductPojo();
        pojo.setName(name);
        pojo.setMrp(mrp);
        pojo.setBrandId(brandId);
        pojo.setDescription(desc);
        pojo.setClientId(clientId);
        pojo.setClientSkuId(clientSkuId);
        return pojo;
    }

    public static BinFilterForm createBinFilterForm(long binId,long clientId,String clientSkuId)
    {
        BinFilterForm form=new BinFilterForm();
        form.setBinId(binId);
        form.setClientId(clientId);
        form.setClientSkuId(clientSkuId);
        return form;

    }
}
