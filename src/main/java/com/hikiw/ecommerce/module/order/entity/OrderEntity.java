package com.hikiw.ecommerce.module.order.entity;

import com.hikiw.ecommerce.Enum.OrderStatus;
import com.hikiw.ecommerce.Enum.PaymentMethod;
import com.hikiw.ecommerce.Enum.PaymentStatus;
import com.hikiw.ecommerce.common.constant.BaseEntity;
import com.hikiw.ecommerce.module.payment.entity.PaymentEntity;
import com.hikiw.ecommerce.module.shop.entity.ShopEntity;
import com.hikiw.ecommerce.module.user.entity.UserEntity;
import com.hikiw.ecommerce.module.voucher.entity.VoucherEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

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
    String orderCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id", nullable = false)
    ShopEntity shop;

    @Column(name = "receiver_name", length = 100)
    String receiverName;

    @Column(name = "receiver_phone", length = 20)
    String receiverPhone;

    @Column(name = "shipping_address", columnDefinition = "TEXT")
    String shippingAddress;

    // ========== VOUCHER & DISCOUNT INFO ==========
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "voucher_id")
    VoucherEntity voucher; // Voucher của Shop

    @Column(name = "platform_voucher_code")
    String platformVoucherCode; // Mã voucher của sàn (nếu có lưu)

    // ========== MONEY SPLIT (SNAPSHOT TÀI CHÍNH) ==========
    @Column(name = "subtotal", nullable = false)
    Double subtotal;

    @Builder.Default
    @Column(name = "original_shipping_fee")
    Double originalShippingFee = 0.0;

    @Builder.Default
    @Column(name = "shipping_discount")
    Double shippingDiscount = 0.0;

    @Builder.Default
    @Column(name = "shipping_fee")
    Double shippingFee = 0.0; // Phí ship thực thu: original - discount

    @Builder.Default
    @Column(name = "shop_discount_amount")
    Double shopDiscountAmount = 0.0; // Đổi tên từ discountAmount cho rõ nghĩa

    @Builder.Default
    @Column(name = "platform_discount_amount")
    Double platformDiscountAmount = 0.0;

    @Column(name = "total_amount", nullable = false)
    Double totalAmount; // subtotal + shippingFee - shopDiscount - platformDiscount

    // ========== STATUS ==========
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id") // Sẽ sinh ra cột payment_id trong bảng orders
    PaymentEntity payment;

    @Column(columnDefinition = "TEXT")
    String note;

    // ========== RELATIONSHIPS ==========
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    List<OrderItemEntity> orderItems = new ArrayList<>();

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    List<OrderStatusHistoryEntity> statusHistory = new ArrayList<>();
}