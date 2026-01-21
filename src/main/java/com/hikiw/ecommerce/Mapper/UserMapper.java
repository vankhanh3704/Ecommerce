package com.hikiw.ecommerce.Mapper;

import com.hikiw.ecommerce.Entity.UserEntity;
import com.hikiw.ecommerce.Model.Request.UserCreationRequest;
import com.hikiw.ecommerce.Model.Request.UserUpdateRequest;
import com.hikiw.ecommerce.Model.Response.UserResponse;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface    UserMapper {
    UserEntity toEntity(UserCreationRequest request);

    UserResponse toUserResponse(UserEntity entity);

    // map du lieu update user vao entity
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)

    void toUpdateUser(@MappingTarget UserEntity user, UserUpdateRequest request);
}
