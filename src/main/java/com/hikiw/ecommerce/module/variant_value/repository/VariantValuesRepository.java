package com.hikiw.ecommerce.module.variant_value.repository;

import com.hikiw.ecommerce.module.variant_value.entity.VariantValuesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VariantValuesRepository extends JpaRepository<VariantValuesEntity, Long> {
    // Check if a variant value with the given variant ID and value name exists
    boolean existsByVariant_VariantIdAndValueName(Long variantId, String valueName);

    @Query("Select vv from VariantValuesEntity vv where vv.variant.variantId = :variantId ORDER BY vv.displayOrder ASC, vv.valueName ASC")
    List<VariantValuesEntity> findByVariantIdOrderedByDisplayOrder(@Param("variantId") Long variantId);

    List<VariantValuesEntity> findByVariantValueIdIn(List<Long> variantValueIds);
}
