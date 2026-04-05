package com.hikiw.ecommerce.module.review.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hikiw.ecommerce.Enum.ErrorCode;
import com.hikiw.ecommerce.Enum.OrderStatus;
import com.hikiw.ecommerce.common.Exception.AppException;
import com.hikiw.ecommerce.module.order.entity.OrderItemEntity;
import com.hikiw.ecommerce.module.order.repository.OrderItemRepository;
import com.hikiw.ecommerce.module.product.entity.ProductEntity;
import com.hikiw.ecommerce.module.review.dto.ReviewCreationRequest;
import com.hikiw.ecommerce.module.review.dto.ReviewResponse;
import com.hikiw.ecommerce.module.review.entity.ReviewEntity;
import com.hikiw.ecommerce.module.review.mapper.ReviewMapper;
import com.hikiw.ecommerce.module.review.repository.ReviewHelpfulRepository;
import com.hikiw.ecommerce.module.review.repository.ReviewReplyRepository;
import com.hikiw.ecommerce.module.review.repository.ReviewRepository;
import com.hikiw.ecommerce.module.user.entity.UserEntity;
import com.hikiw.ecommerce.module.user.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional(readOnly = true)
public class ReviewService {
    ReviewRepository reviewRepository;
    ReviewMapper reviewMapper;
    ReviewHelpfulRepository reviewHelpfulRepository;
    ReviewReplyRepository reviewReplyRepository;
    ObjectMapper objectMapper;
    OrderItemRepository orderItemRepository;
    UserRepository userRepository;


    // create review
    @Transactional
    public ReviewResponse createReview(ReviewCreationRequest request, Long currentUserId) {

        OrderItemEntity orderItem = orderItemRepository.findById(request.getOrderItemId())
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_ITEM_NOT_EXISTED));

        // Chỉ review được khi order đã DELIVERED
        if (orderItem.getOrder().getOrderStatus() != OrderStatus.DELIVERED) {
            throw new AppException(ErrorCode.REVIEW_ORDER_NOT_DELIVERED);
        }

        // Kiểm tra user có phải người mua không bằng ID chuẩn
        if (!orderItem.getOrder().getUser().getId().equals(currentUserId)) {
            throw new AppException(ErrorCode.REVIEW_NOT_AUTHORIZED);
        }

        // Kiểm tra đã review chưa
        if (reviewRepository.existsByOrderItem_OrderItemId(request.getOrderItemId())) {
            throw new AppException(ErrorCode.REVIEW_ALREADY_EXISTED);
        }

        UserEntity user = userRepository.findById(currentUserId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        ProductEntity product = orderItem.getProductVariant().getProduct();

        //  Convert imageUrls → JSON string
        String imagesJson = toJsonString(request.getImageUrls());
        ReviewEntity review = ReviewEntity.builder()
                .orderItem(orderItem)
                .user(user)
                .product(product)
                .rating(request.getRating())
                .comment(request.getComment())
                .images(imagesJson)
                .isAnonymous(request.getIsAnonymous() != null && request.getIsAnonymous())
                .build();

        ReviewEntity saved = reviewRepository.save(review);
        return buildReviewResponse(saved);
    }



    // Private helper method

    private ReviewResponse buildReviewResponse(ReviewEntity review) {
        ReviewResponse response = reviewMapper.toResponse(review);

        // Parse JSON images → List<String>
        response.setImageUrls(fromJsonString(review.getImages()));

        // Ẩn thông tin user nếu isAnonymous
        if (Boolean.TRUE.equals(review.getIsAnonymous())) {
            response.setUserId(null);
            response.setUsername("Anonymous");
        }

        // Set reply nếu có
        if (review.getReply() != null) {
            response.setReply(reviewMapper.toReplyResponse(review.getReply()));
        }

        return response;
    }
    // Chuyển List<String> thành JSON để lưu vào DB
    private String toJsonString(List<String> list) {
        if (list == null || list.isEmpty()) return null;
        try {
            return objectMapper.writeValueAsString(list);
        } catch (JsonProcessingException e) {
            return null;
        }
    }
    // Chuyển JSON từ DB thành List<String>
    private List<String> fromJsonString(String json) {
        if (json == null || json.isBlank()) return new ArrayList<>();
        try {
            return objectMapper.readValue(json, new TypeReference<List<String>>() {});
        } catch (JsonProcessingException e) {
            return new ArrayList<>();
        }
    }

}
