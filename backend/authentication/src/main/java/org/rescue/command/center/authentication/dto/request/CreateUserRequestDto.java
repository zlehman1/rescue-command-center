package org.rescue.command.center.authentication.dto.request;

import org.rescue.command.center.authentication.dto.base.User;

public class CreateUserRequestDto {
    private User user;
    private String password;

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