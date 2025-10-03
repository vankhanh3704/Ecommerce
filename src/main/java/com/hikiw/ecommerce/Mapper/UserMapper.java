package com.hikiw.ecommerce.Mapper;

import com.hikiw.ecommerce.Entity.UserEntity;
import com.hikiw.ecommerce.Model.Request.UserCreationRequest;
import com.hikiw.ecommerce.Model.Request.UserUpdateRequest;
import com.hikiw.ecommerce.Model.Response.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserEntity toEntity(UserCreationRequest request);

    UserResponse toUserResponse(UserEntity entity);

    // map du lieu update user vao entity
    void toUpdateUser(@MappingTarget UserEntity user, UserUpdateRequest request);
}
