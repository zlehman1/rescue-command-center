package whs.master.rescuecommandcenter.usermanagement.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import whs.master.rescuecommandcenter.usermanagement.dto.request.UpdatePasswordDto;
import whs.master.rescuecommandcenter.usermanagement.service.PasswordService;

@RestController
@RequestMapping("/api/v1/users/password")
public class PasswordController {

    private final PasswordService passwordService;

    public PasswordController(PasswordService passwordService) {
        this.passwordService = passwordService;
    }

    @PutMapping
    public ResponseEntity<?> updatePassword(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
            @RequestBody UpdatePasswordDto requestDto){
        if (passwordService.updatePassword(token.substring(7).trim(), requestDto))
            return ResponseEntity.ok("");
        else
            return ResponseEntity.internalServerError().build();
    }
}