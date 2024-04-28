package org.rescue.command.center.usermanagement.controller;

import org.rescue.command.center.usermanagement.dto.request.CreateUserRequestDto;
import org.rescue.command.center.usermanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public org.rescue.command.center.usermanagement.dto.base.User createUser(@RequestBody CreateUserRequestDto requestDto) {
        return userService.saveUser(requestDto);
    }
}