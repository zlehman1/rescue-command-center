package whs.master.rescuecommandcenter.emergencycallsystem.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PoliceEmergencyResponseDto<T> {
    private T data;

    public PoliceEmergencyResponseDto(T data) {
        this.data = data;
    }
}