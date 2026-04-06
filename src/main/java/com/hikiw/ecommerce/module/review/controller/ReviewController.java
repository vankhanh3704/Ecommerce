package com.hikiw.ecommerce.module.review.controller;


import com.hikiw.ecommerce.common.Response.ApiResponse;
import com.hikiw.ecommerce.configuration.SecurityUtil;
import com.hikiw.ecommerce.module.review.dto.ReplyReviewRequest;
import com.hikiw.ecommerce.module.review.dto.ReviewCreationRequest;
import com.hikiw.ecommerce.module.review.dto.ReviewResponse;
import com.hikiw.ecommerce.module.review.dto.ReviewUpdateRequest;
import com.hikiw.ecommerce.module.review.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ReviewController {
    ReviewService reviewService;
    SecurityUtil securityUtil;

    @PostMapping
    public ApiResponse<ReviewResponse> createReview(@RequestBody ReviewCreationRequest request) {
        return ApiResponse.<ReviewResponse>builder()
                .result(reviewService.createReview(request, securityUtil.getCurrentUserId()))
                .message("Review created successfully")
                .build();
    }


    @PutMapping("/{reviewId}")
    public ApiResponse<ReviewResponse> updateReview(@PathVariable Long reviewId, @RequestBody ReviewUpdateRequest request) {
        return ApiResponse.<ReviewResponse>builder()
                .result(reviewService.updateReview(reviewId, request, securityUtil.getCurrentUserId()))
                .message("Review updated successfully")
                .build();
    }
    @DeleteMapping("/{reviewId}")
    public ApiResponse<String> deleteReview(@PathVariable Long reviewId) { // Đã bỏ @RequestParam userId
        reviewService.deleteReview(reviewId, securityUtil.getCurrentUserId()); // Lấy userId từ SecurityUtil
        return ApiResponse.<String>builder()
                .result("Review deleted successfully")
                .build();
    }

    @GetMapping("/user/{userId}")
    public ApiResponse<List<ReviewResponse>> getReviewsByUser(
            @PathVariable Long userId) {
        return ApiResponse.<List<ReviewResponse>>builder()
                .result(reviewService.getReviewsByUser(userId))
                .build();
    }

    @GetMapping("/{reviewId}")
    public ApiResponse<ReviewResponse> getReviewById(@PathVariable Long reviewId) {
        return ApiResponse.<ReviewResponse>builder()
                .result(reviewService.getReviewById(reviewId))
                .build();
    }

    @PostMapping("/{reviewId}/reply")
    public ApiResponse<ReviewResponse> replyToReview(
            @PathVariable Long reviewId,
            @Valid @RequestBody ReplyReviewRequest request) {
        return ApiResponse.<ReviewResponse>builder()
                .result(reviewService.replyToReview(reviewId, request, securityUtil.getCurrentUserId()))
                .message("Reply added successfully")
                .build();
    }

    @PutMapping("/{reviewId}/helpful")
    public ApiResponse<ReviewResponse> markHelpful(@PathVariable Long reviewId) {
        return ApiResponse.<ReviewResponse>builder()
                .result(reviewService.markHelpful(reviewId, securityUtil.getCurrentUserId()))
                .message("Marked as helpful")
                .build();
    }
}
