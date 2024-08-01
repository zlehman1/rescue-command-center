package whs.master.rescuecommandcenter.usermanagement.enums;

public enum RoleType {
    USER("USER"),
    ADMIN("ADMIN"),
    DISPATCHER("DISPATCHER"),
    SYSTEM("SYSTEM");

    private String value;

    RoleType(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}