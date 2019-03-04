package com.thoughtworks.mall_user.controller;

import com.thoughtworks.mall_user.controller.request.LoginRequest;
import com.thoughtworks.mall_user.exception.UserNotFoundException;
import com.thoughtworks.mall_user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<Long> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(userService.GetUserId(loginRequest));

    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    private void userNotFoundExceptionHandle(UserNotFoundException ex) {

    }


}
