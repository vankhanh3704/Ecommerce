package com.hikiw.ecommerce.module.payment.service;


import com.hikiw.ecommerce.Enum.ErrorCode;
import com.hikiw.ecommerce.Enum.OrderStatus;
import com.hikiw.ecommerce.Enum.PaymentMethod;
import com.hikiw.ecommerce.Enum.PaymentStatus;
import com.hikiw.ecommerce.common.Exception.AppException;
import com.hikiw.ecommerce.module.order.entity.OrderEntity;
import com.hikiw.ecommerce.module.order.repository.OrderRepository;
import com.hikiw.ecommerce.module.payment.dto.PaymentCreationRequest;
import com.hikiw.ecommerce.module.payment.dto.PaymentResponse;
import com.hikiw.ecommerce.module.payment.entity.PaymentEntity;
import com.hikiw.ecommerce.module.payment.mapper.PaymentMapper;
import com.hikiw.ecommerce.module.payment.repository.PaymentRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PaymentService {
    PaymentRepository paymentRepository;
    PaymentMapper paymentMapper;
    OrderRepository orderRepository;


    // tạo payment
    @Transactional
    public PaymentResponse createPayment(PaymentCreationRequest request){
        List<OrderEntity> orders = orderRepository.findAllById(request.getOrderIds());

        if (orders.size() != request.getOrderIds().size()) {
            throw new AppException(ErrorCode.ORDER_NOT_EXISTED);
        }

        double totalPaymentAmount = 0.0;

        // Kiểm tra từng đơn hàng trước khi tạo payment
        for (OrderEntity order : orders) {
            if (order.getPayment() != null) {
                throw new AppException(ErrorCode.PAYMENT_ALREADY_EXISTED);
            }
            if (order.getOrderStatus() == OrderStatus.CANCELLED) {
                throw new AppException(ErrorCode.ORDER_CANNOT_PAY);
            }
            totalPaymentAmount += order.getTotalAmount();
        }

        // Tạo payment mới với trạng thái UNPAID
        String referenceCode = generateReferenceCode();


        PaymentEntity payment = PaymentEntity.builder()
                .paymentMethod(request.getPaymentMethod())
                .amount(totalPaymentAmount)
                .referenceCode(referenceCode)
                .paymentStatus(PaymentStatus.UNPAID)
                .build();

        PaymentEntity savedPayment = paymentRepository.save(payment);

        for (OrderEntity order : orders) {
            order.setPayment(savedPayment);
        }
        orderRepository.saveAll(orders);
        savedPayment.setOrders(orders);

        if (totalPaymentAmount <= 0) {
            return confirmZeroAmountPayment(savedPayment.getPaymentId());
        }

        if (request.getPaymentMethod() == PaymentMethod.COD) {
            return confirmCodPayment(savedPayment.getPaymentId());
        }

        PaymentResponse response = paymentMapper.toResponse(savedPayment);
        response.setPaymentUrl(generatePaymentUrl(savedPayment));
        return response;
    }


    // ========== COD — Xác nhận ngay ==========
    @Transactional
    public PaymentResponse confirmCodPayment(Long paymentId) {
        PaymentEntity payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new AppException(ErrorCode.PAYMENT_NOT_EXISTED));

        payment.setPaymentStatus(PaymentStatus.PAID);
        payment.setPaymentDate(LocalDateTime.now());
        payment.setGatewayMessage("COD - Payment on delivery");
        paymentRepository.save(payment);

        for (OrderEntity order : payment.getOrders()) {
            order.setPaymentStatus(PaymentStatus.PAID);
        }
        orderRepository.saveAll(payment.getOrders());

        return paymentMapper.toResponse(payment);
    }

    @Transactional
    public PaymentResponse confirmZeroAmountPayment(Long paymentId) {
        PaymentEntity payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new AppException(ErrorCode.PAYMENT_NOT_EXISTED));

        payment.setPaymentStatus(PaymentStatus.PAID);
        payment.setPaymentDate(LocalDateTime.now());
        payment.setGatewayMessage("0 VND - Auto confirmed");
        paymentRepository.save(payment);

        for (OrderEntity order : payment.getOrders()) {
            order.setPaymentStatus(PaymentStatus.PAID);
            order.setOrderStatus(OrderStatus.CONFIRMED);
        }
        orderRepository.saveAll(payment.getOrders());

        return paymentMapper.toResponse(payment);
    }

    // help method
    private String generateReferenceCode() {
        String datePart = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        return "PAY-" + datePart + "-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
    }

    private String generatePaymentUrl(PaymentEntity payment) {
        // TODO: Tích hợp thực tế với VNPay / MoMo / ZaloPay
        // Đây là placeholder
        return "https://payment-gateway.example.com/pay?ref=" + payment.getReferenceCode();
    }
}
