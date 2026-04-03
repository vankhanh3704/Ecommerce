package com.hikiw.ecommerce.module.order.service;


import com.hikiw.ecommerce.Enum.DiscountType;
import com.hikiw.ecommerce.Enum.ErrorCode;
import com.hikiw.ecommerce.Enum.OrderStatus;
import com.hikiw.ecommerce.common.Exception.AppException;
import com.hikiw.ecommerce.module.cart.dto.CartItemResponse;
import com.hikiw.ecommerce.module.cart.dto.VariantInfo;
import com.hikiw.ecommerce.module.cart.entity.CartEntity;
import com.hikiw.ecommerce.module.cart.entity.CartItemEntity;
import com.hikiw.ecommerce.module.cart.repository.CartItemRepository;
import com.hikiw.ecommerce.module.cart.service.CartService;
import com.hikiw.ecommerce.module.order.dto.*;
import com.hikiw.ecommerce.module.order.entity.OrderEntity;
import com.hikiw.ecommerce.module.order.entity.OrderItemEntity;
import com.hikiw.ecommerce.module.order.entity.OrderStatusHistoryEntity;
import com.hikiw.ecommerce.module.order.mapper.OrderMapper;
import com.hikiw.ecommerce.module.order.repository.OrderItemRepository;
import com.hikiw.ecommerce.module.order.repository.OrderRepository;
import com.hikiw.ecommerce.module.order.repository.OrderStatusHistoryRepository;
import com.hikiw.ecommerce.module.product_variant.entity.ProductVariantEntity;
import com.hikiw.ecommerce.module.product_variant.repository.ProductVariantRepository;
import com.hikiw.ecommerce.module.user.entity.UserEntity;
import com.hikiw.ecommerce.module.user.repository.UserRepository;
import com.hikiw.ecommerce.module.voucher.entity.VoucherEntity;
import com.hikiw.ecommerce.module.voucher.repository.VoucherRepository;
import com.hikiw.ecommerce.module.voucher.service.VoucherService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional(readOnly = true)
public class OrderService {

    OrderRepository orderRepository;
    OrderMapper orderMapper;
    OrderItemRepository orderItemRepository;
    OrderStatusHistoryRepository orderStatusHistoryRepository;
    CartService cartService;
    UserRepository userRepository;
    VoucherRepository voucherRepository;
    ProductVariantRepository productVariantRepository;
    VoucherService voucherService;
    private final CartItemRepository cartItemRepository;

    // ========== PREVIEW CHECKOUT ==========
    // Gọi khi user bấm "Mua hàng" từ giỏ hàng
    // Trả về thông tin để hiển thị trang checkout
    public CheckoutPreviewResponse previewCheckout(CheckoutPreviewRequest request, Long userId) {

        // Lấy các cartItem được chọn và validate ownership
        List<CartItemEntity> selectedItems = getAndValidateCartItems(request.getSelectedCartItemIds(), userId);


        validateStock(selectedItems);

        Double subtotal = calculateSubtotal(selectedItems);
        Double shippingFee = calculateShippingFee(subtotal);
        Double discountAmount = 0.0;
        String voucherDescription = null;
        boolean isFreeShipping = false;
        List<String> warnings = new ArrayList<>();


        // Xử lý voucher nếu có
        if (request.getVoucherCode() != null && !request.getVoucherCode().isBlank()) {
            VoucherEntity voucher = voucherRepository
                    .findByCode(request.getVoucherCode().toUpperCase())
                    .orElseThrow(() -> new AppException(ErrorCode.VOUCHER_NOT_EXISTED));

            if (voucher.isValid()) {
                if (voucher.getDiscountType() == DiscountType.FREE_SHIPPING) {
                    shippingFee = 0.0;
                    isFreeShipping = true;
                    voucherDescription = "Miễn phí vận chuyển";
                } else {
                    discountAmount = voucher.calculateDiscount(subtotal);
                    voucherDescription = buildVoucherDescription(voucher);
                }
            } else {
                warnings.add("Voucher '" + request.getVoucherCode() + "' không còn hiệu lực");
            }
        }

        // Kiểm tra stock và thêm warning
        for (CartItemEntity item : selectedItems) {
            ProductVariantEntity variant = item.getProductVariant();
            if (variant.getStock() < item.getQuantity()) {
                warnings.add("Sản phẩm '" + variant.getProduct().getProductName()
                        + "' chỉ còn " + variant.getStock() + " sản phẩm");
            }
        }

        Double totalAmount = subtotal + shippingFee - discountAmount;

        return CheckoutPreviewResponse.builder()
                .selectedItems(buildCartItemResponses(selectedItems))
                .subtotal(subtotal)
                .shippingFee(shippingFee)
                .discountAmount(discountAmount)
                .totalAmount(totalAmount)
                .voucherCode(request.getVoucherCode())
                .voucherDescription(voucherDescription)
                .isFreeShipping(isFreeShipping)
                .warnings(warnings)
                .build();
    }

    // Xây dựng mô tả voucher để hiển thị trên UI
    private String buildVoucherDescription(VoucherEntity voucher) {
        return switch (voucher.getDiscountType()) {
            case PERCENTAGE -> "Giảm " + voucher.getDiscountValue().intValue() + "%"
                    + (voucher.getMaxDiscount() != null
                    ? " (tối đa " + voucher.getMaxDiscount() + "đ)" : "");
            case FIXED_AMOUNT -> "Giảm " + voucher.getDiscountValue() + "đ";
            case FREE_SHIPPING -> "Miễn phí vận chuyển";
        };
    }

    // Lấy các cartItem được chọn và validate ownership
    private List<CartItemEntity> getAndValidateCartItems(List<Long> selectedCartItemIds, Long userId) {
        List<CartItemEntity> items = cartItemRepository.findAllById(selectedCartItemIds);

        if(items.size() != selectedCartItemIds.size()){
            throw new AppException(ErrorCode.CART_ITEM_NOT_EXISTED);
        }

        // Validate ownership
        boolean allBelongToUser = items.stream()
                .allMatch(item -> item.getCart().getUser().getId().equals(userId));

        if (!allBelongToUser) {
            throw new AppException(ErrorCode.CART_ITEM_NOT_EXISTED);
        }
        return items;
    }

    // Validate tồn kho trước khi preview để tránh trường hợp đến bước đặt hàng mới phát hiện thiếu hàng
    private void validateStock(List<CartItemEntity> items) {
        for (CartItemEntity item : items) {
            ProductVariantEntity variant = item.getProductVariant();
            if (variant.getStock() < item.getQuantity()) {
                throw new AppException(ErrorCode.INSUFFICIENT_STOCK);
            }
        }
    }

    private Double calculateShippingFee(Double subtotal) {
        return (subtotal != null && subtotal >= 500000) ? 0.0 : 30000.0;
    }

    private void saveStatusHistory(OrderEntity savedOrder, OrderStatus status, String message) {
        orderStatusHistoryRepository.save(OrderStatusHistoryEntity.builder()
                        .order(savedOrder)
                        .status(status)
                        .note(message)
                .build());
    }
    private List<CartItemResponse> buildCartItemResponses(List<CartItemEntity> items) {
        return items.stream().map(item -> CartItemResponse.builder()
                .cartItemId(item.getCartItemId())
                .productVariantId(item.getProductVariant().getProductVariantId())
                .productId(item.getProductVariant().getProduct().getProductId())
                .productName(item.getProductVariant().getProduct().getProductName())
                .sku(item.getProductVariant().getSku())
                .productImageUrl(item.getProductVariant().getImageUrl())
                .variantValues(item.getProductVariant().getVariantMappings().stream()
                                .map(mapping -> VariantInfo.builder()
                                        .variantName(mapping.getVariantValue().getVariant().getVariantName())
                                        .valueName(mapping.getVariantValue().getValueName())
                                        .imageUrl(mapping.getVariantValue().getImageUrl())
                                        .build())
                                .collect(Collectors.toList())
                        )
                .discountPercentage(item.getProductVariant().getDiscountPercentage())
                .price(item.getProductVariant().getPrice())
                .oldPrice(item.getProductVariant().getOldPrice())
                .quantity(item.getQuantity())
                .subtotal(item.getProductVariant().getPrice() * item.getQuantity())
                .stockAvailable(item.getProductVariant().getStock())
                .inStock(item.getProductVariant().getInStock())
                .build()).toList();
    }
    private OrderResponse buildOrderResponse(Long orderId) {
        OrderEntity order = orderRepository.findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_EXISTED));

        OrderResponse response = orderMapper.toResponse(order);

        // set order items
        List<OrderItemResponse> items = orderItemRepository.findByOrder_OrderIdOrderByCreatedDateAsc(orderId)
                .stream()
                .map(item -> OrderItemResponse.builder()
                        .orderItemId(item.getOrderItemId())
                        .productVariantId(item.getProductVariant().getProductVariantId())
                        .productName(item.getProductName())
                        .sku(item.getSku())
                        .variantInfo(item.getVariantInfo())
                        .imageUrl(item.getImageUrl())
                        .pricePerUnit(item.getPricePerUnit())
                        .quantity(item.getQuantity())
                        .totalPrice(item.getTotalPrice())
                        .build())
                .toList();

        // Set status history
        List<OrderStatusHistoryResponse> history = orderStatusHistoryRepository
                .findByOrder_OrderIdOrderByCreatedDateAsc(orderId)
                .stream()
                .map(h -> OrderStatusHistoryResponse.builder()
                        .historyId(h.getHistoryId())
                        .status(h.getStatus())
                        .note(h.getNote())
                        .createdDate(h.getCreatedDate())
                        .build())
                .toList();

        response.setStatusHistory(history);
        response.setOrderItems(items);
        return response;
    }

    private String generateOrderCode() {
        String datePart = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        long count = orderRepository.count() + 1;
        return String.format("ORD-%s-%04d", datePart, count);
    }

    private Double calculateSubtotal(List<CartItemEntity> items) {
        double subtotal = 0.0;
        for (CartItemEntity item : items) {
            Double price = item.getProductVariant().getPrice();
            int quantity = item.getQuantity();
            subtotal += price * quantity;
        }
        return subtotal;
    }

}
