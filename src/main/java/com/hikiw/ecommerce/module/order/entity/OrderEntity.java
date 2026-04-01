package com.hikiw.ecommerce.module.order.entity;


import com.hikiw.ecommerce.Enum.OrderStatus;
import com.hikiw.ecommerce.Enum.PaymentMethod;
import com.hikiw.ecommerce.Enum.PaymentStatus;
import com.hikiw.ecommerce.common.constant.BaseEntity;
import com.hikiw.ecommerce.module.user.entity.UserEntity;
import com.hikiw.ecommerce.module.voucher.entity.VoucherEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    Long orderId;

    @Column(name = "order_name", nullable = false, unique = true, length = 30)
    String orderCode; // vd : ORD-20260325-0001

    // user : Nhiều đơn hàng có thể thuộc về một người dùng.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    UserEntity user;


    // dia chi giao hang (snapshot tại thời điểm đặt)
    @Column(name = "receiver_name", length = 100)
    String receiverName;

    @Column(name = "receiver_phone", length = 20)
    String receiverPhone;

    @Column(name = "shipping_address", columnDefinition = "TEXT")
    String shippingAddress;

    // voucher
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "voucher_id")
    VoucherEntity voucher;

    // money
    @Column(name = "subtotal", nullable = false)
    Double subtotal;        // Tổng tiền hàng

    @Builder.Default
    @Column(name = "shipping_fee")
    Double shippingFee = 0.0;

    @Builder.Default
    @Column(name = "discount_amount")
    Double discountAmount = 0.0;

    @Column(name = "total_amount", nullable = false)
    Double totalAmount;     // subtotal + shippingFee - discountAmount

    // status (trang thai)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    @Column(name = "order_status", nullable = false)
    OrderStatus orderStatus = OrderStatus.PENDING;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    @Column(name = "payment_status", nullable = false)
    PaymentStatus paymentStatus = PaymentStatus.UNPAID;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false)
    PaymentMethod paymentMethod;

    @Column(columnDefinition = "TEXT")
    String note;

    // order items
}
