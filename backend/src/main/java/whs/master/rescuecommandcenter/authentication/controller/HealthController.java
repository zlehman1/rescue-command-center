package whs.master.rescuecommandcenter.authentication.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/authentication/health")
@Tag(name = "Authentication API")
public class HealthController {
    @Operation(summary = "Health check endpoint", description = "Endpoint to check the health of the authentication service.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Service is healthy",
                    content = @Content(mediaType = "text",
                            examples = @ExampleObject(value = "healthy")))
    })
    @GetMapping()
    public String health() {
        return "healthy";
    }
}