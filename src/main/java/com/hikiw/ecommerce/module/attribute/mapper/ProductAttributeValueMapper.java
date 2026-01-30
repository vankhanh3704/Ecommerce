package com.hikiw.ecommerce.module.attribute.mapper;


import com.hikiw.ecommerce.module.attribute.entity.ProductAttributeValueEntity;
import com.hikiw.ecommerce.module.attribute.dto.ProductAttributeValueCreationRequest;
import com.hikiw.ecommerce.module.attribute.dto.ProductAttributeValueUpdateRequest;
import com.hikiw.ecommerce.module.attribute.dto.ProductAttributeValueResponse;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductAttributeValueMapper {



    ProductAttributeValueEntity toEntity(ProductAttributeValueCreationRequest request);

    @Mapping(target = "productId", source = "product.productId")
    @Mapping(target = "productName", source = "product.productName")
    @Mapping(target = "attributeKeyId", source = "attributeKey.attributeKeyId")
    @Mapping(target = "attributeKeyName", source = "attributeKey.keyName")
    @Mapping(target = "dataType", source = "attributeKey.dataType")
    @Mapping(target = "displayName", source = "attributeKey.displayName")
    ProductAttributeValueResponse toResponse(ProductAttributeValueEntity entity);


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void toUpdate(@MappingTarget ProductAttributeValueEntity entity, ProductAttributeValueUpdateRequest request);

    List<ProductAttributeValueResponse> toResponseList(List<ProductAttributeValueEntity> entity);
}
