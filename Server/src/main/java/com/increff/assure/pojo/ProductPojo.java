package com.increff.assure.pojo;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(
        indexes = @Index(name = "client_id_sku", columnList = "clientId, clientSkuId", unique = true)
)
@Getter @Setter
public class ProductPojo extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long globalSkuId;
    private String clientSkuId;
    private long clientId;
    private String name;
    private String brandId;
    private double mrp;
    private String description;

}
