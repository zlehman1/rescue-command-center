package org.rescue.command.center.usermanagement.dto.request;

import org.rescue.command.center.usermanagement.dto.base.UserDto;

public class CreateUserRequestDto {
    private UserDto userDto;
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserDto getUser() {
        return userDto;
    }

    public void setUser(UserDto userDto) {
        this.userDto = userDto;
    }
}