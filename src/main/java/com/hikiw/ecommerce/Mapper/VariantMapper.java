package com.hikiw.ecommerce.Mapper;


import com.hikiw.ecommerce.Entity.VariantEntity;
import com.hikiw.ecommerce.Model.Request.variant.VariantCreationRequest;
import com.hikiw.ecommerce.Model.Response.VariantResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface VariantMapper {

    VariantEntity toVariantEntity(VariantCreationRequest request);

    VariantResponse toVariantResponse(VariantEntity entity);
}
