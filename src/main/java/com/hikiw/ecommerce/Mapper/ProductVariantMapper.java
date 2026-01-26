package com.hikiw.ecommerce.Mapper;

import com.hikiw.ecommerce.Entity.ProductVariantEntity;
import com.hikiw.ecommerce.Model.Request.product.variant.ProductVariantCreationRequest;
import com.hikiw.ecommerce.Model.Request.product.variant.ProductVariantUpdateRequest;
import com.hikiw.ecommerce.Model.Response.ProductVariantDetailResponse;
import com.hikiw.ecommerce.Model.Response.ProductVariantResponse;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductVariantMapper {

    @Mapping(target = "productId", source = "product.productId")
    @Mapping(target = "productName", source = "product.productName")
    @Mapping(target = "inStock", expression = "java(entity.getInStock())")
    @Mapping(target = "discountPercentage", expression = "java(entity.getDiscountPercentage())")
    @Mapping(target = "variantInfo", expression = "java(entity.getVariantInfoString())")
    ProductVariantResponse toResponse(ProductVariantEntity entity);

    @Mapping(target = "productVariantId", ignore = true)
    @Mapping(target = "variantMappings", ignore = true)
    @Mapping(target = "isActive", constant = "true")
    @Mapping(target = "soldCount", constant = "0")
    ProductVariantEntity toEntity(ProductVariantCreationRequest request);



    @Mapping(target = "productId", source = "product.productId")
    @Mapping(target = "productName", source = "product.productName")
    @Mapping(target = "inStock", expression = "java(entity.getInStock())")
    @Mapping(target = "discountPercentage", expression = "java(entity.getDiscountPercentage())")
    @Mapping(target = "variantValues", ignore = true)
    ProductVariantDetailResponse toDetailResponse(ProductVariantEntity entity);


    @BeanMapping(nullValuePropertyMappingStrategy = org.mapstruct.NullValuePropertyMappingStrategy.IGNORE)
    void updateProductVariant(@MappingTarget ProductVariantEntity entity, ProductVariantUpdateRequest request);


}
