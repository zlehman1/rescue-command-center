package whs.master.rescuecommandcenter.emergencycallsystem.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

import whs.master.rescuecommandcenter.emergencycallsystem.service.FireEmergencyCallService;

@RestController
@RequestMapping("/api/v1/emergency/share")
@Tag(name = "Emergency Sharing API", description = "Operations for sharing emergency calls")
public class ShareEmergencyController {

    private final FireEmergencyCallService fireEmergencyCallService;

    @Autowired
    public ShareEmergencyController(FireEmergencyCallService fireEmergencyCallService) {
        this.fireEmergencyCallService = fireEmergencyCallService;
    }

    @Operation(summary = "Health check endpoint", description = "Endpoint to check the health of the sharing emergency service.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Service is healthy",
                    content = @Content(mediaType = "text",
                            examples = @ExampleObject(value = "healthy")))
    })
    @GetMapping("/health")
    public String health() {
        return "healthy";
    }
}