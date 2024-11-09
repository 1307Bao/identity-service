package com.tutorial.identity_service.service;

import com.tutorial.identity_service.dto.request.UserCreationRequest;
import com.tutorial.identity_service.dto.request.UserUpdateRequest;
import com.tutorial.identity_service.dto.response.UserResponse;
import com.tutorial.identity_service.entity.User;
import com.tutorial.identity_service.exception.AppRunTimeException;
import com.tutorial.identity_service.exception.ErrorCode;
import com.tutorial.identity_service.mapper.UserMapper;
import com.tutorial.identity_service.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService  {

    UserRepository userRepository;
    UserMapper userMapper;

    public User createUser(UserCreationRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new AppRunTimeException(ErrorCode.USER_EXISTS);
        }

        User user = userMapper.toUser(request);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        return userRepository.save(user);
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }

    public UserResponse getUser(String id) {
        return userMapper.toUserResponse(userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found")));
    }

    public UserResponse updateUser(String id, UserUpdateRequest request) {
        User userTaken = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        userMapper.updateUser(userTaken, request);

        return userMapper.toUserResponse(userRepository.save(userTaken));
    }
}
