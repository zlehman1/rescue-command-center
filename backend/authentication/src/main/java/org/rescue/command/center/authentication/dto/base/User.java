package org.rescue.command.center.authentication.dto.base;

import jakarta.validation.constraints.Size;

public class User {
    private Long id;
    private String username;
    private String firstName;
    private String lastName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Size(min = 1, max = 255, message = "Der Benutzername muss zwischen 1 bis 255 Zeichen lang sein!")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
}
