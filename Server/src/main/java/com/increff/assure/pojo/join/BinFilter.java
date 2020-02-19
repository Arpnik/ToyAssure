package com.increff.assure.pojo.join;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
//todo keep out of pojo list
@Getter @Setter @AllArgsConstructor
public class BinFilter {
    private long clientId;
    private String name;//clientName
    private String clientSkuId;
    private long quantity;
    private long binId;
    private long binSkuId;

    public BinFilter(long binSkuId,long binId, long quantity)
    {
        this.quantity=quantity;
        this.binSkuId=binSkuId;
        this.binId=binId;
    }
}
