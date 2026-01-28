package com.hikiw.ecommerce.Repository;


import com.hikiw.ecommerce.Entity.AttributeKeyEntity;
import com.hikiw.ecommerce.Entity.ProductAttributeValueEntity;
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
