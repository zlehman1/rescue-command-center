package whs.master.rescuecommandcenter.emergencycallsystem.dto.base;

import lombok.Getter;
import lombok.Setter;

import whs.master.rescuecommandcenter.emergencycallsystem.enums.PoliceEmergencyCallKeyword;
import whs.master.rescuecommandcenter.emergencycallsystem.model.EmergencyCallState;

import java.time.LocalDateTime;

@Getter @Setter
public class PoliceEmergencyDto {
    private Long id;
    private LocalDateTime timestamp;
    private PoliceEmergencyCallKeyword keyword;
    private String location;
    private String information;
    private String communicatorName;
    private String communicatorPhoneNumber;
    private EmergencyCallState emergencyCallState;
    private String dispatcherUsername;

    public PoliceEmergencyDto(
            Long id,
            LocalDateTime timestamp,
            PoliceEmergencyCallKeyword keyword,
            String location,
            String information,
            String communicatorName,
            String communicatorPhoneNumber,
            EmergencyCallState emergencyCallState,
            String dispatcherUsername) {
        this.id = id;
        this.timestamp = timestamp;
        this.keyword = keyword;
        this.location = location;
        this.information = information;
        this.communicatorName = communicatorName;
        this.communicatorPhoneNumber = communicatorPhoneNumber;
        this.emergencyCallState = emergencyCallState;
        this.dispatcherUsername = dispatcherUsername;
    }
}
