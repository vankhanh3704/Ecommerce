package com.hikiw.ecommerce.Repository;


import com.hikiw.ecommerce.Entity.ProductVariantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductVariantRepository extends JpaRepository<ProductVariantEntity, Long> {
    Boolean existsBySku(String sku);
}
