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


    // calculate discount
    // 3 th :
    // th1 : đối với trường hợp giảm giá theo % (PERCENTAGE)
    //Giảm 10% tối đa 50k. Đơn hàng 1 triệu (10% là 100k) thì bạn cũng chỉ được giảm 50k.
    public Double calculateDiscount(Double orderAmount){
        // số tiền khách mua nhỏ hơn mức chi tiêu tối thiểu để áp dụng mã
        if(orderAmount < minSpend){
            return 0.0;
        }
        return switch (discountType){
            case PERCENTAGE -> {
                double discount = orderAmount * (discountValue / 100);
                // maxDiscount ở đây là số tiền giảm giá tối đa
                // vd : Bạn được giảm 100k(discount), nhưng mã chỉ cho giảm tối đa 50k(maxDiscount) -> Lấy 50k.
                yield (maxDiscount != null) ? Math.min(discount, maxDiscount) : discount;
            }
            case FIXED_AMOUNT -> Math.min(discountValue, orderAmount);
            case FREE_SHIPPING -> 0.0;  // xử lý riêng ở Order
        };

    }
}
