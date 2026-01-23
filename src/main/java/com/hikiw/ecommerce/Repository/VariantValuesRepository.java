package com.hikiw.ecommerce.Repository;

import com.hikiw.ecommerce.Entity.VariantValuesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VariantValuesRepository extends JpaRepository<VariantValuesEntity, Long> {
}
