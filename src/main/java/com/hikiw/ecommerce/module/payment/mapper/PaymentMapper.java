package com.hikiw.ecommerce.module.payment.mapper;


import com.hikiw.ecommerce.module.order.entity.OrderEntity;
import com.hikiw.ecommerce.module.payment.dto.PaymentResponse;
import com.hikiw.ecommerce.module.payment.entity.PaymentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PaymentMapper {

    @Mapping(target = "paymentUrl", ignore = true)
    @Mapping(target = "orderIds", expression = "java(mapOrderIds(entity.getOrders()))")
    @Mapping(target = "orderCodes", expression = "java(mapOrderCodes(entity.getOrders()))")
    PaymentResponse toResponse(PaymentEntity entity);

    default List<Long> mapOrderIds(List<OrderEntity> orders) {
        if (orders == null) return new ArrayList<>();
        return orders.stream().map(OrderEntity::getOrderId).toList();
    }

    default List<String> mapOrderCodes(List<OrderEntity> orders) {
        if (orders == null) return new ArrayList<>();
        return orders.stream().map(OrderEntity::getOrderCode).toList();
    }
}
