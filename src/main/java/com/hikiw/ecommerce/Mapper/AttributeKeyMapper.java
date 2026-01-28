package com.hikiw.ecommerce.Mapper;


import com.hikiw.ecommerce.Entity.AttributeKeyEntity;
import com.hikiw.ecommerce.Model.Request.product.attribute.AttributeKeyCreationRequest;
import com.hikiw.ecommerce.Model.Request.product.attribute.AttributeKeyUpdateRequest;
import com.hikiw.ecommerce.Model.Response.AttributeKeyResponse;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.context.annotation.Bean;

@Mapper(componentModel = "spring")
public interface AttributeKeyMapper {

    AttributeKeyEntity toEntity(AttributeKeyCreationRequest request);


    @Mapping(target = "dataType", source = "dataType")
    @Mapping(target = "isActive", constant = "true")
    AttributeKeyResponse toResponse(AttributeKeyEntity entity);

    @BeanMapping(nullValuePropertyMappingStrategy = org.mapstruct.NullValuePropertyMappingStrategy.IGNORE)
    void updateAttributeKey(@MappingTarget AttributeKeyEntity entity, AttributeKeyUpdateRequest request);
}
