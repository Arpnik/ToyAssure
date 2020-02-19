package com.increff.assure.pojo;

import com.increff.assure.model.constants.InvoiceType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(
        indexes = @Index(name="channel_name",columnList = "name",unique = true)
)
@Getter @Setter
public class ChannelPojo extends AbstractEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(columnDefinition = "varchar(255) default 'INTERNAL'")
    private String name;
    @Column(columnDefinition = "varchar(255) default 'SELF'")
    @Enumerated(value = EnumType.STRING)
    private InvoiceType invoiceType;

}
