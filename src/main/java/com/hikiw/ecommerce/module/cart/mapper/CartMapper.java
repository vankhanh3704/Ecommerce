package com.hikiw.ecommerce.module.cart.mapper;


import com.hikiw.ecommerce.module.cart.dto.CartItemResponse;
import com.hikiw.ecommerce.module.cart.dto.CartResponse;
import com.hikiw.ecommerce.module.cart.entity.CartEntity;
import com.hikiw.ecommerce.module.cart.entity.CartItemEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CartMapper {

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "totalItems", expression = "java(cart.getTotalItems())")
    @Mapping(target = "totalAmount", expression = "java(cart.getTotalPrice())")
    @Mapping(target = "items", source = "items")
    CartResponse toResponse(CartEntity cart);



    @Mapping(source = "productVariant.productVariantId", target = "productVariantId")
    @Mapping(source = "productVariant.product.productId", target = "productId")
    @Mapping(source = "productVariant.product.productName", target = "productName")
    @Mapping(source = "productVariant.sku", target = "sku")
    @Mapping(source = "productVariant.imageUrl", target = "productImageUrl")
    @Mapping(source = "productVariant.price", target = "price")
    @Mapping(source = "productVariant.oldPrice", target = "oldPrice")
    @Mapping(source = "productVariant.stock", target = "stockAvailable")
    CartItemResponse toItemResponse(CartItemEntity cartItem);



}
