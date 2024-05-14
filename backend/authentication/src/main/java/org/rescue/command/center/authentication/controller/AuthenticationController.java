package org.rescue.command.center.authentication.controller;

import org.rescue.command.center.authentication.dto.request.LoginRequestDto;

import org.rescue.command.center.authentication.service.AuthenticationService;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/authentication")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(
            @RequestBody LoginRequestDto loginRequest){
        String token = authenticationService.login(loginRequest);

        if(token.isEmpty())
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(authenticationService.login(loginRequest));
    }
}