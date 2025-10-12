package com.hikiw.ecommerce.Service;

import com.hikiw.ecommerce.Entity.RoleEntity;
import com.hikiw.ecommerce.Entity.UserEntity;
import com.hikiw.ecommerce.Enum.ErrorCode;
import com.hikiw.ecommerce.Exception.AppException;
import com.hikiw.ecommerce.Mapper.UserMapper;
import com.hikiw.ecommerce.Model.Request.UserCreationRequest;
import com.hikiw.ecommerce.Model.Request.UserUpdateRequest;
import com.hikiw.ecommerce.Model.Response.UserResponse;
import com.hikiw.ecommerce.Repository.RoleRepository;
import com.hikiw.ecommerce.Repository.UserRepository;
import com.hikiw.ecommerce.constant.PredefinedRole;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class UserService {
    UserRepository userRepository;
    UserMapper userMapper;
    RoleRepository  roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;


    public UserResponse createUser(UserCreationRequest request){
        if(userRepository.existsByUsername(request.getUsername())){
            throw new AppException(ErrorCode.USER_EXISTED);
        }
        UserEntity userEntity = userMapper.toEntity(request);
        // encode password
        userEntity.setPassword(passwordEncoder.encode(request.getPassword()));

        HashSet<RoleEntity> roles = new HashSet<>();
        roleRepository.findById(PredefinedRole.USER_ROLE).ifPresent(roles::add); // map với role trong db nếu có, không có thì phải tạo
        userEntity.setRoles(roles);
        try {
            userEntity = userRepository.save(userEntity);
        } catch (DataIntegrityViolationException exception) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }
        return userMapper.toUserResponse(userEntity);
    }


    public UserResponse getUserById(Long id){
        UserEntity userEntity = userRepository
                .findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        return userMapper.toUserResponse(userEntity);
    }
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> getUsers(){
        return userRepository.findAll()
                .stream()
                .map(userMapper::toUserResponse)
                .toList();
    }

    public void deleteUser(Long id){
        userRepository.deleteById(id);
    }

    public UserResponse updateUser(Long userId,UserUpdateRequest request){
        UserEntity userEntity = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not exists"));
        userMapper.toUpdateUser(userEntity, request);
        return userMapper.toUserResponse(userRepository.save(userEntity));
    }
}
