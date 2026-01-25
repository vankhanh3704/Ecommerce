package com.hikiw.ecommerce.Mapper;

import com.hikiw.ecommerce.Entity.VariantValuesEntity;
import com.hikiw.ecommerce.Model.Request.variant.values.VariantValuesCreationRequest;
import com.hikiw.ecommerce.Model.Request.variant.values.VariantValuesUpdateRequest;
import com.hikiw.ecommerce.Model.Response.VariantValuesDetailResponse;
import com.hikiw.ecommerce.Model.Response.VariantValuesResponse;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface VariantValuesMapper {

    VariantValuesEntity toEntity(VariantValuesCreationRequest request);

    @Mapping(target = "variantId", source = "variant.variantId")
    @Mapping(target = "variantName", source = "variant.variantName")
    VariantValuesResponse toResponse(VariantValuesEntity entity);


    @Mapping(target = "variantId", source = "variant.variantId")
    @Mapping(target = "variantName", source = "variant.variantName")
    VariantValuesDetailResponse toDetailResponse(VariantValuesEntity entity);


    @BeanMapping(nullValuePropertyMappingStrategy = org.mapstruct.NullValuePropertyMappingStrategy.IGNORE)
    void toUpdateVariantValue(@MappingTarget VariantValuesEntity entity, VariantValuesUpdateRequest request);
}
