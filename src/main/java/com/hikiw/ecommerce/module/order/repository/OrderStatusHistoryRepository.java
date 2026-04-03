package com.hikiw.ecommerce.module.order.repository;

import com.hikiw.ecommerce.module.order.entity.OrderStatusHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderStatusHistoryRepository extends JpaRepository<OrderStatusHistoryEntity, Long> {
    List<OrderStatusHistoryEntity> findByOrder_OrderIdOrderByCreatedDateAsc(Long orderId);

}
