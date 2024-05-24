package whs.master.rescuecommandcenter.emergencycallsystem.enums;

public enum BOSOrganizationEnum {
    NOTDEFINED("NotDefined"),
    FEUERWEHR("Feuerwehr"),
    POLIZEI("Polizei");

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