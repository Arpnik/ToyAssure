package com.increff.assure.dao;

import com.increff.assure.pojo.BinSkuPojo;
import com.increff.assure.pojo.join.BinFilter;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class BinSkuDao extends AbstractDao<BinSkuPojo> {

    private static String select_binId_globalSkuId="select p from BinSkuPojo p where p.binId=:binId and p.globalSkuId=:globalSkuId";
    private static String select_product_info_by_binId ="select new com.increff.assure.pojo.join.BinFilter(m.id,m.name,p.clientSkuId,b.quantity,b.binId,b.id) from BinSkuPojo b,ProductPojo p,MemberPojo m where b.binId=:binId and m.id=p.clientId and p.globalSkuId=b.globalSkuId";
    private static String select_product_info_by_binId_global ="select new com.increff.assure.pojo.join.BinFilter(m.id,m.name,p.clientSkuId,b.quantity,b.binId,b.id) from BinSkuPojo b,ProductPojo p,MemberPojo m where b.binId=:binId and p.globalSkuId=b.globalSkuId and m.id=p.clientId and b.globalSkuId=:globalSkuId";
    private static String select_product_info_by_global="select new com.increff.assure.pojo.join.BinFilter(b.id,b.binId,b.quantity) from BinSkuPojo b where b.globalSkuId=:globalSkuId";
    private static String select_globalSkuId="select p from BinSkuPojo p where p.globalSkuId=:globalSkuId order by p.quantity";

    public BinSkuPojo checkPresenceByParams(long binId, long globalSkuId) {
        TypedQuery<BinSkuPojo> query=getQuery(select_binId_globalSkuId);
        query.setParameter("globalSkuId",globalSkuId);
        query.setParameter("binId",binId);
        return getSingle(query);
    }

    public List<BinFilter> selectProductInfoByGlobalSku(long globalSkuId)
    {
        Query query=em().createQuery(select_product_info_by_global);
        query.setParameter("globalSkuId",globalSkuId);
        return query.getResultList();
    }


    public List<BinFilter> selectProductInfoByBinAndGlobalSku(long binId, long globalSkuId)
    {
        Query query=em().createQuery(select_product_info_by_binId_global);
        query.setParameter("globalSkuId",globalSkuId);
        query.setParameter("binId",binId);
        return query.getResultList();
    }


    public List<BinFilter> selectProductInfoByBinId(long binId)
    {
        Query query=em().createQuery(select_product_info_by_binId);
        query.setParameter("binId",binId);
        return query.getResultList();
    }

    public List<BinSkuPojo> selectByGlobalSku(long globalSkuId)
    {
        TypedQuery<BinSkuPojo> query = getQuery(select_globalSkuId);
        query.setParameter("globalSkuId", globalSkuId);
        return query.getResultList();

    }

    public void update(BinSkuPojo updated)
    {
    }

}
