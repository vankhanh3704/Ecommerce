package com.hikiw.ecommerce.module.voucher.repository;

import com.hikiw.ecommerce.module.voucher.entity.VoucherUsageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoucherUsageRepository extends JpaRepository<VoucherUsageEntity, Long> {
}
