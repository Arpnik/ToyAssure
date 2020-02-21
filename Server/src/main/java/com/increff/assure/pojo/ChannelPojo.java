package com.increff.assure.pojo;

import com.increff.assure.model.constants.InvoiceType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(
        indexes = @Index(name="channel_name",columnList = "name",unique = true)
)
@Getter
@Setter
public class ChannelPojo extends AbstractEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String name;
    @NotNull
    @Enumerated(value = EnumType.STRING)
    private InvoiceType invoiceType;

}
