package com.hikiw.ecommerce.module.review.entity;

import com.hikiw.ecommerce.common.constant.BaseEntity;

import com.hikiw.ecommerce.module.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "review_reply")
public class ReviewReplyEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reply_id")
    Long replyId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id", nullable = false, unique = true)
    ReviewEntity review; // 1 review chỉ có 1 reply

    // Shop owner reply
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    UserEntity user;

    @Column(name = "reply_text", nullable = false, columnDefinition = "TEXT")
    String replyText;
}
