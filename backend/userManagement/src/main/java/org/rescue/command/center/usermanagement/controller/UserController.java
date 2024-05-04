package org.rescue.command.center.usermanagement.controller;

import org.rescue.command.center.usermanagement.dto.base.HttpResponseCodeDto;
import org.rescue.command.center.usermanagement.dto.base.UserDto;
import org.rescue.command.center.usermanagement.dto.request.CreateUserRequestDto;
import org.rescue.command.center.usermanagement.dto.request.UpdateUserRequestDto;
import org.rescue.command.center.usermanagement.dto.response.UserResponseDto;
import org.rescue.command.center.usermanagement.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(
            @PathVariable Long id,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        UserResponseDto responseDto = userService.getUserById(id, token.substring(7).trim());

        if(responseDto == null)
            return ResponseEntity.notFound().build();

        if(responseDto.getHttpCodeDetails() != null && responseDto.getHttpCodeDetails().getCode() == 403)
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/user")
    public ResponseEntity<?> getUserByUsername(
            @RequestParam String username,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        UserResponseDto responseDto = userService.getUserByUsername(username, token.substring(7).trim());

        if(responseDto.getHttpCodeDetails().getCode() == 404)
            return ResponseEntity.notFound().build();

        if(responseDto.getHttpCodeDetails() != null && responseDto.getHttpCodeDetails().getCode() == 403)
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        return ResponseEntity.ok(responseDto);
    }

    @GetMapping
    public ResponseEntity<?> getAllUsers(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        UserResponseDto<List<UserDto>> responseDto = userService.getAllUsers(token.substring(7).trim());

        if(responseDto.getHttpCodeDetails() != null && responseDto.getHttpCodeDetails().getCode() == 403)
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        return ResponseEntity.ok(responseDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(
            @PathVariable Long id,
            @RequestBody UpdateUserRequestDto requestDto,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token){
        UserResponseDto responseDto = userService.updateUserById(id, requestDto, token.substring(7).trim());

        if(responseDto.getHttpCodeDetails().getCode() == 404)
            return ResponseEntity.notFound().build();

        if(responseDto.getHttpCodeDetails() != null && responseDto.getHttpCodeDetails().getCode() == 403)
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        return ResponseEntity.ok(responseDto);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> createUser(@RequestBody CreateUserRequestDto requestDto) {
        UserResponseDto responseDto = userService.saveUser(requestDto);

        if(responseDto.getHttpCodeDetails().getCode() == 409)
            return ResponseEntity.status(HttpStatus.CONFLICT).build();

        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(
            @PathVariable Long id,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token){
        HttpResponseCodeDto responseCodeDto = userService.deleteUser(id, token.substring(7).trim());

        if (responseCodeDto.getCode() == 404)
            return ResponseEntity.notFound().build();
        else if (responseCodeDto.getCode() == 403)
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/health")
    public String health() {
        return "healthy";
    }
}