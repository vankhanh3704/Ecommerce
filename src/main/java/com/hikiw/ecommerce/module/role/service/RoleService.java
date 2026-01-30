package com.hikiw.ecommerce.module.role.service;

import com.hikiw.ecommerce.module.role.dto.RoleRequest;
import com.hikiw.ecommerce.module.role.dto.RoleResponse;
import com.hikiw.ecommerce.module.role.entity.RoleEntity;
import com.hikiw.ecommerce.module.role.mapper.RoleMapper;
import com.hikiw.ecommerce.module.role.repository.RoleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleService {
    RoleRepository roleRepository;
    RoleMapper roleMapper;

    public RoleResponse create(RoleRequest request){
        RoleEntity roleEntity = roleMapper.toRoleEntity(request);
        roleRepository.save(roleEntity);
        return roleMapper.toRoleResponse(roleEntity);
    }

    public List<RoleResponse> getAll(){
        return roleRepository.findAll().stream().map(roleMapper::toRoleResponse).toList();
    }

    public void delete(String id){
        roleRepository.deleteById(id);
    }
}
