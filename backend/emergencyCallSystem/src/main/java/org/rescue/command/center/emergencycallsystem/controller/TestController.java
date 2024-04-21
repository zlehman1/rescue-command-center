package org.rescue.command.center.emergencycallsystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/emergency/test")

public class TestController {
    private Environment environment;

    @Autowired
    public TestController(Environment environment){
        this.environment = environment;
    }

    @GetMapping
    public String health() {
        return "Port: " + environment.getProperty("local.server.port");
    }

}