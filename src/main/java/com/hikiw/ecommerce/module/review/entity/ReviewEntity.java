package com.hikiw.ecommerce.module.review.entity;
import com.hikiw.ecommerce.common.constant.BaseAuditEntity;
import com.hikiw.ecommerce.common.constant.BaseEntity;
import com.hikiw.ecommerce.common.entity.BaseEntity;
import com.hikiw.ecommerce.module.order.entity.OrderItemEntity;
import com.hikiw.ecommerce.module.product.entity.ProductEntity;
import com.hikiw.ecommerce.module.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "review")
public class ReviewEntity extends BaseAuditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long reviewId;

    // Chỉ review được sau khi đã mua hàng (1 OrderItem = 1 Review)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_item_id", nullable = false, unique = true)
    OrderItemEntity orderItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    UserEntity user; // 1 user có thể có nhiều review

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    ProductEntity product; // 1 product có thể có nhiều review

    @Column(nullable = false)
    Integer rating; // 1 - 5 sao

    @Column(columnDefinition = "TEXT")
    String comment;

    // Lưu danh sách URL ảnh dạng JSON
    @Column(columnDefinition = "JSON")
    String images;

    @Builder.Default
    @Column(name = "is_anonymous")
    Boolean isAnonymous = false; // có thể ẩn danh khi review

    @Builder.Default
    @Column(name = "helpful_count")
    Integer helpfulCount = 0; // số lượt đánh giá hữu ích

    // Shop reply
    @OneToOne(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    ReviewReplyEntity reply;
}
