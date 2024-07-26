package whs.master.rescuecommandcenter.usermanagement.enums;

public enum RoleType {
    USER("USER"),
    ADMIN("ADMIN"),
    DISPATCHER("DISPATCHER");

    private String value;

    RoleType(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}