package org.rescue.command.center.authentication.controller;

import org.rescue.command.center.authentication.dto.request.LoginRequestDto;
import org.rescue.command.center.authentication.dto.response.LoginResponseDto;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/authentication/login")
public class AuthenticationController {
    @PostMapping
    public ResponseEntity<?> createAuthenticationToken(@RequestBody LoginRequestDto loginRequest) throws Exception {
        return ResponseEntity.ok(new LoginResponseDto("jwt"));
    }
}