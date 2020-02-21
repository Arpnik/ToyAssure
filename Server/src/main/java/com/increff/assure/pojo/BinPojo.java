package com.increff.assure.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
public class BinPojo extends AbstractEntity {
    @TableGenerator(name = "bin_gen", pkColumnValue = "bin_gen", initialValue = 1000)
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "bin_gen")
    private Long binId;

}
