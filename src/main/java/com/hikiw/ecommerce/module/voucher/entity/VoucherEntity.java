package com.hikiw.ecommerce.module.voucher.entity;


import com.hikiw.ecommerce.Enum.DiscountType;
import com.hikiw.ecommerce.common.constant.BaseAuditEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "voucher")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VoucherEntity extends BaseAuditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long voucherId;

    @Column(nullable = false, unique = true, length = 50)
    String code;

    @Enumerated(EnumType.STRING) // map một enum trong Java sang database.
    @Column(name = "discount_type")
    DiscountType discountType;

    @Column(nullable = false, name = "discount_value")
    Double discountValue;

    // đơn tối thiểu để dùng voucher
    @Column(name = "min_spend")
    Double minSpend = 0.0;

    @Column(name = "max_discount")
    Double maxDiscount;  // giới hạn số tiền được giảm tối đa (áp dụng cho voucher %)

    @Column(name = "usage_limit")
    Integer usageLimit;     // tổng số lần voucher có thể được sử dụng , nếu null thì không giới hạn

    @Builder.Default
    @Column(name = "used_count")
    Integer usedCount = 0;

    @OneToMany(mappedBy = "voucher", cascade = CascadeType.ALL, orphanRemoval = true)
    List<VoucherUsageEntity> usages = new ArrayList<>();

    @Column(name = "start_date", nullable = false)
    LocalDateTime startDate;

    @Column(name = "end_date", nullable = false)
    LocalDateTime endDate;

    @Builder.Default
    @Column(name = "is_active")
    Boolean isActive = true;

    // help method
    public boolean isValid(){
        LocalDateTime now = LocalDateTime.now();
        return Boolean.TRUE.equals(isActive)
                && now.isAfter(startDate)
                && now.isBefore(endDate)
                && (usageLimit == null || usageLimit > usedCount);
    }
}
