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

    @Operation(summary = "Get a user by its username")
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

    @GetMapping
    public ResponseEntity<?> getAllUsers(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        UserResponseDto<List<UserDto>> responseDto = userService.getAllUsers(token.substring(7).trim());

        if(responseDto.getHttpCodeDetails() != null && responseDto.getHttpCodeDetails().getCode() == 403)
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        return ResponseEntity.ok(responseDto);
    }

    @PutMapping("/user")
    public ResponseEntity<?> updateUser(
            @RequestParam String username,
            @RequestBody UpdateUserRequestDto requestDto,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token){
        UserResponseDto responseDto = userService.updateUserByUsername(username, requestDto, token.substring(7).trim());

        if(responseDto.getHttpCodeDetails().getCode() == 404)
            return ResponseEntity.notFound().build();

        if(responseDto.getHttpCodeDetails() != null && responseDto.getHttpCodeDetails().getCode() == 403)
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        return ResponseEntity.ok(responseDto);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> createUser(@RequestBody CreateUserRequestDto requestDto) {
        UserResponseDto responseDto = userService.saveUser(requestDto);

        if(responseDto.getHttpCodeDetails().getCode() == 409)
            return ResponseEntity.status(HttpStatus.CONFLICT).build();

        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/user")
    public ResponseEntity<?> deleteUser(
            @RequestParam String username,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token){
        HttpResponseCodeDto responseCodeDto = userService.deleteUser(username, token.substring(7).trim());

        if (responseCodeDto.getCode() == 404)
            return ResponseEntity.notFound().build();
        else if (responseCodeDto.getCode() == 403)
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/health")
    public String health() {
        return "healthy";
    }
}