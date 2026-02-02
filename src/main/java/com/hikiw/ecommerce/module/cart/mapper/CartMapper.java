package com.hikiw.ecommerce.module.cart.mapper;


import com.hikiw.ecommerce.module.cart.dto.CartCreationRequest;
import com.hikiw.ecommerce.module.cart.dto.CartResponse;
import com.hikiw.ecommerce.module.cart.entity.CartEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CartMapper {

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "totalItems", expression = "java(cart.getTotalItems())")
    @Mapping(target = "totalPrice", expression = "java(cart.getTotalPrice())")
    @Mapping(target = "items", source = "items")
    CartResponse toResponse(CartEntity cart);



}
