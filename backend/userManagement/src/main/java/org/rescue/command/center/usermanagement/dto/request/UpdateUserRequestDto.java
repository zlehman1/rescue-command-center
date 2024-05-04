package org.rescue.command.center.usermanagement.dto.request;

import org.rescue.command.center.usermanagement.enums.UserUpdateType;

public class UpdateUserRequestDto {
    private UserUpdateType updateType;
    private String firstName;
    private String lastName;
    private String password;

    public UserUpdateType getUpdateType() {
        return updateType;
    }

    public void setUpdateType(UserUpdateType updateType) {
        this.updateType = updateType;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
