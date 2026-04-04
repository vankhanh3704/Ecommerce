package com.hikiw.ecommerce.module.cart.service;


import com.hikiw.ecommerce.Enum.ErrorCode;
import com.hikiw.ecommerce.common.Exception.AppException;
import com.hikiw.ecommerce.module.cart.dto.*;
import com.hikiw.ecommerce.module.cart.entity.CartEntity;
import com.hikiw.ecommerce.module.cart.entity.CartItemEntity;
import com.hikiw.ecommerce.module.cart.mapper.CartMapper;
import com.hikiw.ecommerce.module.cart.repository.CartItemRepository;
import com.hikiw.ecommerce.module.cart.repository.CartRepository;
import com.hikiw.ecommerce.module.product_variant.entity.ProductVariantEntity;
import com.hikiw.ecommerce.module.product_variant.repository.ProductVariantRepository;
import com.hikiw.ecommerce.module.user.entity.UserEntity;
import com.hikiw.ecommerce.module.user.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CartService {

    CartRepository cartRepository;
    CartItemRepository cartItemRepository;
    CartMapper cartMapper;
    UserRepository userRepository;
    ProductVariantRepository productVariantRepository;


    // phải có cái này để tránh trường hợp user không mua gì dẫn tới cart rỗng ~ dư thừa (Trường hợp mặc định tài khoản có sẵn cart)
    // Nếu user đã từng add hàng → có cart → lấy
    // Nếu user chưa từng add → tạo lần đầu
    @Transactional
    public CartEntity getOrCreateCart(Long userId){
        return cartRepository.findByUser_Id(userId)
                .orElseGet(() -> createCartSafely(userId));
    }

    private CartEntity createCartSafely(Long userId){
        UserEntity user = userRepository
                .findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        CartEntity cart = CartEntity.builder()
                .user(user)
                .build();
        return cartRepository.save(cart);
    }


    // add product to cart
    @Transactional
    public CartResponse addProductToCart(Long userId, AddToCartRequest request){
        CartEntity cart = getOrCreateCart(userId);
        ProductVariantEntity productVariant = productVariantRepository.findById(request.getProductVariantId())
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_VARIANT_NOT_EXISTED));

        // kiểm tra xem sẩn phẩm có đang hoạt động không
        if(!productVariant.getIsActive()){
            throw new AppException(ErrorCode.PRODUCT_NOT_EXISTED);
        }

        // kiểm tra sản phẩm còn đủ hàng không
        if(!productVariant.canOrder(request.getQuantity())){
            throw new AppException(ErrorCode.INSUFFICIENT_STOCK);
        }
        CartItemEntity itemToSave;
        // kiểm tra nếu sản phẩm còn đang tồn tại trong giỏ hàng
        Optional<CartItemEntity> existingItem = cartItemRepository
                .findByCart_CartIdAndProductVariant_ProductVariantId(cart.getCartId(), request.getProductVariantId());

        if(existingItem.isPresent()){
            itemToSave = existingItem.get();
            // xử lý trường hợp click nhanh khiến số lượng thêm vào giỏ vượt quá tồn kho
            int newQuantity = itemToSave.getQuantity() + request.getQuantity();

            if(!productVariant.canOrder(request.getQuantity())){
                throw new AppException(ErrorCode.INSUFFICIENT_STOCK);
            }
            itemToSave.setQuantity(newQuantity);
        }
        else{
            itemToSave = CartItemEntity.builder()
                    .cart(cart)
                    .productVariant(productVariant)
                    .quantity(request.getQuantity())
                    .build();
        }
        cartItemRepository.save(itemToSave);
        return getCart(userId);
    }


    // get cart
    @Transactional
    public CartResponse getCart(Long userId){
        CartEntity cart = getOrCreateCart(userId);
        return buildCartResponse(cart.getUser().getId());
    }


    // cập nhât số lượng sản phẩm trong giỏ hàng
    @Transactional
    public CartResponse updateCartItem(Long userId, Long cartItemId, CartUpdateRequest request){
        CartEntity cart = getOrCreateCart(userId);
        CartItemEntity item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new AppException(ErrorCode.CART_ITEM_NOT_EXISTED));

        // check xem item co thuoc ve user day khong
        if(!item.getCart().getCartId().equals(cart.getCartId())){
            throw new AppException(ErrorCode.CART_ITEM_NOT_EXISTED);
        }

        // check stock
        if(request.getQuantity() > item.getProductVariant().getStock()){
            throw new AppException(ErrorCode.INSUFFICIENT_STOCK);
        }

        item.setQuantity(request.getQuantity());
        cartItemRepository.save(item);

        return getCart(userId);
    }

    // xoá sản phẩm khỏi giỏ hàng ( xoá item trong db, sau đấy cart remove cái item đấy đi)
    @Transactional
    public CartResponse removeCartItem(Long userId, Long cartItemId){
        CartItemEntity item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new AppException(ErrorCode.CART_ITEM_NOT_EXISTED));
        CartEntity cart = item.getCart();
        cartItemRepository.delete(item);
        cart.getItems().remove(item);
        return buildCartResponse(userId);
    }
    // xoá toàn bộ giỏ hàng
    @Transactional
    public CartResponse clearCart(Long userId){
        CartEntity cart = getOrCreateCart(userId);
        cartItemRepository.deleteAll(cart.getItems());
        cart.getItems().clear();
        return buildCartResponse(userId);
    }



    // xây dựng response cho từng item trong giỏ hàng
    private CartItemResponse buildCartItemResponse(CartItemEntity item) {
        CartItemResponse response = cartMapper.toItemResponse(item);
        // Set 4 field thủ công
        response.setInStock(item.getProductVariant().getInStock());
        response.setSubtotal(item.getSubtotal());
        response.setDiscountPercentage(item.getProductVariant().getDiscountPercentage());
        response.setVariantValues(
                item.getProductVariant().getVariantMappings().stream()
                        .map(mapping -> VariantInfo.builder()
                                .variantName(mapping.getVariantValue().getVariant().getVariantName())
                                .valueName(mapping.getVariantValue().getValueName())
                                .imageUrl(mapping.getVariantValue().getImageUrl())
                                .build())
                        .collect(java.util.stream.Collectors.toList())
        );
        return response;
    }

    // xây dựng response cho giỏ hàng
    private CartResponse buildCartResponse(Long userId) {
        CartEntity cart = cartRepository.findByUser_Id(userId)
                .orElseThrow(() -> new AppException(ErrorCode.CART_NOT_EXISTED));
        List<CartItemResponse> items = cart.getItems().stream()
                .map(this::buildCartItemResponse)
                .toList();
        CartResponse response = cartMapper.toResponse(cart);
        response.setItems(items);
        return response;
    }

    // ========== VALIDATE CART (dùng trước khi tạo Order) ==========
    public void validateCart(Long userId) {
        CartEntity cart = getOrCreateCart(userId);

        if (cart.getItems().isEmpty()) {
            throw new AppException(ErrorCode.CART_IS_EMPTY);
        }

        for (CartItemEntity item : cart.getItems()) {
            ProductVariantEntity variant = item.getProductVariant();

            if (!variant.getIsActive()) {
                throw new AppException(ErrorCode.PRODUCT_NOT_EXISTED);
            }

            if (variant.getStock() < item.getQuantity()) {
                throw new AppException(ErrorCode.INSUFFICIENT_STOCK);
            }
        }

    }

    private Double calculateShippingFee(Double subtotal) {
        // Miễn phí ship khi đơn từ 500k
        return (subtotal != null && subtotal >= 500000) ? 0.0 : 30000.0;
    }

}
