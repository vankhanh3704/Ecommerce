package com.hikiw.ecommerce.Mapper;

import com.hikiw.ecommerce.Entity.ShopEntity;
import com.hikiw.ecommerce.Entity.ShopLocationEntity;
import com.hikiw.ecommerce.Model.Request.ShopLocationCreationRequest;
import com.hikiw.ecommerce.Model.Response.ShopLocationResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ShopLocationMapper {
    // ========== REQUEST → ENTITY ==========

    @Mapping(target = "shopLocationId", ignore = true)
    @Mapping(target = "shop", ignore = true) // Set manually in service
    @Mapping(target = "products", ignore = true)
    @Mapping(target = "country", defaultValue = "Vietnam")
    @Mapping(target = "isDefaultPickup", defaultValue = "false")
    @Mapping(target = "isDefaultReturn", defaultValue = "false")
    @Mapping(target = "isActive", constant = "true")
    ShopLocationEntity toEntity(ShopLocationCreationRequest request);

    // ========== ENTITY → RESPONSE ==========

    @Mapping(source = "shop.shopId", target = "shopId")
    @Mapping(source = "shop.shopName", target = "shopName")
    @Mapping(expression = "java(entity.getFullAddress())", target = "fullAddress")
    ShopLocationResponse toResponse(ShopLocationEntity entity);
}
