package com.hikiw.ecommerce.Repository;


import com.hikiw.ecommerce.Entity.AttributeKeyEntity;
import com.hikiw.ecommerce.Entity.ProductAttributeValueEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductAttributeValueRepository extends JpaRepository<ProductAttributeValueEntity, Long> {
    boolean existsByAttributeKey(AttributeKeyEntity entity);
}
