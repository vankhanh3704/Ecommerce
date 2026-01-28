package com.hikiw.ecommerce.Mapper;


import com.hikiw.ecommerce.Entity.ProductAttributeValueEntity;
import com.hikiw.ecommerce.Model.Request.product.attribute.ProductAttributeValueCreationRequest;
import com.hikiw.ecommerce.Model.Request.product.attribute.ProductAttributeValueUpdateRequest;
import com.hikiw.ecommerce.Model.Response.ProductAttributeValueResponse;
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
