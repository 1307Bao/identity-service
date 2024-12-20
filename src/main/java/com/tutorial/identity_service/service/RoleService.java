package com.tutorial.identity_service.service;

import com.tutorial.identity_service.dto.request.PermissionRequest;
import com.tutorial.identity_service.dto.request.RoleRequest;
import com.tutorial.identity_service.dto.response.PermissionResponse;
import com.tutorial.identity_service.dto.response.RoleResponse;
import com.tutorial.identity_service.entity.Permission;
import com.tutorial.identity_service.mapper.RoleMapper;
import com.tutorial.identity_service.repository.PermissionRepository;
import com.tutorial.identity_service.repository.RoleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleService {
    RoleRepository roleRepository;
    PermissionRepository permissionRepository;
    RoleMapper roleMapper;

    public RoleResponse create(RoleRequest request) {
        var role = roleMapper.toRole(request);
        var permissions = permissionRepository.findAllById(request.getPermissions());

        role.setPermissions(new HashSet<>(permissions));
        role = roleRepository.save(role);

        return roleMapper.toRoleResponse(role);
    }

    public List<RoleResponse> getALl() {
        return roleRepository.findAll()
                .stream()
                .map(roleMapper::toRoleResponse)
                .toList();
    }

    public void delete(String role) {
        roleRepository.deleteById(role);
    }
}
