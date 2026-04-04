package com.hikiw.ecommerce.module.voucher.repository;

import com.hikiw.ecommerce.module.voucher.entity.VoucherEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface VoucherRepository extends JpaRepository<VoucherEntity, Long>, JpaSpecificationExecutor<VoucherEntity> {

    boolean existsByCode(String code);
    Optional<VoucherEntity> findByCode(String code);
    @Query("SELECT v FROM VoucherEntity v WHERE v.isActive = true " +
            "AND v.startDate <= :now AND v.endDate >= :now " +
            "AND (v.usageLimit IS NULL OR v.usedCount < v.usageLimit)")
    List<VoucherEntity> findAllValid(@Param("now") LocalDateTime now);

    Optional<VoucherEntity> findByCodeAndShop_ShopId(String code, Long shopId);
}
