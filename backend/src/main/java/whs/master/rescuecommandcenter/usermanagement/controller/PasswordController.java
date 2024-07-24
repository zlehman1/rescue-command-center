package whs.master.rescuecommandcenter.usermanagement.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import whs.master.rescuecommandcenter.usermanagement.dto.request.UpdatePasswordDto;
import whs.master.rescuecommandcenter.usermanagement.service.PasswordService;

@RestController
@RequestMapping("/api/v1/users/password")
@Tag(name = "Users API")
public class PasswordController {

    private final PasswordService passwordService;

    public PasswordController(PasswordService passwordService) {
        this.passwordService = passwordService;
    }

    @Operation(summary = "Update the password of the current user", description = "Update the password of the current user with the new password from the request body.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password updated",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content) })
    @PutMapping
    public ResponseEntity<?> updatePassword(
            @Parameter(description = "Json Web Token (JWT)") @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
            @RequestBody UpdatePasswordDto requestDto){
        if (passwordService.updatePassword(token.substring(7).trim(), requestDto))
            return ResponseEntity.ok("");
        else
            return ResponseEntity.internalServerError().build();
    }
}