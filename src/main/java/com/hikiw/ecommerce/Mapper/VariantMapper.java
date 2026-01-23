package com.hikiw.ecommerce.Mapper;


import com.hikiw.ecommerce.Entity.VariantEntity;
import com.hikiw.ecommerce.Model.Request.variant.VariantCreationRequest;
import com.hikiw.ecommerce.Model.Response.VariantResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface VariantMapper {

    VariantEntity toVariantEntity(VariantCreationRequest request);

    @Mapping(target = "totalValues", expression = "java(entity.getVariantValues() == null ? 0 : entity.getVariantValues().size())")
    VariantResponse toVariantResponse(VariantEntity entity);
}
