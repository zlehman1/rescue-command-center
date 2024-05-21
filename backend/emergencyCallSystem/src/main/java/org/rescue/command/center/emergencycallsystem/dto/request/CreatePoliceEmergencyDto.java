package org.rescue.command.center.emergencycallsystem.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CreatePoliceEmergencyDto {
    private String keyword;
    private String location;
    private String information;
    private String communicatorName;
    private String communicatorPhoneNumber;
}