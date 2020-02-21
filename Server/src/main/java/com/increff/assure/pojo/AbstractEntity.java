package com.increff.assure.pojo;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import java.time.ZonedDateTime;

@MappedSuperclass @Getter @Setter
public abstract class AbstractEntity {

    @CreationTimestamp
    @Column(updatable=false)
    private ZonedDateTime createdDate;

    @UpdateTimestamp
    private ZonedDateTime modifiedDate;


    @Version
    private Long version;


}
