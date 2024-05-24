package whs.master.rescuecommandcenter.emergencycallsystem.service;

import org.javatuples.Pair;

import whs.master.rescuecommandcenter.emergencycallsystem.dto.base.FireEmergencyDto;
import whs.master.rescuecommandcenter.emergencycallsystem.dto.base.FireMessageDto;
import whs.master.rescuecommandcenter.emergencycallsystem.dto.base.PoliceEmergencyDto;
import whs.master.rescuecommandcenter.emergencycallsystem.dto.request.CreateFireEmergencyDto;
import whs.master.rescuecommandcenter.emergencycallsystem.dto.request.CreateFireMessageRequestDto;
import whs.master.rescuecommandcenter.emergencycallsystem.dto.response.FireEmergencyResponseDto;
import whs.master.rescuecommandcenter.emergencycallsystem.dto.response.PoliceEmergencyResponseDto;
import whs.master.rescuecommandcenter.emergencycallsystem.model.police.PoliceEmergencyCall;

import java.util.List;

public interface FireEmergencyCallService {
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
    FireEmergencyResponseDto<Pair<FireEmergencyDto, List<FireMessageDto>>> getFireEmergencyCallById(long id, String token);

    /**
     * Create a new fire emergency call
     * @param requestDto emergency call data
     * @param token JWT token of the requesting user
     * @return requested emergency call
     */
    FireEmergencyResponseDto<FireEmergencyDto> createFireEmergencyCall(CreateFireEmergencyDto requestDto, String token);

    /**
     * Creates a new message for a fire emergency
     * @param requestDto information about the message and emergency id
     * @param token JWT token of the requesting user
     * @return new message
     */
    FireEmergencyResponseDto<FireMessageDto> createFireMessage(CreateFireMessageRequestDto requestDto, String token);

    /**
     * Gets all police emergency calls.
     * @return List with police emergency calls
     */
    PoliceEmergencyResponseDto<List<PoliceEmergencyDto>> getPoliceEmergencyCalls(String token);

    /**
     * Gets the police emergency call by id.
     * @param id Unique identifier of the emergency call
     * @return PoliceEmergencyCall
     */
    PoliceEmergencyCall getPoliceEmergencyCallById(long id);
}
