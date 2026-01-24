package com.hikiw.ecommerce.Mapper;


import com.hikiw.ecommerce.Entity.VariantEntity;
import com.hikiw.ecommerce.Model.Request.variant.VariantCreationRequest;
import com.hikiw.ecommerce.Model.Request.variant.VariantUpdateRequest;
import com.hikiw.ecommerce.Model.Response.VariantDetailResponse;
import com.hikiw.ecommerce.Model.Response.VariantResponse;
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
