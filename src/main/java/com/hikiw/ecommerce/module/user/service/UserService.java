package com.hikiw.ecommerce.module.user.service;

import com.hikiw.ecommerce.module.cart.entity.CartEntity;
import com.hikiw.ecommerce.module.cart.repository.CartRepository;
import com.hikiw.ecommerce.module.cart.service.CartService;
import com.hikiw.ecommerce.module.role.entity.RoleEntity;
import com.hikiw.ecommerce.Enum.ErrorCode;
import com.hikiw.ecommerce.common.Exception.AppException;
import com.hikiw.ecommerce.module.role.repository.RoleRepository;
import com.hikiw.ecommerce.common.constant.PredefinedRole;
import com.hikiw.ecommerce.module.user.dto.UserUpdateRequest;
import com.hikiw.ecommerce.module.user.dto.UserCreationRequest;
import com.hikiw.ecommerce.module.user.dto.UserResponse;
import com.hikiw.ecommerce.module.user.entity.UserEntity;
import com.hikiw.ecommerce.module.user.mapper.UserMapper;
import com.hikiw.ecommerce.module.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;

@Service
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class UserService {
    UserRepository userRepository;
    UserMapper userMapper;
    RoleRepository  roleRepository;
    CartRepository cartRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public UserResponse createUser(UserCreationRequest request) {
        if (userRepository.existsByUsername(request.getUsername()) ||
                userRepository.existsByEmail(request.getEmail())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        UserEntity userEntity = userMapper.toEntity(request);
        userEntity.setPassword(passwordEncoder.encode(request.getPassword()));

        // Gán quyền theo Request của Admin, nếu rỗng thì gán USER mặc định
        HashSet<RoleEntity> roles = new HashSet<>();
        if (request.getRoles() != null && !request.getRoles().isEmpty()) {
            request.getRoles().forEach(roleName -> {
                roleRepository.findById(roleName).ifPresent(roles::add);
            });
        } else {
            roleRepository.findById(PredefinedRole.USER_ROLE).ifPresent(roles::add);
        }
        userEntity.setRoles(roles);


        UserEntity savedUser = userRepository.save(userEntity);
        // Tạo cart rỗng cho user mới
        cartRepository.save(CartEntity.builder().user(savedUser).build());
        return userMapper.toUserResponse(savedUser);
    }


    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public UserResponse updateUser(Long userId, UserUpdateRequest request) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        if (request.getEmail() != null
                && !request.getEmail().equals(userEntity.getEmail())
                && userRepository.existsByEmail(request.getEmail())) {
            throw new AppException(ErrorCode.USER_EXISTED); // Hoặc mã lỗi EMAIL_ALREADY_EXISTS nếu bạn có
        }
        if (request.getPhoneNumber() != null
                && !request.getPhoneNumber().equals(userEntity.getPhoneNumber())
                && userRepository.existsByPhoneNumber(request.getPhoneNumber())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }
        // Cập nhật thông tin cơ bản
        userMapper.toUpdateUser(userEntity, request); // Nhớ bỏ set password trong hàm này ở Mapper

        // Cập nhật Role nếu Admin có truyền lên
        if (request.getRoles() != null && !request.getRoles().isEmpty()) {
            HashSet<RoleEntity> roles = new HashSet<>();
            request.getRoles().forEach(roleName -> {
                roleRepository.findById(roleName).ifPresent(roles::add);
            });
            userEntity.setRoles(roles);
        }

        return userMapper.toUserResponse(userRepository.save(userEntity));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse getUserById(Long id) {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        return userMapper.toUserResponse(userEntity);
    }

    // NÂNG CẤP: Phân trang (Pagination) thay vì findAll()
    @PreAuthorize("hasRole('ADMIN')")
    public Page<UserResponse> getUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size); // Page trong Spring bắt đầu từ 0
        return userRepository.findAll(pageable)
                .map(userMapper::toUserResponse);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void deleteUser(Long id) {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        userEntity.setIsActive(false);
        userRepository.save(userEntity);
    }



}
