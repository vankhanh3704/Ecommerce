package com.hikiw.ecommerce.common.constant;


import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public class BaseAuditEntity extends BaseEntity{
    @Column(nullable = false)
    Boolean isActive = true;
}
