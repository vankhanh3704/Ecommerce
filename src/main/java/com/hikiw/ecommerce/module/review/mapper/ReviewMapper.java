package com.hikiw.ecommerce.module.review.mapper;

import com.hikiw.ecommerce.module.review.dto.ReviewReplyResponse;
import com.hikiw.ecommerce.module.review.dto.ReviewResponse;
import com.hikiw.ecommerce.module.review.entity.ReviewEntity;
import com.hikiw.ecommerce.module.review.entity.ReviewReplyEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ReviewMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.username", target = "username")
    @Mapping(source = "product.productId", target = "productId")
    @Mapping(source = "product.productName", target = "productName")
    @Mapping(source = "orderItem.orderItemId", target = "orderItemId")
    @Mapping(source = "orderItem.variantInfo", target = "variantInfo")
    @Mapping(target = "imageUrls", ignore = true)  // parse JSON trong service
    ReviewResponse toResponse(ReviewEntity entity);

    List<ReviewResponse> toResponseList(List<ReviewEntity> entities);

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.username", target = "username")
    ReviewReplyResponse toReplyResponse(ReviewReplyEntity entity);
}