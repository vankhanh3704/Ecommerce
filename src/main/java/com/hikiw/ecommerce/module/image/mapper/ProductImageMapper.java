package com.hikiw.ecommerce.module.image.mapper;


import com.hikiw.ecommerce.module.image.dto.ProductImageResponse;
import com.hikiw.ecommerce.module.image.dto.ProductImageUpdateRequest;
import com.hikiw.ecommerce.module.image.entity.ProductImageEntity;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductImageMapper {

    @Mapping(source = "product.productId", target = "productId")
    @Mapping(target = "thumbnailUrl", ignore = true)  // Set in service
    @Mapping(target = "mediumUrl", ignore = true)     // Set in service
    ProductImageResponse toResponse(ProductImageEntity entity);

    List<ProductImageResponse> toResponseList(List<ProductImageEntity> entities);


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void toUpdate(@MappingTarget ProductImageEntity entity, ProductImageUpdateRequest request);

}
