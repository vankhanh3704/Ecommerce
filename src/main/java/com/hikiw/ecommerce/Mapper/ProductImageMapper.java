package com.hikiw.ecommerce.Mapper;


import com.hikiw.ecommerce.Entity.ProductImageEntity;
import com.hikiw.ecommerce.Model.Response.image.ProductImageResponse;
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
