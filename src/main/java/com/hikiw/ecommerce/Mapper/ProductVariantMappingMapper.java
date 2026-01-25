package com.hikiw.ecommerce.Mapper;

import com.hikiw.ecommerce.Entity.ProductVariantMappingEntity;
import com.hikiw.ecommerce.Model.Request.product.variant.ProductVariantMappingCreationRequest;
import com.hikiw.ecommerce.Model.Response.ProductVariantMappingResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductVariantMappingMapper {



    @Mapping(target = "productVariantId", source = "productVariant.productVariantId")
    @Mapping(target = "productVariantSku", source = "productVariant.sku")
    ProductVariantMappingResponse toResponse(ProductVariantMappingEntity entity);


    @Mapping(target = "mappingId", ignore = true)
    @Mapping(target = "productVariant", ignore = true)
    @Mapping(target = "variantValue", ignore = true)
    ProductVariantMappingEntity toEntity(ProductVariantMappingCreationRequest request);
}
