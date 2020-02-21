package com.increff.assure.pojo;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(
        indexes = @Index(name = "client_id_sku", columnList = "clientId, clientSkuId", unique = true)
)
@Getter @Setter
public class ProductPojo extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long globalSkuId;
    @NotNull
    private String clientSkuId;
    @NotNull
    private Long clientId;
    @NotNull
    private String name;
    @NotNull
    private String brandId;
    @NotNull
    private Double mrp;
    @NotNull
    private String description;//TODO size handle

}
