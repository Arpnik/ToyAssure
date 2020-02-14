package com.increff.assure.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
@Table(
        indexes = @Index(name="uniqueConstraintBinSku",columnList = "binId,globalSkuId",unique = true)
)
public class BinSkuPojo extends AbstractEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private long binId;
    private long globalSkuId;
    private long quantity;

}
