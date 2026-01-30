package com.hikiw.ecommerce.module.role.mapper;

import com.hikiw.ecommerce.module.role.dto.RoleRequest;
import com.hikiw.ecommerce.module.role.dto.RoleResponse;
import com.hikiw.ecommerce.module.role.entity.RoleEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    RoleEntity toRoleEntity(RoleRequest request);
    RoleResponse toRoleResponse(RoleEntity roleEntity);
}
