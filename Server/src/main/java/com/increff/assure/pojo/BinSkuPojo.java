package com.increff.assure.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(
        indexes = @Index(name="bin_global_sku",columnList = "binId,globalSkuId",unique = true)
)
@Getter @Setter
public class BinSkuPojo extends AbstractEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Long binId;
    @NotNull
    private Long globalSkuId;
    @NotNull
    private Long quantity;

}
