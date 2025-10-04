package com.hikiw.ecommerce.Service;

import com.hikiw.ecommerce.Entity.UserEntity;
import com.hikiw.ecommerce.Enum.ErrorCode;
import com.hikiw.ecommerce.Exception.AppException;
import com.hikiw.ecommerce.Mapper.UserMapper;
import com.hikiw.ecommerce.Model.Request.UserCreationRequest;
import com.hikiw.ecommerce.Model.Request.UserUpdateRequest;
import com.hikiw.ecommerce.Model.Response.UserResponse;
import com.hikiw.ecommerce.Repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class UserService {
    UserRepository userRepository;
    UserMapper userMapper;


    public UserResponse createUser(UserCreationRequest request){
        if(userRepository.existsByUsername(request.getUsername())){
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        UserEntity userEntity = userMapper.toEntity(request);
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
