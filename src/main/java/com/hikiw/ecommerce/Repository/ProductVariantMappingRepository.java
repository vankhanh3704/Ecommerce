package com.hikiw.ecommerce.Repository;


import com.hikiw.ecommerce.Entity.ProductVariantMappingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductVariantMappingRepository extends JpaRepository<ProductVariantMappingEntity, Long> {
}
