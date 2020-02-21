package com.increff.assure.pojo.join;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
@Getter @Setter @AllArgsConstructor
public class BinFilter {
    private Long clientId;
    private String name;//clientName
    private String clientSkuId;
    private Long quantity;
    private Long binId;
    private Long binSkuId;

    public BinFilter(Long binSkuId,Long binId, Long quantity)
    {
        this.quantity=quantity;
        this.binSkuId=binSkuId;
        this.binId=binId;
    }
}
