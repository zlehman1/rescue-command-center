package whs.master.rescuecommandcenter.emergencycallsystem.controller;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

import org.javatuples.Pair;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;

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

import java.util.List;

@RestController
@RequestMapping("/api/v1/emergency/fire")
@Tag(name = "Fire Emergency System API")
public class FireEmergencyController {

    private final FireEmergencyCallService fireEmergencyCallService;

    @Autowired
    public FireEmergencyController(FireEmergencyCallService fireEmergencyCallService) {
        this.fireEmergencyCallService = fireEmergencyCallService;
    }

    @Operation(summary = "Health check endpoint", description = "Endpoint to check the health of the Fire Emergency service")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Service is healthy",
                    content = @Content)
    })
    @GetMapping("/health")
    public String health() {
        return "healthy";
    }

    @Operation(summary = "Get all fire emergency calls", description = "Retrieve all fire emergency calls")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved all fire emergency calls",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = FireEmergencyResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content)
    })
    @GetMapping
    public ResponseEntity<FireEmergencyResponseDto<List<FireEmergencyDto>>> getFireEmergencyCalls(
            @Parameter(description = "Json Web Token (JWT)") @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        FireEmergencyResponseDto<List<FireEmergencyDto>> responseDto = fireEmergencyCallService.getFireEmergencyCalls(token.substring(7).trim());

        return ResponseEntity.ok(responseDto);
    }

    @Operation(summary = "Get fire emergency call by ID", description = "Retrieve a specific fire emergency call by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the fire emergency call",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = FireEmergencyResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found",
                    content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getFireEmergencyCalls(
            @Parameter(description = "Json Web Token (JWT)") @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
            @Parameter(description = "ID of the fire emergency call to be retrieved") @PathVariable long id) {
        FireEmergencyResponseDto<Pair<FireEmergencyDto, List<FireMessageDto>>> responseDto = fireEmergencyCallService.getFireEmergencyCallById(id, token.substring(7).trim());

        return ResponseEntity.ok(responseDto);
    }

    @Operation(summary = "Update a fire emergency call", description = "Update details of a specific fire emergency call by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successfully updated the fire emergency call",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateFireEmergencyCalls(
            @Parameter(description = "Json Web Token (JWT)") @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
            @Parameter(description = "ID of the fire emergency call to be updated") @PathVariable long id,
            @RequestBody UpdateFireEmergencyRequestDto requestDto){

        boolean successful = fireEmergencyCallService.updateFireEmergencyCall(token.substring(7).trim(), id, requestDto);

        if(successful)
            return ResponseEntity.noContent().build();

        return ResponseEntity.internalServerError().build();
    }

    @Operation(summary = "Create a new fire emergency call", description = "Create a new fire emergency call")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully created the fire emergency call",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = FireEmergencyResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content)
    })
    @PostMapping
    public ResponseEntity<?> createFireEmergencyCall(
            @Parameter(description = "Json Web Token (JWT)") @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
            @RequestBody CreateFireEmergencyDto createFireEmergencyDto) {
        FireEmergencyResponseDto<FireEmergencyDto> responseDto = fireEmergencyCallService.createFireEmergencyCall(createFireEmergencyDto, token.substring(7).trim());

        if(responseDto == null)
            return ResponseEntity.internalServerError().body("Failed to create fire emergency call");

        return ResponseEntity.ok(responseDto);
    }

    @Operation(summary = "Create a new fire emergency message", description = "Create a new message related to a fire emergency call")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully created the fire emergency message",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = FireEmergencyResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content)
    })
    @PostMapping("/message")
    public ResponseEntity<?> createFireMessage(
            @Parameter(description = "Json Web Token (JWT)") @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
            @RequestBody CreateFireMessageRequestDto requestDto) {
        FireEmergencyResponseDto<FireMessageDto> responseDto = fireEmergencyCallService.createFireMessage(requestDto, token.substring(7).trim());

        if(responseDto == null)
            return ResponseEntity.internalServerError().body("Failed to create fire emergency message");

        return ResponseEntity.ok(responseDto);
    }
}