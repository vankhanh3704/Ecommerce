package com.hikiw.ecommerce.module.cart.service;


import com.hikiw.ecommerce.Enum.ErrorCode;
import com.hikiw.ecommerce.common.Exception.AppException;
import com.hikiw.ecommerce.module.cart.dto.CartResponse;
import com.hikiw.ecommerce.module.cart.entity.CartEntity;
import com.hikiw.ecommerce.module.cart.mapper.CartMapper;
import com.hikiw.ecommerce.module.cart.repository.CartRepository;
import com.hikiw.ecommerce.module.user.entity.UserEntity;
import com.hikiw.ecommerce.module.user.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional(readOnly = true)
public class CartService {

    CartRepository cartRepository;
    CartMapper cartMapper;
    UserRepository userRepository;


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
}
