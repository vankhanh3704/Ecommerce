package com.hikiw.ecommerce.module.payment.repository;

import com.hikiw.ecommerce.module.payment.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentEntity, Long> {

    Optional<PaymentEntity> findByOrders_OrderId(Long orderId);

    Optional<PaymentEntity> findByTransactionId(String transactionId);

    Optional<PaymentEntity> findByReferenceCode(String referenceCode);

    boolean existsByOrders_OrderId(Long orderId);
}