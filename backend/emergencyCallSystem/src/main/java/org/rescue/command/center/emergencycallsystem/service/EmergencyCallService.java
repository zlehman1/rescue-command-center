package org.rescue.command.center.emergencycallsystem.service;

import org.rescue.command.center.emergencycallsystem.dto.base.FireEmergencyDto;
import org.rescue.command.center.emergencycallsystem.dto.request.CreateFireEmergencyDto;
import org.rescue.command.center.emergencycallsystem.dto.response.FireEmergencyResponseDto;
import org.rescue.command.center.emergencycallsystem.model.police.PoliceEmergencyCall;

import java.util.List;

public interface EmergencyCallService {
    /**
     * Gets all fire emergency calls.
     * @param token JWT token of the requesting user
     * @return List with fire emergency calls
     */
    FireEmergencyResponseDto<List<FireEmergencyDto>> getFireEmergencyCalls(String token);

    /**
     * Gets the fire emergency call by id.
     * @param id Unique identifier of the emergency call
     * @param token JWT token of the requesting user
     * @return requested emergency call
     */
    FireEmergencyResponseDto<FireEmergencyDto> getFireEmergencyCallById(long id, String token);

    /**
     * Create a new fire emergency call
     * @param requestDto emergency call data
     * @param token JWT token of the requesting user
     * @return requested emergency call
     */
    FireEmergencyResponseDto<FireEmergencyDto> createFireEmergencyCall(CreateFireEmergencyDto requestDto, String token);

    /**
     * Gets all police emergency calls.
     * @return List with police emergency calls
     */
    List<PoliceEmergencyCall> getPoliceEmergencyCalls();

    /**
     * Gets the police emergency call by id.
     * @param id Unique identifier of the emergency call
     * @return PoliceEmergencyCall
     */
    PoliceEmergencyCall getPoliceEmergencyCallById(long id);
}
