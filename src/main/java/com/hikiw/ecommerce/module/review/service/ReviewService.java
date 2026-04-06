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
import com.hikiw.ecommerce.module.review.dto.ReplyReviewRequest;
import com.hikiw.ecommerce.module.review.dto.ReviewCreationRequest;
import com.hikiw.ecommerce.module.review.dto.ReviewResponse;
import com.hikiw.ecommerce.module.review.dto.ReviewUpdateRequest;
import com.hikiw.ecommerce.module.review.entity.ReviewEntity;
import com.hikiw.ecommerce.module.review.entity.ReviewHelpfulEntity;
import com.hikiw.ecommerce.module.review.entity.ReviewReplyEntity;
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

    // update review
    @Transactional
    public ReviewResponse updateReview(Long reviewId, ReviewUpdateRequest request, Long currentUserId){
        ReviewEntity review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new AppException(ErrorCode.REVIEW_NOT_EXISTED));

        if(!review.getUser().getId().equals(currentUserId)){
            throw new AppException(ErrorCode.REVIEW_NOT_AUTHORIZED);
        }

        if(request.getRating() != null){
            review.setRating(request.getRating());
        }
        if(request.getComment() != null) {
            review.setComment(request.getComment());
        }
        if(request.getImageUrls() != null){
            review.setImages(toJsonString(request.getImageUrls()));
        }
        if(request.getIsAnonymous() != null){
            review.setIsAnonymous(request.getIsAnonymous());
        }
        return buildReviewResponse(reviewRepository.save(review));
    }
    // delete review
    @Transactional
    public void deleteReview(Long reviewId, Long currentUserId) {
        ReviewEntity review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new AppException(ErrorCode.REVIEW_NOT_EXISTED));

        if (!review.getUser().getId().equals(currentUserId)) {
            throw new AppException(ErrorCode.REVIEW_NOT_AUTHORIZED);
        }
        review.setIsActive(false);
        reviewRepository.save(review);
    }


    public List<ReviewResponse> getReviewsByUser(Long userId) {
        return reviewRepository
                .findByUser_IdAndIsActiveTrueOrderByCreatedDateDesc(userId)
                .stream()
                .map(this::buildReviewResponse)
                .toList();
    }


    public ReviewResponse getReviewById(Long reviewId) {
        ReviewEntity review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new AppException(ErrorCode.REVIEW_NOT_EXISTED));
        return buildReviewResponse(review);
    }


    // HELPFUL
    @Transactional
    public ReviewResponse markHelpful(Long reviewId, Long currentUserId) {
        ReviewEntity review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new AppException(ErrorCode.REVIEW_NOT_EXISTED));

        UserEntity user = userRepository.findById(currentUserId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        // Kiểm tra xem đã "Thả hữu ích" chưa
        var helpfulOpt = reviewHelpfulRepository.findByReview_ReviewIdAndUser_Id(reviewId, currentUserId);

        if (helpfulOpt.isPresent()) {
            // Đã bấm rồi -> Bấm lại là HỦY (Unlike)
            reviewHelpfulRepository.delete(helpfulOpt.get());
            review.setHelpfulCount(Math.max(0, review.getHelpfulCount() - 1));
        } else {
            // Chưa bấm -> THÊM MỚI (Like)
            ReviewHelpfulEntity newHelpful = ReviewHelpfulEntity.builder()
                    .review(review)
                    .user(user)
                    .build();
            reviewHelpfulRepository.save(newHelpful);
            review.setHelpfulCount(review.getHelpfulCount() + 1);
        }

        return buildReviewResponse(reviewRepository.save(review));
    }

    //  SHOP REPLY
    @Transactional
    public ReviewResponse replyToReview(Long reviewId, ReplyReviewRequest request, Long currentUserId) {
        ReviewEntity review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new AppException(ErrorCode.REVIEW_NOT_EXISTED));

        if (reviewReplyRepository.existsByReview_ReviewId(reviewId)) {
            throw new AppException(ErrorCode.REVIEW_REPLY_ALREADY_EXISTED);
        }

        //  KIỂM TRA CHỦ SHOP
        Long shopOwnerId = review.getProduct().getShop().getOwner().getId();
        if (!shopOwnerId.equals(currentUserId)) {
            throw new AppException(ErrorCode.UNAUTHORIZED_ACTION); // Báo lỗi không có quyền
        }

        UserEntity user = userRepository.findById(currentUserId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        ReviewReplyEntity reply = ReviewReplyEntity.builder()
                .review(review)
                .user(user)
                .replyText(request.getReplyText())
                .build();

        reviewReplyRepository.save(reply);
        review.setReply(reply);
        return buildReviewResponse(review);
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
