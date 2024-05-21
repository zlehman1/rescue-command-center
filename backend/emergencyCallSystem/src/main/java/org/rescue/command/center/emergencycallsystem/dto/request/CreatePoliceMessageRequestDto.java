package org.rescue.command.center.emergencycallsystem.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CreatePoliceMessageRequestDto {
    private Long emergencyId;
    private String message;
}