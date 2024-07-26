package whs.master.rescuecommandcenter.emergencycallsystem.handler.attributes;

import lombok.Getter;
import lombok.Setter;

import whs.master.rescuecommandcenter.emergencycallsystem.enums.BOSOrganizationEnum;

/**
 * This class contains the websocket session attributes for the emergency system messages.
 */
@Getter @Setter
public class EmergencyDetailsSessionAttributes {
    /**
     * Contains the district name of the session.
     */
    private String district;

    /**
     * Contains the name of the BOS Organization of the session.
     */
    private BOSOrganizationEnum organization;

    /**
     * Contains the unique identifier of the emergency of the session.
     */
    private String emergencyId;

    public EmergencyDetailsSessionAttributes(String district, BOSOrganizationEnum organization, String emergencyId) {
        this.district = district;
        this.organization = organization;
        this.emergencyId = emergencyId;
    }
}