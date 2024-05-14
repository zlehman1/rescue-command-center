package org.rescue.command.center.emergencycallsystem.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class FireEmergencyResponseDto<T> {
    private T data;

    public FireEmergencyResponseDto(T data) {
        this.data = data;
    }
}