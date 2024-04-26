package org.rescue.command.center.authentication.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/authentication/home")
public class LoginController {
    @GetMapping
    public String health() {
        return "healthy";
    }
}