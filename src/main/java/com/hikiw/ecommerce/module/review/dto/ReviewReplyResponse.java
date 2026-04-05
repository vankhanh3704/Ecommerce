package com.hikiw.ecommerce.module.review.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReviewReplyResponse {
    Long replyId;
    Long userId;
    String username;
    String replyText;
    LocalDateTime createdDate;
    LocalDateTime updatedDate;
}