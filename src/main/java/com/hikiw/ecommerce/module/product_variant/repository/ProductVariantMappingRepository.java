package com.hikiw.ecommerce.module.product_variant.repository;


import com.hikiw.ecommerce.module.product_variant.entity.ProductVariantMappingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductVariantMappingRepository extends JpaRepository<ProductVariantMappingEntity, Long> {
    boolean existsByProductVariant_ProductVariantIdAndVariantValue_VariantValueId(Long productVariantId, Long variantValueId);
    List<ProductVariantMappingEntity> findByProductVariant_ProductVariantId(Long productVariantId);
}
