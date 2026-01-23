package com.hikiw.ecommerce.Mapper;

import com.hikiw.ecommerce.Entity.VariantEntity;
import com.hikiw.ecommerce.Entity.VariantValuesEntity;
import com.hikiw.ecommerce.Model.Request.variant.values.VariantValuesCreationRequest;
import com.hikiw.ecommerce.Model.Response.VariantValuesResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface VariantValuesMapper {

    VariantValuesEntity toEntity(VariantValuesCreationRequest request);

    @Mapping(target = "variantId", source = "variant.variantId")
    @Mapping(target = "variantName", source = "variant.variantName")
    VariantValuesResponse toResponse(VariantValuesEntity entity);
}
