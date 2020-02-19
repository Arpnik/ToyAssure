package com.increff.assure.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(
        indexes = @Index(name="bin_global_sku",columnList = "binId,globalSkuId",unique = true)
)
@Getter @Setter
public class BinSkuPojo extends AbstractEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private long binId;
    private long globalSkuId;
    private long quantity;

}
