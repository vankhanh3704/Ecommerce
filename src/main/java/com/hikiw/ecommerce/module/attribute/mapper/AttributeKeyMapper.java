package com.hikiw.ecommerce.module.attribute.mapper;


import com.hikiw.ecommerce.module.attribute.entity.AttributeKeyEntity;
import com.hikiw.ecommerce.module.attribute.dto.AttributeKeyCreationRequest;
import com.hikiw.ecommerce.module.attribute.dto.AttributeKeyUpdateRequest;
import com.hikiw.ecommerce.module.attribute.dto.AttributeKeyResponse;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AttributeKeyMapper {

    AttributeKeyEntity toEntity(AttributeKeyCreationRequest request);


    @Mapping(target = "dataType", source = "dataType")
    @Mapping(target = "isActive", constant = "true")
    AttributeKeyResponse toResponse(AttributeKeyEntity entity);

    @BeanMapping(nullValuePropertyMappingStrategy = org.mapstruct.NullValuePropertyMappingStrategy.IGNORE)
    void updateAttributeKey(@MappingTarget AttributeKeyEntity entity, AttributeKeyUpdateRequest request);
}
