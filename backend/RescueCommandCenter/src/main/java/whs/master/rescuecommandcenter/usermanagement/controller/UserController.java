package whs.master.rescuecommandcenter.usermanagement.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import whs.master.rescuecommandcenter.usermanagement.dto.base.HttpResponseCodeDto;
import whs.master.rescuecommandcenter.usermanagement.dto.base.UserDto;
import whs.master.rescuecommandcenter.usermanagement.dto.request.CreateUserRequestDto;
import whs.master.rescuecommandcenter.usermanagement.dto.request.UpdateUserRequestDto;
import whs.master.rescuecommandcenter.usermanagement.dto.response.UserResponseDto;
import whs.master.rescuecommandcenter.usermanagement.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "Users API")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Get a user by its username", description = "Retrieve a specific user by providing the username.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the user",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserResponseDto.class)) }),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content) })
    @GetMapping("/user")
    public ResponseEntity<?> getUserById(
            @Parameter(description = "username of the user to be searched") @RequestParam String username,
            @Parameter(description = "Json Web Token (JWT)")@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        UserResponseDto responseDto = userService.getUserByUsername(username, token.substring(7).trim());

        if(responseDto.getHttpCodeDetails().getCode() == 404)
            return ResponseEntity.notFound().build();

        if(responseDto.getHttpCodeDetails() != null && responseDto.getHttpCodeDetails().getCode() == 403)
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        return ResponseEntity.ok(responseDto);
    }

    @Operation(summary = "Get all users", description = "Retrieve a list of all users.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retrieved all users",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserResponseDto.class)) }),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content) })
    @GetMapping
    public ResponseEntity<?> getAllUsers(
            @Parameter(description = "Json Web Token (JWT)") @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        UserResponseDto<List<UserDto>> responseDto = userService.getAllUsers(token.substring(7).trim());

        if(responseDto.getHttpCodeDetails() != null && responseDto.getHttpCodeDetails().getCode() == 403)
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        return ResponseEntity.ok(responseDto);
    }

    @Operation(summary = "Update a user by its username", description = "Update details of a specific user by providing the username and request body.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserResponseDto.class)) }),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content) })
    @PutMapping("/user")
    public ResponseEntity<?> updateUser(
            @Parameter(description = "username of the user to be updated") @RequestParam String username,
            @RequestBody UpdateUserRequestDto requestDto,
            @Parameter(description = "Json Web Token (JWT)") @RequestHeader(HttpHeaders.AUTHORIZATION) String token){
        UserResponseDto responseDto = userService.updateUserByUsername(username, requestDto, token.substring(7).trim());

        if(responseDto.getHttpCodeDetails().getCode() == 404)
            return ResponseEntity.notFound().build();

        if(responseDto.getHttpCodeDetails() != null && responseDto.getHttpCodeDetails().getCode() == 403)
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        return ResponseEntity.ok(responseDto);
    }

    @Operation(summary = "Create a new user", description = "Create a new user by providing the necessary details in the request body.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserResponseDto.class)) }),
            @ApiResponse(responseCode = "409", description = "Conflict - user already exists",
                    content = @Content) })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> createUser(@RequestBody CreateUserRequestDto requestDto) {
        UserResponseDto responseDto = userService.saveUser(requestDto);

        if(responseDto.getHttpCodeDetails().getCode() == 409)
            return ResponseEntity.status(HttpStatus.CONFLICT).build();

        return ResponseEntity.ok(responseDto);
    }

    @Operation(summary = "Delete a user by its username", description = "Delete a specific user by providing the username.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User deleted",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content) })
    @DeleteMapping("/user")
    public ResponseEntity<?> deleteUser(
            @Parameter(description = "username of the user to be deleted") @RequestParam String username,
            @Parameter(description = "Json Web Token (JWT)") @RequestHeader(HttpHeaders.AUTHORIZATION) String token){
        HttpResponseCodeDto responseCodeDto = userService.deleteUser(username, token.substring(7).trim());

        if (responseCodeDto.getCode() == 404)
            return ResponseEntity.notFound().build();
        else if (responseCodeDto.getCode() == 403)
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Health check endpoint", description = "Endpoint to check the health of the User Management service.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Service is healthy",
                    content = @Content) })
    @GetMapping("/health")
    public String health() {
        return "healthy";
    }
}