package com.tutorial.identity_service.service;

import com.tutorial.identity_service.dto.request.UserCreationRequest;
import com.tutorial.identity_service.dto.request.UserUpdateRequest;
import com.tutorial.identity_service.entity.User;
import com.tutorial.identity_service.exception.AppRunTimeException;
import com.tutorial.identity_service.exception.ErrorCode;
import com.tutorial.identity_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService  {
    @Autowired
    private UserRepository userRepository;

    public User createUser(UserCreationRequest request) {
        User user = new User();

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new AppRunTimeException(ErrorCode.USER_EXISTS);
        }

        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setDateOfBirth(request.getDateOfBirth());

        return userRepository.save(user);
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public User getUser(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public User updateUser(String id, UserUpdateRequest request) {
        User userTaken = getUser(id);

        userTaken.setPassword(request.getPassword());
        userTaken.setFirstName(request.getFirstName());
        userTaken.setLastName(request.getLastName());
        userTaken.setDateOfBirth(request.getDateOfBirth());

        return userRepository.save(userTaken);
    }
}
