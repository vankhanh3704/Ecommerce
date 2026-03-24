package com.hikiw.ecommerce.module.voucher.entity;


import com.hikiw.ecommerce.module.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Table(name = "voucher_usage")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VoucherUsageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "usage_id")
    Long usageId;

    @ManyToOne(fetch = FetchType.LAZY)
            @JoinColumn(name = "voucher_id", nullable = false)
    VoucherEntity voucher;


    @ManyToOne(fetch = FetchType.LAZY)
            @JoinColumn(name = "user_id", nullable = false)
    UserEntity user;

    @Column(name = "order_id")
    Long orderId;

    @Column(name = "used_date", updatable = false)
    LocalDateTime usedDate;

    @PrePersist
    void prePersist() {
        this.usedDate = LocalDateTime.now();
    }
}
