package com.hikiw.ecommerce.Repository;


import com.hikiw.ecommerce.Entity.VariantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface VariantRepository extends JpaRepository<VariantEntity, Long> {
    boolean existsByVariantName(String variantName);

    @Query("SELECT COUNT(vv) FROM VariantValuesEntity vv WHERE vv.variant.variantId = :variantId")
    Long countVariantValues(@Param("variantId") Long variantId);
}
