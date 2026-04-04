package com.hikiw.ecommerce.module.order.service;


import com.hikiw.ecommerce.Enum.DiscountType;
import com.hikiw.ecommerce.Enum.ErrorCode;
import com.hikiw.ecommerce.Enum.OrderStatus;
import com.hikiw.ecommerce.Enum.PaymentStatus;
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
import com.hikiw.ecommerce.module.shop.entity.ShopEntity;
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
import java.util.Map;
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
    // ========== PREVIEW CHECKOUT ==========
    public CheckoutPreviewResponse previewCheckout(CheckoutPreviewRequest request, Long userId) {

        List<CartItemEntity> selectedItems = getAndValidateCartItems(
                request.getSelectedCartItemIds(), userId);

        // Nhóm items theo shop
        Map<Long, List<CartItemEntity>> itemsByShop = selectedItems.stream()
                .collect(Collectors.groupingBy(
                        item -> item.getProductVariant().getProduct().getShop().getShopId()
                ));

        List<ShopOrderPreview> shopPreviews = new ArrayList<>();
        List<String> warnings = new ArrayList<>();

        double totalSubtotal = 0, totalShipping = 0, totalDiscount = 0;

        for (Map.Entry<Long, List<CartItemEntity>> entry : itemsByShop.entrySet()) {
            Long shopId = entry.getKey();
            List<CartItemEntity> shopItems = entry.getValue();
            ShopEntity shop = shopItems.get(0).getProductVariant().getProduct().getShop();

            double subtotal = calculateSubtotal(shopItems);
            double shippingFee = calculateShippingFee(subtotal);
            double discountAmount = 0.0;
            String voucherCode = null;
            String voucherDescription = null;
            boolean isFreeShipping = false;

            // Áp dụng voucher của shop này nếu có
            if (request.getShopVoucherCodes() != null
                    && request.getShopVoucherCodes().containsKey(shopId)) {

                voucherCode = request.getShopVoucherCodes().get(shopId);
                VoucherEntity voucher = voucherRepository
                        .findByCodeAndShop_ShopId(voucherCode.toUpperCase(), shopId)
                        .orElse(null);

                if (voucher != null && voucher.isValid()) {
                    if (voucher.getDiscountType() == DiscountType.FREE_SHIPPING) {
                        shippingFee = 0.0;
                        isFreeShipping = true;
                        voucherDescription = "Miễn phí vận chuyển";
                    } else {
                        discountAmount = voucher.calculateDiscount(subtotal);
                        voucherDescription = buildVoucherDescription(voucher);
                    }
                } else {
                    warnings.add("Voucher '" + voucherCode + "' của shop "
                            + shop.getShopName() + " không hợp lệ");
                    voucherCode = null;
                }
            }

            // Kiểm tra stock
            for (CartItemEntity item : shopItems) {
                if (item.getProductVariant().getStock() < item.getQuantity()) {
                    warnings.add("Sản phẩm '"
                            + item.getProductVariant().getProduct().getProductName()
                            + "' chỉ còn " + item.getProductVariant().getStock() + " sản phẩm");
                }
            }

            double shopTotal = subtotal + shippingFee - discountAmount;

            shopPreviews.add(ShopOrderPreview.builder()
                    .shopId(shopId)
                    .shopName(shop.getShopName())
                    .items(buildCartItemResponses(shopItems))
                    .subtotal(subtotal)
                    .shippingFee(shippingFee)
                    .discountAmount(discountAmount)
                    .totalAmount(shopTotal)
                    .voucherCode(voucherCode)
                    .voucherDescription(voucherDescription)
                    .isFreeShipping(isFreeShipping)
                    .build());

            totalSubtotal += subtotal;
            totalShipping += shippingFee;
            totalDiscount += discountAmount;
        }

        return CheckoutPreviewResponse.builder()
                .shopOrders(shopPreviews)
                .totalSubtotal(totalSubtotal)
                .totalShippingFee(totalShipping)
                .totalDiscount(totalDiscount)
                .grandTotal(totalSubtotal + totalShipping - totalDiscount)
                .warnings(warnings)
                .build();
    }

    @Transactional
    public CheckoutResponse createOrder(OrderCreationRequest request, Long userId) {
        List<CartItemEntity> selectedItems = getAndValidateCartItems(
                request.getSelectedCartItemIds(), userId);

        validateStock(selectedItems);

        // 1. FIX BUG: Nhóm items theo SHOP ID, KHÔNG PHẢI Product ID
        Map<Long, List<CartItemEntity>> itemsByShop = selectedItems.stream()
                .collect(Collectors.groupingBy(
                        item -> item.getProductVariant().getProduct().getShop().getShopId()));

        List<OrderResponse> createdOrderResponses = new ArrayList<>();

        // 2. TỐI ƯU: Lấy user 1 lần duy nhất bên ngoài vòng lặp
        UserEntity currentUser = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        // --- CÁC BIẾN TÍNH TỔNG CHO PAYMENT SUMMARY ---
        double summaryTotalItemsSubtotal = 0.0;
        double summaryTotalOriginalShippingFee = 0.0;
        double summaryTotalShippingDiscount = 0.0;
        double summaryTotalShopDiscount = 0.0;

        for (Map.Entry<Long, List<CartItemEntity>> entry : itemsByShop.entrySet()) {
            Long shopId = entry.getKey();
            List<CartItemEntity> shopItems = entry.getValue();

            ShopEntity shop = shopItems.getFirst().getProductVariant().getProduct().getShop();

            // Tính tiền hàng và phí ship gốc
            double subtotal = calculateSubtotal(shopItems);
            double originalShippingFee = calculateShippingFee(subtotal);

            double shippingFee = originalShippingFee;
            double shopDiscountAmount = 0.0;
            double shippingDiscountAmount = 0.0;
            VoucherEntity voucher = null;

            // Xử lý Voucher của Shop
            if (request.getShopVoucherCodes() != null && request.getShopVoucherCodes().containsKey(shopId)) {
                String code = request.getShopVoucherCodes().get(shopId);
                voucher = voucherRepository.findByCodeAndShop_ShopId(code.toUpperCase(), shopId)
                        .orElseThrow(() -> new AppException(ErrorCode.VOUCHER_NOT_EXISTED));

                if (!voucher.isValid()) throw new AppException(ErrorCode.VOUCHER_EXPIRED);

                if (voucher.getDiscountType() == DiscountType.FREE_SHIPPING) {
                    shippingDiscountAmount = originalShippingFee; // Miễn phí ship
                    shippingFee = 0.0;
                } else {
                    shopDiscountAmount = voucher.calculateDiscount(subtotal);
                }
            }

            // Tính tổng tiền cho sub-order này (tránh số âm)
            double totalAmount = Math.max(0.0, subtotal + shippingFee - shopDiscountAmount);

            // Cộng dồn vào Summary toàn đơn
            summaryTotalItemsSubtotal += subtotal;
            summaryTotalOriginalShippingFee += originalShippingFee;
            summaryTotalShippingDiscount += shippingDiscountAmount;
            summaryTotalShopDiscount += shopDiscountAmount;

            // Tạo OrderEntity cho Shop này với các field mới
            OrderEntity order = OrderEntity.builder()
                    .user(currentUser)
                    .shop(shop)
                    .orderCode(generateOrderCode())
                    .receiverName(request.getReceiverName())
                    .receiverPhone(request.getReceiverPhone())
                    .shippingAddress(request.getShippingAddress())
                    .voucher(voucher)

                    // --- THÊM CÁC BIẾN TÀI CHÍNH MỚI VÀO ĐÂY ---
                    .subtotal(subtotal)
                    .originalShippingFee(originalShippingFee)   // Phí ship gốc
                    .shippingDiscount(shippingDiscountAmount)   // Tiền giảm ship
                    .shippingFee(shippingFee)                   // Phí ship cuối
                    .shopDiscountAmount(shopDiscountAmount)     // Tiền voucher shop giảm
                    .platformVoucherCode(request.getPlatformVoucherCode()) // Mã sàn
                    .platformDiscountAmount(0.0)                // Tiền sàn giảm (Tạm để 0)
                    .totalAmount(totalAmount)
                    // ------------------------------------------

                    .paymentMethod(request.getPaymentMethod())
                    .orderStatus(OrderStatus.PENDING)
                    .paymentStatus(PaymentStatus.UNPAID)
                    .note(request.getNote())
                    .build();

            OrderEntity savedOrder = orderRepository.save(order);

            // TỐI ƯU: Lưu OrderItems và Variant bằng Batch (saveAll)
            List<OrderItemEntity> orderItemsToSave = new ArrayList<>();
            List<ProductVariantEntity> variantsToUpdate = new ArrayList<>();

            for (CartItemEntity cartItem : shopItems) {
                ProductVariantEntity variant = cartItem.getProductVariant();

                orderItemsToSave.add(OrderItemEntity.builder()
                        .order(savedOrder)
                        .productVariant(variant)
                        .productName(variant.getProduct().getProductName())
                        .sku(variant.getSku())
                        .variantInfo(variant.getVariantInfoString())
                        .imageUrl(variant.getImageUrl())
                        .pricePerUnit(variant.getPrice())
                        .quantity(cartItem.getQuantity())
                        .totalPrice(variant.getPrice() * cartItem.getQuantity())
                        .build());

                // Giảm stock, tăng sold count
                variant.setStock(variant.getStock() - cartItem.getQuantity());
                variant.setSoldCount(variant.getSoldCount() + cartItem.getQuantity());
                variantsToUpdate.add(variant);
            }

            // Thực thi SQL Insert/Update theo lô
            orderItemRepository.saveAll(orderItemsToSave);
            productVariantRepository.saveAll(variantsToUpdate);

            saveStatusHistory(savedOrder, OrderStatus.PENDING, "Order created");

            if (voucher != null) {
                voucherService.confirmVoucherUsage(voucher.getVoucherId(), userId, savedOrder.getOrderId());
            }

            // 1. Gắn List items vào Entity (để Mapper tự lấy)
            savedOrder.setOrderItems(orderItemsToSave);

            // 2. Chuyển sang DTO (MapStruct sẽ tự động map tất cả các field tiền bạc)
            OrderResponse orderResponse = orderMapper.toResponse(savedOrder);

            // 3. Fake History vào DTO (vì lúc save history nó chưa nạp lại vào RAM)
            OrderStatusHistoryResponse historyDto = OrderStatusHistoryResponse.builder()
                    .status(OrderStatus.PENDING)
                    .note("Order created")
                    .createdDate(LocalDateTime.now())
                    .build();
            orderResponse.setStatusHistory(List.of(historyDto));

            // 4. Đẩy vào mảng để chờ trả về
            createdOrderResponses.add(orderResponse);
        }

        // Xóa các item đã mua khỏi giỏ hàng
        cartItemRepository.deleteAllById(request.getSelectedCartItemIds());

        // --- XỬ LÝ VOUCHER SÀN (NẾU CÓ) ---
        double summaryTotalPlatformDiscount = 0.0;
        if (request.getPlatformVoucherCode() != null && !request.getPlatformVoucherCode().isBlank()) {
            // TODO: Viết hàm truy vấn voucher Sàn (không gắn với shop_id) và tính toán discount
            // Ví dụ: summaryTotalPlatformDiscount = platformVoucher.calculateDiscount(summaryTotalItemsSubtotal);
        }

        // Tính TỔNG THANH TOÁN cuối cùng của cả phiên
        double finalShippingFee = Math.max(0.0, summaryTotalOriginalShippingFee - summaryTotalShippingDiscount);
        double grandTotal = Math.max(0.0, summaryTotalItemsSubtotal + finalShippingFee - summaryTotalShopDiscount - summaryTotalPlatformDiscount);

        PaymentSummaryResponse paymentSummary = PaymentSummaryResponse.builder()
                .totalItemsSubtotal(summaryTotalItemsSubtotal)
                .totalOriginalShippingFee(summaryTotalOriginalShippingFee)
                .totalShippingDiscount(summaryTotalShippingDiscount)
                .finalShippingFee(finalShippingFee)
                .totalShopDiscount(summaryTotalShopDiscount)
                .totalPlatformDiscount(summaryTotalPlatformDiscount)
                .grandTotal(grandTotal)
                .paymentMethod(request.getPaymentMethod())
                .build();

        return CheckoutResponse.builder()
                .checkoutSessionId("CHK-" + System.currentTimeMillis())
                .orders(createdOrderResponses)
                .paymentSummary(paymentSummary)
                .build();
    }



    // HELPER METHODS
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
