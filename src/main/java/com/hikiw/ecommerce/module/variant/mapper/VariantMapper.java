package com.hikiw.ecommerce.module.variant.mapper;


import com.hikiw.ecommerce.module.variant.dto.VariantCreationRequest;
import com.hikiw.ecommerce.module.variant.dto.VariantDetailResponse;
import com.hikiw.ecommerce.module.variant.dto.VariantResponse;
import com.hikiw.ecommerce.module.variant.dto.VariantUpdateRequest;
import com.hikiw.ecommerce.module.variant.entity.VariantEntity;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface VariantMapper {

    VariantEntity toVariantEntity(VariantCreationRequest request);

    @Mapping(target = "totalValues", expression = "java(entity.getVariantValues() == null ? 0 : entity.getVariantValues().size())")
    VariantResponse toVariantResponse(VariantEntity entity);


    @Mapping(target = "totalValues", expression = "java(entity.getVariantValues() == null ? 0 : entity.getVariantValues().size())")
    @Mapping(target = "variantValues", source = "variantValues")
    VariantDetailResponse toVariantDetailResponse(VariantEntity entity);


    @BeanMapping(nullValuePropertyMappingStrategy = org.mapstruct.NullValuePropertyMappingStrategy.IGNORE)
    void updateVariant(@MappingTarget VariantEntity target, VariantUpdateRequest request);
}
