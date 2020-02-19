package com.increff.assure.pojo;

import com.increff.assure.model.constants.MemberTypes;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(
        indexes = @Index(name="member_name_type", columnList = "name, type", unique = true)
)
@Getter @Setter
public class MemberPojo extends AbstractEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    @Enumerated(value = EnumType.STRING)
    private MemberTypes type;

}
