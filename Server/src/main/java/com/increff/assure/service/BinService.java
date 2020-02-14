package com.increff.assure.service;

import com.increff.assure.dao.BinDao;
import com.increff.assure.dao.BinSkuDao;
import com.increff.assure.pojo.BinPojo;
import com.increff.assure.pojo.BinSkuPojo;
import com.increff.assure.pojo.BinFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BinService {
    @Autowired
    private BinDao binDao;

    @Autowired
    private BinSkuDao binSkuDao;

    @Transactional
    public void createBins(long numberOfBins)
    {
        long i;
        for(i=0;i<numberOfBins;i++)
        {
            BinPojo pojo=new BinPojo();
            binDao.insert(pojo);
        }
    }


    @Transactional
    public void AddOrUpdateInventory(List<BinSkuPojo> pojoList)
    {
        for(BinSkuPojo pojo:pojoList)
        {
            BinSkuPojo existing= binSkuDao.checkPresenceByBinIdAndGlobalSku(pojo.getBinId(),pojo.getGlobalSkuId());
            if(existing!=null)
            {
                existing.setQuantity(pojo.getQuantity()+existing.getQuantity());
                binSkuDao.update(existing);
            }
            else
            {
                binSkuDao.insert(pojo);
            }
        }
    }

    @Transactional(readOnly = true)
    public List<BinFilter> getProductInfo(long binId,long globalSkuId) throws ApiException {
        if(globalSkuId<=0)
        {
            checkBinId(binId);
            return binSkuDao.selectProductInfoByBinId(binId);
        }
        if(binId==0)
        {
            return binSkuDao.selectProductInfoByGlobalSku(globalSkuId);
        }
        checkBinId(binId);
        List<BinFilter> result= binSkuDao.selectProductInfoByBinAndGlobalSku(binId,globalSkuId);
        return result;
    }

    @Transactional(rollbackFor = ApiException.class)
    public void update(long binSkuId,long quantity) throws ApiException {
        BinSkuPojo existing=binSkuDao.select(binSkuId);
        if(existing==null)
        {
            throw new ApiException("The BinSkuId doesn't exist");
        }
        existing.setQuantity(quantity);
        binSkuDao.update(existing);
    }

    @Transactional(readOnly = true,rollbackFor = ApiException.class)
    public void checkBinId(long binId) throws ApiException {
        BinPojo existing=binDao.select(binId);
        if(existing==null)
        {
            throw new ApiException("Bin ID:"+binId+" doesn't exists.");
        }
    }

    @Transactional(readOnly = true)
    public BinSkuPojo get(long binSkuId)
    {
       return binSkuDao.select(binSkuId);
    }

    public BinSkuPojo getCheck(long binSkuId) throws ApiException {
        BinSkuPojo pojo=get(binSkuId);
        if(pojo==null)
            throw new ApiException("BinSKuId is not valid");
        return pojo;
    }
}
