package com.increff.assure.service;

import com.increff.assure.dao.BinDao;
import com.increff.assure.dao.BinSkuDao;
import com.increff.assure.model.Exception.ApiException;
import com.increff.assure.pojo.BinPojo;
import com.increff.assure.pojo.BinSkuPojo;
import com.increff.assure.pojo.join.BinFilter;
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
    public void createBins(Long numberOfBins) {
        Long i;
        for (i = new Long(0); i < numberOfBins; i++) {
            BinPojo pojo = new BinPojo();
            binDao.insert(pojo);
        }
    }


    @Transactional
    public void AddOrUpdateInventory(List<BinSkuPojo> pojoList) {
        for (BinSkuPojo pojo : pojoList) {
            BinSkuPojo existing = binSkuDao.checkPresenceByParams(pojo.getBinId(), pojo.getGlobalSkuId());
            if (existing != null) {
                existing.setQuantity(pojo.getQuantity() + existing.getQuantity());
                binSkuDao.update(existing);
                continue;
            }
            binSkuDao.insert(pojo);
        }
    }

    @Transactional(readOnly = true)
    public List<BinFilter> getProductInfo(Long binId, Long globalSkuId) throws ApiException {
        checkBinId(binId);
        return binSkuDao.selectProductInfoByBinAndGlobalSku(binId, globalSkuId);
    }

    @Transactional(readOnly = true)
    public List<BinFilter> getProducts(Long globalSkuId) {
        return binSkuDao.selectProductInfoByGlobalSku(globalSkuId);
    }

    @Transactional(readOnly = true)
    public List<BinFilter> getProductInfo(Long binId) throws ApiException {
        checkBinId(binId);
        return binSkuDao.selectProductInfoByBinId(binId);
    }

    @Transactional(rollbackFor = ApiException.class)
    public void update(Long binSkuId, Long quantity) throws ApiException {

        BinSkuPojo existing = binSkuDao.select(binSkuId);
        if (existing == null)
            throw new ApiException("The bin SKU ID doesn't exist");

        existing.setQuantity(quantity);
        binSkuDao.update(existing);
    }

    @Transactional(readOnly = true)
    public void checkBinId(Long binId) throws ApiException {
        BinPojo existing = binDao.select(binId);
        if (existing == null)
            throw new ApiException("Bin ID:" + binId + " doesn't exist");
    }

    @Transactional(readOnly = true)
    public BinSkuPojo get(Long binSkuId) {
        return binSkuDao.select(binSkuId);
    }

    @Transactional(readOnly = true)
    public BinSkuPojo getCheck(Long binSkuId) throws ApiException {
        BinSkuPojo pojo = get(binSkuId);
        if (pojo == null)
            throw new ApiException("Bin SKU ID is not valid");
        return pojo;
    }

    @Transactional(readOnly = true)
    public List<BinSkuPojo> getBins(Long globalSkuId) {
        return binSkuDao.selectByGlobalSku(globalSkuId);
    }
}
