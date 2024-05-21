package org.rescue.command.center.emergencycallsystem.controller;

import org.rescue.command.center.emergencycallsystem.service.FireEmergencyCallService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/emergency/share")
public class ShareEmergencyController {

    private final FireEmergencyCallService fireEmergencyCallService;

    @Autowired
    public ShareEmergencyController(FireEmergencyCallService fireEmergencyCallService) {
        this.fireEmergencyCallService = fireEmergencyCallService;
    }

    @PutMapping("/health")
    public String health() {
        return "healthy";
    }
}