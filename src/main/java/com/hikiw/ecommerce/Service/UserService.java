package com.hikiw.ecommerce.Service;

import com.hikiw.ecommerce.Entity.UserEntity;
import com.hikiw.ecommerce.Model.Request.UserCreationRequest;
import com.hikiw.ecommerce.Model.Response.UserResponse;
import com.hikiw.ecommerce.Repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class UserService {
    UserRepository userRepository;

    public UserResponse createUser(UserCreationRequest request){
        UserEntity userEntity = UserEntity.builder()
                .username(request.getUsername())
                .password(request.getPassword())
                .build();
        userEntity = userRepository.save(userEntity);
        log.info("User created with id: {}", userEntity.getId());
        return new UserResponse(userEntity.getId(), userEntity.getUsername(), userEntity.getPassword());
    }
}
