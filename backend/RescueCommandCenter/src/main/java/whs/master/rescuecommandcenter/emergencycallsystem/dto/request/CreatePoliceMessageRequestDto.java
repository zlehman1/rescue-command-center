package whs.master.rescuecommandcenter.emergencycallsystem.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CreatePoliceMessageRequestDto {
    private Long emergencyId;
    private String message;
}