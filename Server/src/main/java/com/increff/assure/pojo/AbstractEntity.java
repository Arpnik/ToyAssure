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
public class AbstractEntity {

    @CreationTimestamp
    @Column(updatable=false)
    private ZonedDateTime createdDate;

    @UpdateTimestamp
    private ZonedDateTime modifiedDate;


    @Version
    private Long version;

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

}
