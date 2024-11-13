package com.tutorial.identity_service.mapper;

import com.tutorial.identity_service.dto.request.PermissionRequest;
import com.tutorial.identity_service.dto.response.PermissionResponse;
import com.tutorial.identity_service.entity.Permission;
import org.mapstruct.Mapper;

@Mapper(componentModel = "Spring")
public interface PermissionMapper {
    Permission toPermission(PermissionRequest request);
    PermissionResponse toPermissionResponse(Permission permission);
}
