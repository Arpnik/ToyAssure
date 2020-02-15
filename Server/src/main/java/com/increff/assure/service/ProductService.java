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
    public List<ProductPojo> getAllByClientId(long clientId)
    {
        return dao.selectByClientId(clientId);
    }

    @Transactional(readOnly = true)
    public ProductPojo get(long id)
    {
        return dao.select(id);
    }

    @Transactional(rollbackFor = ApiException.class)
    public void update(long id,ProductPojo updatedPojo) throws ApiException {
        ProductPojo existing=getCheck(id);
        existing.setDescription(updatedPojo.getDescription());
        existing.setBrandId(updatedPojo.getBrandId());
        existing.setMrp(updatedPojo.getMrp());
        existing.setName(updatedPojo.getName());
        dao.update(existing);
    }

    public ProductPojo getCheck(long id) throws ApiException {
        ProductPojo pojo=get(id);
        if(pojo==null)
        {
            throw new ApiException("Product doesn't exists");
        }
        return pojo;
    }

    @Transactional(readOnly = true)
    public ProductPojo getByClientIdAndClientSku(long clientId,String ClientSkuId){
        ProductPojo existing = dao.selectByClientIdAndClientSku(clientId,ClientSkuId);
        return existing;
    }


}