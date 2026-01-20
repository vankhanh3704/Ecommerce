package com.hikiw.ecommerce.Mapper;

import com.hikiw.ecommerce.Entity.ShopEntity;
import com.hikiw.ecommerce.Model.Request.ShopCreateRequest;
import com.hikiw.ecommerce.Model.Request.ShopUpdateRequest;
import com.hikiw.ecommerce.Model.Response.ShopResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

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
    ShopEntity toEntity(ShopCreateRequest request);

    // entity -> response
    @Mapping(source = "owner.id", target = "ownerUserId")
    @Mapping(source = "owner.username", target = "ownerName")
    ShopResponse toResponse(ShopEntity request);

    void toUpdateShop(@MappingTarget ShopEntity shop, ShopUpdateRequest request);
}
