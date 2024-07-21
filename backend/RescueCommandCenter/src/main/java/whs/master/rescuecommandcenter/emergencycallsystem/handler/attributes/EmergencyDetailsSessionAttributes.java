package whs.master.rescuecommandcenter.emergencycallsystem.handler.attributes;

import lombok.Getter;
import lombok.Setter;

import whs.master.rescuecommandcenter.emergencycallsystem.enums.BOSOrganizationEnum;

@Getter @Setter
public class EmergencyDetailsSessionAttributes {
    private String district;
    private BOSOrganizationEnum organization;
    private String id;

    public EmergencyDetailsSessionAttributes(String district, BOSOrganizationEnum organization, String id) {
        this.district = district;
        this.organization = organization;
        this.id = id;
    }
}