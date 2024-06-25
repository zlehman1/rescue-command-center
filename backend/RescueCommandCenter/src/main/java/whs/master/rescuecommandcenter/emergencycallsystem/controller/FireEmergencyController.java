package whs.master.rescuecommandcenter.emergencycallsystem.controller;

import org.javatuples.Pair;

import whs.master.rescuecommandcenter.emergencycallsystem.dto.base.FireEmergencyDto;
import whs.master.rescuecommandcenter.emergencycallsystem.dto.base.FireMessageDto;
import whs.master.rescuecommandcenter.emergencycallsystem.dto.request.CreateFireEmergencyDto;
import whs.master.rescuecommandcenter.emergencycallsystem.dto.request.CreateFireMessageRequestDto;
import whs.master.rescuecommandcenter.emergencycallsystem.dto.request.UpdateFireEmergencyRequestDto;
import whs.master.rescuecommandcenter.emergencycallsystem.dto.response.FireEmergencyResponseDto;
import whs.master.rescuecommandcenter.emergencycallsystem.service.FireEmergencyCallService;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/emergency/fire")
public class FireEmergencyController {

    private final FireEmergencyCallService fireEmergencyCallService;

    @Autowired
    public FireEmergencyController(FireEmergencyCallService fireEmergencyCallService) {
        this.fireEmergencyCallService = fireEmergencyCallService;
    }

    @GetMapping("/health")
    public String health() {
        return "healthy";
    }

    @GetMapping
    public ResponseEntity<FireEmergencyResponseDto<List<FireEmergencyDto>>> getFireEmergencyCalls(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        FireEmergencyResponseDto<List<FireEmergencyDto>> responseDto = fireEmergencyCallService.getFireEmergencyCalls(token.substring(7).trim());

        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getFireEmergencyCalls(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
            @PathVariable long id) {
        FireEmergencyResponseDto<Pair<FireEmergencyDto, List<FireMessageDto>>> responseDto = fireEmergencyCallService.getFireEmergencyCallById(id, token.substring(7).trim());

        return ResponseEntity.ok(responseDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateFireEmergencyCalls(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
            @PathVariable long id,
            @RequestBody UpdateFireEmergencyRequestDto requestDto){

        boolean successful = fireEmergencyCallService.updateFireEmergencyCall(token.substring(7).trim(), id, requestDto);

        if(successful)
            return ResponseEntity.noContent().build();

        return ResponseEntity.internalServerError().build();
    }

    @PostMapping
    public ResponseEntity<?> createFireEmergencyCall(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
            @RequestBody CreateFireEmergencyDto createFireEmergencyDto) {
        FireEmergencyResponseDto<FireEmergencyDto> responseDto = fireEmergencyCallService.createFireEmergencyCall(createFireEmergencyDto, token.substring(7).trim());

        if(responseDto == null)
            return ResponseEntity.internalServerError().body("Failed to create fire emergency call");

        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/message")
    public ResponseEntity<?> createFireMessage(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
            @RequestBody CreateFireMessageRequestDto requestDto) {
        FireEmergencyResponseDto<FireMessageDto> responseDto = fireEmergencyCallService.createFireMessage(requestDto, token.substring(7).trim());

        if(responseDto == null)
            return ResponseEntity.internalServerError().body("Failed to create fire emergency message");

        return ResponseEntity.ok(responseDto);
    }

}
