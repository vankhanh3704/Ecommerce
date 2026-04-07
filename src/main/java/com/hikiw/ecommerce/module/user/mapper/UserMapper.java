package com.hikiw.ecommerce.module.user.mapper;

import com.hikiw.ecommerce.module.user.dto.UserProfileUpdateRequest;
import com.hikiw.ecommerce.module.user.dto.UserResponse;
import com.hikiw.ecommerce.module.user.dto.UserUpdateRequest;
import com.hikiw.ecommerce.module.user.dto.UserCreationRequest;
import com.hikiw.ecommerce.module.user.entity.UserEntity;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "roles", ignore = true)
    UserEntity toEntity(UserCreationRequest request);

    UserResponse toUserResponse(UserEntity entity);

    // map du lieu update user vao entity
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "roles", ignore = true)
    void toUpdateUser(@MappingTarget UserEntity user, UserUpdateRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "username", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "shop", ignore = true)
    @Mapping(target = "cart", ignore = true)
    void updateUserProfileFromRequest(UserProfileUpdateRequest request, @MappingTarget UserEntity entity);
}
