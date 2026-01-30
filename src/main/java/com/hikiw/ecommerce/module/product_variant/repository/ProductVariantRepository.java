package com.hikiw.ecommerce.module.product_variant.repository;


import com.hikiw.ecommerce.module.product_variant.entity.ProductVariantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductVariantRepository extends JpaRepository<ProductVariantEntity, Long> {
    Boolean existsBySku(String sku);
}
