package com.hikiw.ecommerce.Mapper;

import com.hikiw.ecommerce.Entity.ProductVariantMappingEntity;
import com.hikiw.ecommerce.Model.Request.product.variant.mapping.ProductVariantMappingCreationRequest;
import com.hikiw.ecommerce.Model.Response.ProductVariantMappingResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductVariantMappingMapper {



    @Mapping(target = "productVariantId", source = "productVariant.productVariantId")
    @Mapping(target = "productVariantSku", source = "productVariant.sku")
    @Mapping(target = "variantValueId", source = "variantValue.variantValueId")
    @Mapping(target = "variantName", source = "variantValue.variant.variantName")
    @Mapping(target = "valueName", source = "variantValue.valueName")
    @Mapping(target = "valueImageUrl", source = "variantValue.imageUrl")
    @Mapping(target = "fullInfo", expression = "java(entity.getFullInfo())")
    ProductVariantMappingResponse toResponse(ProductVariantMappingEntity entity);


    @Mapping(target = "mappingId", ignore = true)
    @Mapping(target = "productVariant", ignore = true)
    @Mapping(target = "variantValue", ignore = true)
    ProductVariantMappingEntity toEntity(ProductVariantMappingCreationRequest request);
}
