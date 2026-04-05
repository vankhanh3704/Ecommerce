package com.hikiw.ecommerce.module.payment.entity;


import com.hikiw.ecommerce.Enum.PaymentMethod;
import com.hikiw.ecommerce.Enum.PaymentStatus;
import com.hikiw.ecommerce.module.order.entity.OrderEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "payment")
public class PaymentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    Long paymentId;

    //1 Lần thanh toán chứa nhiều Đơn hàng
    @OneToMany(mappedBy = "payment", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @Builder.Default
    List<OrderEntity> orders = new ArrayList<>();


    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false)
    PaymentMethod paymentMethod;

    @Column(nullable = false)
    Double amount; // tổng số tiền đã thanh toán

    // Mã giao dịch từ cổng thanh toán (VNPay, MoMo...)
    @Column(name = "transaction_id", length = 100)
    String transactionId;

    // Mã tham chiếu nội bộ
    @Column(name = "reference_code", unique = true, length = 50)
    String referenceCode;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    @Column(name = "payment_status", nullable = false)
    PaymentStatus paymentStatus = PaymentStatus.UNPAID;

    @Column(name = "payment_date")
    LocalDateTime paymentDate;

    // Ghi chú từ cổng thanh toán
    @Column(name = "gateway_message", columnDefinition = "TEXT")
    String gatewayMessage;

    // Raw response từ cổng thanh toán (lưu để debug)
    @Column(name = "raw_response", columnDefinition = "TEXT")
    String rawResponse;


}
