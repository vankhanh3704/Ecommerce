package com.hikiw.ecommerce.module.order.mapper;


import com.hikiw.ecommerce.module.order.dto.OrderItemResponse;
import com.hikiw.ecommerce.module.order.dto.OrderResponse;
import com.hikiw.ecommerce.module.order.entity.OrderEntity;
import com.hikiw.ecommerce.module.order.entity.OrderItemEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderMapper {
    @Mapping(target = "userId", source = "user.id")
    @Mapping(source = "user.username", target = "username")
    @Mapping(source = "voucher.code", target = "voucherCode")
    @Mapping(source = "statusHistory", target = "statusHistory")
    OrderResponse toResponse(OrderEntity entity);

    List<OrderResponse> toResponseList(List<OrderEntity> entities);
    OrderItemResponse toItemResponse(OrderItemEntity entity);
}
