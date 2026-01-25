package com.hikiw.ecommerce.Mapper;

import com.hikiw.ecommerce.Entity.ProductVariantEntity;
import com.hikiw.ecommerce.Model.Request.product.variant.ProductVariantCreationRequest;
import com.hikiw.ecommerce.Model.Response.ProductVariantResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductVariantMapper {

    @Mapping(target = "productId", source = "product.productId")
    @Mapping(target = "productName", source = "product.productName")
    @Mapping(target = "inStock", expression = "java(entity.getInStock())")
    @Mapping(target = "discountPercentage", expression = "java(entity.getDiscountPercentage())")
    @Mapping(target = "variantInfo", expression = "java(entity.getVariantInfo())")
    ProductVariantResponse toResponse(ProductVariantEntity entity);


    ProductVariantEntity toEntity(ProductVariantCreationRequest request);

}
