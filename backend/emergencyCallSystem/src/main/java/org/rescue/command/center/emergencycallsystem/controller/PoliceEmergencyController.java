package org.rescue.command.center.emergencycallsystem.controller;

import org.rescue.command.center.emergencycallsystem.dto.base.PoliceEmergencyDto;
import org.rescue.command.center.emergencycallsystem.dto.response.PoliceEmergencyResponseDto;
import org.rescue.command.center.emergencycallsystem.service.EmergencyCallService;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/emergency/police")
public class PoliceEmergencyController {

    private final EmergencyCallService emergencyCallService;

    public PoliceEmergencyController(EmergencyCallService emergencyCallService) {
        this.emergencyCallService = emergencyCallService;
    }

    @GetMapping("/health")
    public String health() {
        return "healthy";
    }

    @GetMapping
    public ResponseEntity<PoliceEmergencyResponseDto<List<PoliceEmergencyDto>> > getFireEmergencyCalls(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        PoliceEmergencyResponseDto<List<PoliceEmergencyDto>> responseDto = emergencyCallService.getPoliceEmergencyCalls(token.substring(7).trim());

        return ResponseEntity.ok(responseDto);
    }
}