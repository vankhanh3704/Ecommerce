package com.hikiw.ecommerce.module.image.repository;


import com.hikiw.ecommerce.module.image.entity.ProductImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImageEntity, Long> {

    @Modifying
    @Query("UPDATE ProductImageEntity pi SET pi.isPrimary = false WHERE pi.product.productId = :productId")
    void unsetAllPrimaryImages(@Param("productId") Long productId);

    long countByProduct_ProductId(Long productId);

}
