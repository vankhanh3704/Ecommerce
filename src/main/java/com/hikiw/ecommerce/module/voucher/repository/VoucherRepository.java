package com.hikiw.ecommerce.module.voucher.repository;

import com.hikiw.ecommerce.module.voucher.entity.VoucherEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VoucherRepository extends JpaRepository<VoucherEntity, Long>, JpaSpecificationExecutor<VoucherEntity> {

    boolean existsByCode(String code);
    Optional<VoucherEntity> findByCode(String code);
}
