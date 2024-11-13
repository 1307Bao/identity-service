package com.tutorial.identity_service.mapper;

import com.tutorial.identity_service.dto.request.RoleRequest;
import com.tutorial.identity_service.dto.response.RoleResponse;
import com.tutorial.identity_service.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "Spring")
public interface RoleMapper {
    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleRequest request);
    RoleResponse toRoleResponse(Role role);
}
