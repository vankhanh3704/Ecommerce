package com.hikiw.ecommerce.module.image.mapper;


import com.hikiw.ecommerce.module.image.dto.ProductImageResponse;
import com.hikiw.ecommerce.module.image.entity.ProductImageEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductImageMapper {

    @Mapping(source = "product.productId", target = "productId")
    @Mapping(target = "thumbnailUrl", ignore = true)  // Set in service
    @Mapping(target = "mediumUrl", ignore = true)     // Set in service
    ProductImageResponse toResponse(ProductImageEntity entity);

    List<ProductImageResponse> toResponseList(List<ProductImageEntity> entities);

}
