package org.rescue.command.center.base.emergencycallsystem.enums;

public enum BOSOrganizationEnum {
    NOTDEFINED("NOTDEFINED"),
    FEUERWEHR("FEUERWEHR"),
    POLIZEI("POLIZEI");

    private final String organizationName;

    BOSOrganizationEnum(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    @Override
    public String toString() {
        return organizationName;
    }
}