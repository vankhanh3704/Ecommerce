package com.hikiw.ecommerce.Mapper;

import com.hikiw.ecommerce.Entity.RoleEntity;
import com.hikiw.ecommerce.Model.Request.role.RoleRequest;
import com.hikiw.ecommerce.Model.Response.RoleResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    RoleEntity toRoleEntity(RoleRequest request);
    RoleResponse toRoleResponse(RoleEntity roleEntity);
}
