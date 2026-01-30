package com.hikiw.ecommerce.module.shop.mapper;

import com.hikiw.ecommerce.module.shop.entity.ShopEntity;
import com.hikiw.ecommerce.module.shop.dto.ShopCreationRequest;
import com.hikiw.ecommerce.module.shop.dto.ShopUpdateRequest;
import com.hikiw.ecommerce.module.shop.dto.ShopDetailResponse;
import com.hikiw.ecommerce.module.shop.dto.ShopResponse;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ShopMapper {


    // create -> entity
    @Mapping(target = "shopId", ignore = true)
    @Mapping(target = "owner", ignore = true) // Set manually in service
    @Mapping(target = "products", ignore = true)     // collection
    @Mapping(target = "rating", constant = "0.0")
    @Mapping(target = "totalProducts", constant = "0")
    @Mapping(target = "totalOrders", constant = "0")
    @Mapping(target = "isVerified", constant = "false")
    @Mapping(target = "isActive", constant = "true")
    @Mapping(target = "shopLocations", ignore = true)
    ShopEntity toEntity(ShopCreationRequest request);

    // entity -> response
    @Mapping(source = "owner.id", target = "ownerUserId")
    @Mapping(source = "owner.username", target = "ownerName")
    ShopResponse toResponse(ShopEntity request);

    @Mapping(source = "owner.id", target = "ownerUserId")
    @Mapping(source = "owner.username", target = "ownerName")
    ShopDetailResponse toDetailResponse(ShopEntity request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void toUpdateShop(@MappingTarget ShopEntity shop, ShopUpdateRequest request);
}
