package whs.master.rescuecommandcenter.emergencycallsystem.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UpdateFireEmergencyRequestDto {
    private int number;
    private String value;
    private String message;
}