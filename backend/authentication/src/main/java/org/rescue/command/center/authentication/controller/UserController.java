package org.rescue.command.center.authentication.controller;

import org.rescue.command.center.authentication.dto.request.CreateUserRequestDto;
import org.rescue.command.center.authentication.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ResponseStatus;

@RestController
@RequestMapping("/api/v1/authentication/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public org.rescue.command.center.authentication.dto.base.User createUser(@RequestBody CreateUserRequestDto requestDto) {
        return userService.saveUser(requestDto);
    }
}