package com.hikiw.ecommerce.module.variant_value.mapper;

import com.hikiw.ecommerce.module.variant_value.entity.VariantValuesEntity;
import com.hikiw.ecommerce.module.variant_value.dto.VariantValuesCreationRequest;
import com.hikiw.ecommerce.module.variant_value.dto.VariantValuesUpdateRequest;
import com.hikiw.ecommerce.module.variant_value.dto.VariantValuesDetailResponse;
import com.hikiw.ecommerce.module.variant_value.dto.VariantValuesResponse;
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
