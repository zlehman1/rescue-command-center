package org.rescue.command.center.authentication.dto.response;

public class LoginResponseDto {
    private String jwtToken;

    public LoginResponseDto(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }
}