package com.hikiw.ecommerce.module.user.mapper;

import com.hikiw.ecommerce.module.user.dto.UserResponse;
import com.hikiw.ecommerce.module.user.dto.UserUpdateRequest;
import com.hikiw.ecommerce.module.user.dto.UserCreationRequest;
import com.hikiw.ecommerce.module.user.entity.UserEntity;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface    UserMapper {
    UserEntity toEntity(UserCreationRequest request);

    UserResponse toUserResponse(UserEntity entity);

    // map du lieu update user vao entity
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)

    void toUpdateUser(@MappingTarget UserEntity user, UserUpdateRequest request);
}
