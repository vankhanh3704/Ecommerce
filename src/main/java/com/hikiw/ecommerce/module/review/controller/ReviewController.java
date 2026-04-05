package com.hikiw.ecommerce.module.review.controller;


import com.hikiw.ecommerce.common.Response.ApiResponse;
import com.hikiw.ecommerce.configuration.SecurityUtil;
import com.hikiw.ecommerce.module.review.dto.ReviewCreationRequest;
import com.hikiw.ecommerce.module.review.dto.ReviewResponse;
import com.hikiw.ecommerce.module.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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


}
