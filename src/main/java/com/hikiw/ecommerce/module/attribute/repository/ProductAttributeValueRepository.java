package com.hikiw.ecommerce.module.attribute.repository;


import com.hikiw.ecommerce.module.attribute.entity.AttributeKeyEntity;
import com.hikiw.ecommerce.module.attribute.entity.ProductAttributeValueEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductAttributeValueRepository extends JpaRepository<ProductAttributeValueEntity, Long> {
    boolean existsByAttributeKey(AttributeKeyEntity entity);

    List<ProductAttributeValueEntity> findByProduct_ProductId(Long productId);

    void deleteByProduct_ProductId(Long productId);

    boolean existsByProduct_ProductIdAndAttributeKey_AttributeKeyId(Long productId, Long attributeKeyId);
}
