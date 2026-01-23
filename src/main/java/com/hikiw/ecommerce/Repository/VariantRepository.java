package com.hikiw.ecommerce.Repository;


import com.hikiw.ecommerce.Entity.VariantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VariantRepository extends JpaRepository<VariantEntity, Long> {
    boolean existsByVariantName(String variantName);
}
