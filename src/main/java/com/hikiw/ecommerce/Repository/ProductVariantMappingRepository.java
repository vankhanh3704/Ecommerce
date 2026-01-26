package com.hikiw.ecommerce.Repository;


import com.hikiw.ecommerce.Entity.ProductVariantMappingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductVariantMappingRepository extends JpaRepository<ProductVariantMappingEntity, Long> {
    boolean existsByProductVariant_ProductVariantIdAndVariantValue_VariantValueId(Long productVariantId, Long variantValueId);
    List<ProductVariantMappingEntity> findByProductVariant_ProductVariantId(Long productVariantId);
}
