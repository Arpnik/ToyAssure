package com.increff.assure.pojo;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(
        indexes = @Index(name="uniqueConstraintProduct",columnList = "clientId,clientSkuId",unique = true)
)
@Getter @Setter
public class ProductPojo extends AbstractEntity {
    @Id
    @SequenceGenerator(name = "mySeqGen", sequenceName = "mySeq", initialValue = 100,allocationSize = 1)
    @GeneratedValue(generator = "mySeqGen")
    private long globalSkuId;
    private String clientSkuId;
    private long clientId;
    private String name;
    private String brandId;
    private double mrp;
    private String description;

}
