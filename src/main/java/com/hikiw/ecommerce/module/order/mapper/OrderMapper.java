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
    @Mapping(source = "statusHistory", target = "statusHistory")
    @Mapping(source = "shop.shopId", target = "shopId")       // ← thêm
    @Mapping(source = "shop.shopName", target = "shopName")   // ← thêm
    @Mapping(target = "shopVoucherCode", source = "voucher.code") // ← Fix target tên mới
    OrderResponse toResponse(OrderEntity entity);


    List<OrderResponse> toResponseList(List<OrderEntity> entities);
    @Mapping(target = "productVariantId", source = "productVariant.productVariantId") // ← Fix Variant ID
    OrderItemResponse toItemResponse(OrderItemEntity entity);
}
