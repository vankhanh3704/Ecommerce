package com.hikiw.ecommerce.Mapper;


import com.hikiw.ecommerce.Entity.ProductEntity;
import com.hikiw.ecommerce.Model.Request.ProductCreateRequest;
import com.hikiw.ecommerce.Model.Response.ProductResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {


    @Mapping(target = "productId", ignore = true)
    @Mapping(target = "shop", ignore = true) // Set manually in service
    @Mapping(target = "category", ignore = true) // Set manually in service
    @Mapping(target = "shopLocation", ignore = true) // Set manually in service
    ProductEntity toProductEntity(ProductCreateRequest request);

    @Mapping(target = "shopId", source = "shop.shopId")
    @Mapping(target = "shopName", source = "shop.shopName")
    @Mapping(target = "categoryName", source = "category.categoryName")
    @Mapping(target = "categoryId", source = "category.categoryId")
    @Mapping(target = "shopLocationId", source = "shopLocation.shopLocationId")
    @Mapping(target = "shopLocationName", source = "shopLocation.locationName")
    ProductResponse toProductResponse(ProductEntity entity);
}
