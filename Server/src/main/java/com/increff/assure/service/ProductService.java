package com.increff.assure.service;

import com.increff.assure.dao.ProductDao;
import com.increff.assure.pojo.ProductPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductService {
    
    @Autowired
    ProductDao dao;

    @Transactional
    public void add(List<ProductPojo> pojoList){
        for(ProductPojo pojo:pojoList){
             dao.insert(pojo);
        }
    }

    @Transactional(readOnly = true)
    public List<ProductPojo> getAllById(long clientId)
    {
        return dao.selectByClientId(clientId);
    }

    @Transactional(readOnly = true)
    public ProductPojo get(long id)
    {
        return dao.select(id);
    }

    @Transactional(rollbackFor = ApiException.class)
    public void update(long id,ProductPojo updated) throws ApiException {
        ProductPojo existing = getCheck(id);
        existing.setDescription(updated.getDescription());
        existing.setBrandId(updated.getBrandId());
        existing.setMrp(updated.getMrp());
        existing.setName(updated.getName());
    }

    @Transactional(readOnly = true)
    public ProductPojo getCheck(long id) throws ApiException {
        ProductPojo pojo=get(id);
        if(pojo==null)
        {
            throw new ApiException("Product doesn't exists");
        }
        return pojo;
    }

    @Transactional(readOnly = true)
    public ProductPojo getCheckByParams(long clientId, String clientSkuId) throws ApiException {
        ProductPojo existing = get(clientId,clientSkuId);
        if(existing == null)
        {
            throw new ApiException("ClientSKu:"+clientSkuId+" is not valid");
        }
        return existing;
    }

    @Transactional(readOnly = true)
    public ProductPojo get(long clientId, String ClientSkuId)
    {
        return dao.selectByClientIdAndClientSku(clientId,ClientSkuId);
    }


}
