package whs.master.rescuecommandcenter.emergencycallsystem.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CreateFireEmergencyDto {
    private String keyword;
    private String location;
    private String information;
    private String communicatorName;
    private String communicatorPhoneNumber;
}