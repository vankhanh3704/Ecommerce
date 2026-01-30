package com.hikiw.ecommerce.module.attribute.repository;

import com.hikiw.ecommerce.module.attribute.entity.AttributeKeyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttributeKeyRepository extends JpaRepository<AttributeKeyEntity, Long> {
    Boolean existsByKeyName(String keyName);
    @Query("SELECT a FROM AttributeKeyEntity a WHERE a.isActive = true ORDER BY a.displayOrder ASC")
    List<AttributeKeyEntity> findAllActiveOrderedByDisplayOrder();
}
