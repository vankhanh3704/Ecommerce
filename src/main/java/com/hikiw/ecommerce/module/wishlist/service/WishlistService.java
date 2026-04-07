package com.hikiw.ecommerce.module.wishlist.service;


import com.hikiw.ecommerce.Enum.ErrorCode;
import com.hikiw.ecommerce.common.Exception.AppException;
import com.hikiw.ecommerce.module.product.entity.ProductEntity;
import com.hikiw.ecommerce.module.product.repository.ProductRepository;
import com.hikiw.ecommerce.module.product_variant.entity.ProductVariantEntity;
import com.hikiw.ecommerce.module.user.entity.UserEntity;
import com.hikiw.ecommerce.module.user.repository.UserRepository;
import com.hikiw.ecommerce.module.wishlist.dto.WishlistResponse;
import com.hikiw.ecommerce.module.wishlist.dto.WishlistSummaryResponse;
import com.hikiw.ecommerce.module.wishlist.entity.WishlistEntity;
import com.hikiw.ecommerce.module.wishlist.mapper.WishlistMapper;
import com.hikiw.ecommerce.module.wishlist.repository.WishlistRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class WishlistService {
    WishlistRepository wishlistRepository;
    ProductRepository productRepository;
    UserRepository userRepository;
    WishlistMapper wishlistMapper;


    // adđ product to wishlist
    @Transactional
    public WishlistResponse addProductToWishlist(Long productId, Long userId) {
        if(wishlistRepository.existsByUser_IdAndProduct_ProductId(userId, productId)) {
            throw new AppException(ErrorCode.WISHLIST_ITEM_ALREADY_EXISTED);
        }
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));

        WishlistEntity wishlist = WishlistEntity.builder()
                .user(user)
                .product(product)
                .build();
        return buildWishlistResponse(wishlistRepository.save(wishlist));
    }





    // remove product from wishlist
    @Transactional
    public void removeProductFromWishlist(Long productId, Long userId) {
        if (!wishlistRepository.existsByUser_IdAndProduct_ProductId(userId, productId)) {
            throw new AppException(ErrorCode.WISHLIST_ITEM_NOT_EXISTED);
        }

        wishlistRepository.deleteByUser_IdAndProduct_ProductIdCustom(userId, productId);
    }


    // ========== TOGGLE — ADD NẾU CHƯA CÓ, XÓA NẾU ĐÃ CÓ ==========
    @Transactional
    public boolean toggleWishlist(Long productId, Long userId) {
        if (wishlistRepository.existsByUser_IdAndProduct_ProductId(userId, productId)) {
            wishlistRepository.deleteByUser_IdAndProduct_ProductIdCustom(userId, productId);
            return false; // false = đã xóa
        } else {
            addProductToWishlist(productId, userId);
            return true;  // true = đã thêm
        }
    }
    // private helper method
    private WishlistResponse buildWishlistResponse(WishlistEntity entity){
        WishlistResponse response = wishlistMapper.toResponse(entity);

        ProductEntity product = entity.getProduct();
        List<ProductVariantEntity> variants = product.getVariants();

        // Lấy ảnh chính của product
        if (product.getImages() != null && !product.getImages().isEmpty()) {
            product.getImages().stream()
                    .filter(img -> Boolean.TRUE.equals(img.getIsPrimary()))
                    .findFirst()
                    .ifPresent(img -> response.setProductImageUrl(img.getImageUrl()));
        }

        // Tính min/max price và isInStock từ variants
        if (variants != null && !variants.isEmpty()) {
            List<ProductVariantEntity> activeVariants = variants.stream()
                    .filter(v -> Boolean.TRUE.equals(v.getIsActive()))
                    .toList();

            if (!activeVariants.isEmpty()) {
                double minPrice = activeVariants.stream()
                        .mapToDouble(ProductVariantEntity::getPrice)
                        .min()
                        .orElse(0.0);

                double maxPrice = activeVariants.stream()
                        .mapToDouble(ProductVariantEntity::getPrice)
                        .max()
                        .orElse(0.0);

                boolean hasStock = activeVariants.stream()
                        .anyMatch(v -> v.getStock() != null && v.getStock() > 0);

                response.setMinPrice(minPrice);
                response.setMaxPrice(maxPrice);
                response.setIsInStock(hasStock);
            }
        }
        return response;
    }

    public WishlistSummaryResponse getMyWishlist(Long userId) {
        List<WishlistEntity> wishlistItems = wishlistRepository.findByUser_IdOrderByCreatedDateDesc(userId);

        List<WishlistResponse> responses = wishlistItems.stream()
                .map(this::buildWishlistResponse)
                .toList();

        return WishlistSummaryResponse.builder()
                .userId(userId)
                .totalItems(wishlistRepository.countByUser_Id(userId))
                .items(responses)
                .build();
    }

    // Kiểm tra xem sản phẩm đã có trong wishlist của user chưa
    public boolean isInWishlist(Long productId, Long userId) {
        return wishlistRepository.existsByUser_IdAndProduct_ProductId(userId, productId);
    }


}
