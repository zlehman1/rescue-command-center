package whs.master.rescuecommandcenter.emergencycallsystem.service;

import org.javatuples.Pair;

import whs.master.rescuecommandcenter.emergencycallsystem.dto.base.PoliceEmergencyDto;
import whs.master.rescuecommandcenter.emergencycallsystem.dto.base.PoliceMessageDto;
import whs.master.rescuecommandcenter.emergencycallsystem.dto.request.CreatePoliceEmergencyDto;
import whs.master.rescuecommandcenter.emergencycallsystem.dto.request.CreatePoliceMessageRequestDto;
import whs.master.rescuecommandcenter.emergencycallsystem.dto.request.UpdatePoliceEmergencyRequestDto;
import whs.master.rescuecommandcenter.emergencycallsystem.dto.response.PoliceEmergencyResponseDto;

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

    /**
     * Updates the master data of an emergency call.
     * @param token JWT token of the requesting user
     * @param id Identifier of the emergency call
     * @param requestDto values for the update
     * @return successful? TRUE/False
     */
    boolean updatePoliceEmergencyCall(String token, long id, UpdatePoliceEmergencyRequestDto requestDto);
}
