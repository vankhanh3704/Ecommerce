package com.hikiw.ecommerce.module.wishlist.mapper;


import com.hikiw.ecommerce.module.wishlist.dto.WishlistResponse;
import com.hikiw.ecommerce.module.wishlist.entity.WishlistEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface WishlistMapper {

    @Mapping(source = "product.productId", target = "productId")
    @Mapping(source = "product.productName", target = "productName")
    @Mapping(source = "product.basePrice", target = "basePrice")
    @Mapping(source = "product.isActive", target = "isActive")
    @Mapping(source = "createdDate", target = "addedDate")
    // minPrice, maxPrice, isInStock, productImageUrl set trong service
    @Mapping(target = "minPrice", ignore = true)
    @Mapping(target = "maxPrice", ignore = true)
    @Mapping(target = "isInStock", ignore = true)
    @Mapping(target = "productImageUrl", ignore = true)
    WishlistResponse toResponse(WishlistEntity entity);
}
