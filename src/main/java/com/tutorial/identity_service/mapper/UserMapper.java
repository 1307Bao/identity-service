package com.tutorial.identity_service.mapper;

import com.tutorial.identity_service.dto.request.UserCreationRequest;
import com.tutorial.identity_service.dto.request.UserUpdateRequest;
import com.tutorial.identity_service.dto.response.UserResponse;
import com.tutorial.identity_service.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "Spring")
public interface UserMapper {
    User toUser(UserCreationRequest request);
    UserResponse toUserResponse(User user);
    void updateUser(@MappingTarget User user, UserUpdateRequest request);
}
