package com.thoughtworks.mall_user.service;

import com.thoughtworks.mall_user.controller.request.LoginRequest;
import com.thoughtworks.mall_user.entity.User;
import com.thoughtworks.mall_user.exception.UserNotFoundException;
import com.thoughtworks.mall_user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public long GetUserId(LoginRequest loginRequest) {
        User user = userRepository.findByName(loginRequest.getName()).orElseThrow(UserNotFoundException::new);
        return user.getId();
    }
}
