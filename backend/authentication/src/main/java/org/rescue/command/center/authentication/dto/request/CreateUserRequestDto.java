package org.rescue.command.center.authentication.dto.request;

import jakarta.validation.constraints.Size;

import org.rescue.command.center.authentication.dto.base.User;

public class CreateUserRequestDto {
    private User user;
    private String password;

    @Size(min = 1, max = 255, message = "Das Passwort muss zwischen 1 bis 255 Zeichen lang sein!")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}