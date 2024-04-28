package org.rescue.command.center.usermanagement.enums;

public enum RoleType {
    USER("USER"),
    ADMIN("ADMIN");

    private String value;

    RoleType(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}