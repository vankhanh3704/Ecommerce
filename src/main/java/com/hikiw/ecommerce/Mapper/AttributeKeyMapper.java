package com.hikiw.ecommerce.Mapper;


import com.hikiw.ecommerce.Entity.AttributeKeyEntity;
import com.hikiw.ecommerce.Model.Request.product.attribute.AttributeKeyCreationRequest;
import com.hikiw.ecommerce.Model.Response.AttributeKeyResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AttributeKeyMapper {


    AttributeKeyEntity toEntity(AttributeKeyCreationRequest request);


    @Mapping(target = "dataType", source = "dataType")
    AttributeKeyResponse toResponse(AttributeKeyEntity entity);
}
