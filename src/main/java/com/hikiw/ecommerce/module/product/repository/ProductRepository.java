package com.hikiw.ecommerce.module.product.repository;

import com.hikiw.ecommerce.module.product.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
    boolean existsByProductName(String name);
}
