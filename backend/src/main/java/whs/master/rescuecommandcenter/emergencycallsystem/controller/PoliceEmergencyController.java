package whs.master.rescuecommandcenter.emergencycallsystem.controller;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

import org.javatuples.Pair;

import whs.master.rescuecommandcenter.emergencycallsystem.dto.base.PoliceEmergencyDto;
import whs.master.rescuecommandcenter.emergencycallsystem.dto.base.PoliceMessageDto;
import whs.master.rescuecommandcenter.emergencycallsystem.dto.request.CreatePoliceEmergencyDto;
import whs.master.rescuecommandcenter.emergencycallsystem.dto.request.CreatePoliceMessageRequestDto;
import whs.master.rescuecommandcenter.emergencycallsystem.dto.request.UpdatePoliceEmergencyRequestDto;
import whs.master.rescuecommandcenter.emergencycallsystem.dto.response.PoliceEmergencyResponseDto;
import whs.master.rescuecommandcenter.emergencycallsystem.service.PoliceEmergencyCallService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/emergency/police")
@Tag(name = "Police Emergency System API")
public class PoliceEmergencyController {

    private final PoliceEmergencyCallService policeEmergencyCallService;

    @Autowired
    public PoliceEmergencyController(PoliceEmergencyCallService policeEmergencyCallService) {
        this.policeEmergencyCallService = policeEmergencyCallService;
    }

    @Operation(summary = "Health check endpoint", description = "Endpoint to check the health of the Police Emergency service")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Service is healthy",
                    content = @Content)
    })
    @GetMapping("/health")
    public String health() {
        return "healthy";
    }

    @Operation(summary = "Get all police emergency calls", description = "Retrieve all police emergency calls")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved all police emergency calls",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PoliceEmergencyResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content)
    })
    @GetMapping
    public ResponseEntity<PoliceEmergencyResponseDto<List<PoliceEmergencyDto>>> getPoliceEmergencyCalls(
            @Parameter(description = "Json Web Token (JWT)") @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        PoliceEmergencyResponseDto<List<PoliceEmergencyDto>> responseDto = policeEmergencyCallService.getPoliceEmergencyCalls(token.substring(7).trim());

        return ResponseEntity.ok(responseDto);
    }

    @Operation(summary = "Get police emergency call by ID", description = "Retrieve a specific police emergency call by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the police emergency call",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PoliceEmergencyResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found",
                    content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getPoliceEmergencyCalls(
            @Parameter(description = "Json Web Token (JWT)") @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
            @Parameter(description = "ID of the police emergency call to be retrieved") @PathVariable long id) {
        PoliceEmergencyResponseDto<Pair<PoliceEmergencyDto, List<PoliceMessageDto>>> responseDto = policeEmergencyCallService.getPoliceEmergencyCallById(id, token.substring(7).trim());

        return ResponseEntity.ok(responseDto);
    }

    @Operation(summary = "Update a police emergency call", description = "Update details of a specific police emergency call by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successfully updated the police emergency call",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> updatePoliceEmergencyCalls(
            @Parameter(description = "Json Web Token (JWT)") @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
            @Parameter(description = "ID of the police emergency call to be updated") @PathVariable long id,
            @RequestBody UpdatePoliceEmergencyRequestDto requestDto){
        boolean successful = policeEmergencyCallService.updatePoliceEmergencyCall(token.substring(7).trim(), id, requestDto);

        if(successful)
            return ResponseEntity.noContent().build();

        return ResponseEntity.internalServerError().build();
    }

    @Operation(summary = "Create a new police emergency call", description = "Create a new police emergency call")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully created the police emergency call",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PoliceEmergencyResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content)
    })
    @PostMapping
    public ResponseEntity<?> createPoliceEmergencyCall(
            @Parameter(description = "Json Web Token (JWT)") @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
            @RequestBody CreatePoliceEmergencyDto createPoliceEmergencyDto) {
        PoliceEmergencyResponseDto<PoliceEmergencyDto> responseDto = policeEmergencyCallService.createPoliceEmergencyCall(createPoliceEmergencyDto, token.substring(7).trim());

        if(responseDto == null)
            return ResponseEntity.internalServerError().body("Failed to create police emergency call");

        return ResponseEntity.ok(responseDto);
    }

    @Operation(summary = "Create a new police emergency message", description = "Create a new message related to a police emergency call")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully created the police emergency message",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PoliceEmergencyResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content)
    })
    @PostMapping("/message")
    public ResponseEntity<?> createPoliceMessage(
            @Parameter(description = "Json Web Token (JWT)") @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
            @RequestBody CreatePoliceMessageRequestDto requestDto) {
        PoliceEmergencyResponseDto<PoliceMessageDto> responseDto = policeEmergencyCallService.createPoliceMessage(requestDto, token.substring(7).trim());

        if(responseDto == null)
            return ResponseEntity.internalServerError().body("Failed to create police emergency message");

        return ResponseEntity.ok(responseDto);
    }
}