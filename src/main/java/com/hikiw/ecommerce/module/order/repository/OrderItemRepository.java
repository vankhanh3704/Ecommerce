package com.hikiw.ecommerce.module.order.repository;

import com.hikiw.ecommerce.module.order.entity.OrderEntity;
import com.hikiw.ecommerce.module.order.entity.OrderItemEntity;
import com.hikiw.ecommerce.module.order.entity.OrderStatusHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItemEntity, Long> {
    List<OrderItemEntity> findByOrder_OrderIdOrderByCreatedDateAsc(Long orderId);

}
