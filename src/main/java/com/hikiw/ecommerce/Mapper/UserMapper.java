package com.hikiw.ecommerce.Mapper;

import com.hikiw.ecommerce.Entity.UserEntity;
import com.hikiw.ecommerce.Model.Request.UserCreationRequest;
import com.hikiw.ecommerce.Model.Response.UserResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserEntity toEntity(UserCreationRequest request);

    UserResponse toUserResponse(UserEntity entity);

}
