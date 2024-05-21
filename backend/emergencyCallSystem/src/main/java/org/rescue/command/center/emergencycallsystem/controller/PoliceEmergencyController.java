package org.rescue.command.center.emergencycallsystem.controller;

import org.javatuples.Pair;
import org.rescue.command.center.emergencycallsystem.dto.base.PoliceEmergencyDto;
import org.rescue.command.center.emergencycallsystem.dto.base.PoliceMessageDto;
import org.rescue.command.center.emergencycallsystem.dto.base.PoliceEmergencyDto;
import org.rescue.command.center.emergencycallsystem.dto.request.CreatePoliceEmergencyDto;
import org.rescue.command.center.emergencycallsystem.dto.request.CreatePoliceMessageRequestDto;
import org.rescue.command.center.emergencycallsystem.dto.response.PoliceEmergencyResponseDto;

import org.rescue.command.center.emergencycallsystem.dto.response.PoliceEmergencyResponseDto;
import org.rescue.command.center.emergencycallsystem.service.PoliceEmergencyCallService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/emergency/police")
public class PoliceEmergencyController {

    private final PoliceEmergencyCallService policeEmergencyCallService;

    @Autowired
    public PoliceEmergencyController(PoliceEmergencyCallService policeEmergencyCallService) {
        this.policeEmergencyCallService = policeEmergencyCallService;
    }

    @GetMapping("/health")
    public String health() {
        return "healthy";
    }

    @GetMapping
    public ResponseEntity<PoliceEmergencyResponseDto<List<PoliceEmergencyDto>>> getPoliceEmergencyCalls(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        PoliceEmergencyResponseDto<List<PoliceEmergencyDto>> responseDto = policeEmergencyCallService.getPoliceEmergencyCalls(token.substring(7).trim());

        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPoliceEmergencyCalls(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
            @PathVariable long id) {
        PoliceEmergencyResponseDto<Pair<PoliceEmergencyDto, List<PoliceMessageDto>>> responseDto = policeEmergencyCallService.getPoliceEmergencyCallById(id, token.substring(7).trim());

        return ResponseEntity.ok(responseDto);
    }

    @PostMapping
    public ResponseEntity<?> createPoliceEmergencyCall(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
            @RequestBody CreatePoliceEmergencyDto createPoliceEmergencyDto) {
        PoliceEmergencyResponseDto<PoliceEmergencyDto> responseDto = policeEmergencyCallService.createPoliceEmergencyCall(createPoliceEmergencyDto, token.substring(7).trim());

        if(responseDto == null)
            return ResponseEntity.internalServerError().body("Failed to create police emergency call");

        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/message")
    public ResponseEntity<?> createPoliceMessage(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
            @RequestBody CreatePoliceMessageRequestDto requestDto) {
        PoliceEmergencyResponseDto<PoliceMessageDto> responseDto = policeEmergencyCallService.createPoliceMessage(requestDto, token.substring(7).trim());

        if(responseDto == null)
            return ResponseEntity.internalServerError().body("Failed to create police emergency message");

        return ResponseEntity.ok(responseDto);
    }

}