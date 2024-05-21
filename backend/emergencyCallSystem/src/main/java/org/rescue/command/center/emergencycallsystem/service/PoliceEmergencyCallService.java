package org.rescue.command.center.emergencycallsystem.service;

import org.javatuples.Pair;

import org.rescue.command.center.emergencycallsystem.dto.base.PoliceEmergencyDto;
import org.rescue.command.center.emergencycallsystem.dto.base.PoliceMessageDto;
import org.rescue.command.center.emergencycallsystem.dto.request.CreatePoliceEmergencyDto;
import org.rescue.command.center.emergencycallsystem.dto.request.CreatePoliceMessageRequestDto;
import org.rescue.command.center.emergencycallsystem.dto.response.PoliceEmergencyResponseDto;

import java.util.List;

public interface PoliceEmergencyCallService {
    /**
     * Gets all Police emergency calls.
     * @param token JWT token of the requesting user
     * @return List with Police emergency calls
     */
    PoliceEmergencyResponseDto<List<PoliceEmergencyDto>> getPoliceEmergencyCalls(String token);

    /**
     * Gets the Police emergency call by id.
     * @param id Unique identifier of the emergency call
     * @param token JWT token of the requesting user
     * @return requested emergency call
     */
    PoliceEmergencyResponseDto<Pair<PoliceEmergencyDto, List<PoliceMessageDto>>> getPoliceEmergencyCallById(long id, String token);

    /**
     * Create a new Police emergency call
     * @param requestDto emergency call data
     * @param token JWT token of the requesting user
     * @return requested emergency call
     */
    PoliceEmergencyResponseDto<PoliceEmergencyDto> createPoliceEmergencyCall(CreatePoliceEmergencyDto requestDto, String token);

    /**
     * Creates a new message for a Police emergency
     * @param requestDto information about the message and emergency id
     * @param token JWT token of the requesting user
     * @return new message
     */
    PoliceEmergencyResponseDto<PoliceMessageDto> createPoliceMessage(CreatePoliceMessageRequestDto requestDto, String token);
}
