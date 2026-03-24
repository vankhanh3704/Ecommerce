package com.hikiw.ecommerce.common.constant;


import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
public class BaseEntity {
    @Column(name = "created_date", updatable = false)
    LocalDateTime createdDate;

    @Column(name = "updated_date")
    LocalDateTime updatedDate;

    @PrePersist
    void prePersist(){
        this.createdDate = LocalDateTime.now();
    }
    @PreUpdate
    void preUpdate(){
        this.updatedDate = LocalDateTime.now();
    }
}
