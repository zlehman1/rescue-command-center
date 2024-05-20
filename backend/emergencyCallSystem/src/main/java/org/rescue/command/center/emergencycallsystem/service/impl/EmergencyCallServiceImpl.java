package org.rescue.command.center.emergencycallsystem.service.impl;

import org.javatuples.Pair;
import org.rescue.command.center.base.authentication.service.JwtTokenService;
import org.rescue.command.center.base.emergencycallsystem.enums.BOSOrganizationEnum;
import org.rescue.command.center.base.userManagement.model.User;
import org.rescue.command.center.base.userManagement.repository.UserRepository;
import org.rescue.command.center.emergencycallsystem.dto.base.FireEmergencyDto;
import org.rescue.command.center.emergencycallsystem.dto.base.FireMessageDto;
import org.rescue.command.center.emergencycallsystem.dto.base.PoliceEmergencyDto;
import org.rescue.command.center.emergencycallsystem.dto.base.PoliceMessageDto;
import org.rescue.command.center.emergencycallsystem.dto.request.CreateFireEmergencyDto;
import org.rescue.command.center.emergencycallsystem.dto.request.CreateFireMessageRequestDto;
import org.rescue.command.center.emergencycallsystem.dto.response.FireEmergencyResponseDto;
import org.rescue.command.center.emergencycallsystem.dto.response.PoliceEmergencyResponseDto;
import org.rescue.command.center.emergencycallsystem.enums.EmergencyCallStateEnum;
import org.rescue.command.center.emergencycallsystem.enums.FireEmergencyCallKeyword;
import org.rescue.command.center.emergencycallsystem.model.EmergencyCallState;
import org.rescue.command.center.emergencycallsystem.model.fire.FireEmergencyCall;
import org.rescue.command.center.emergencycallsystem.model.fire.FireMessage;
import org.rescue.command.center.emergencycallsystem.model.police.PoliceEmergencyCall;
import org.rescue.command.center.emergencycallsystem.repository.EmergencyCallStateRepository;
import org.rescue.command.center.emergencycallsystem.repository.fire.FireEmergencyCallRepository;
import org.rescue.command.center.emergencycallsystem.repository.fire.FireMessageRepository;
import org.rescue.command.center.emergencycallsystem.repository.police.PoliceEmergencyCallRepository;
import org.rescue.command.center.emergencycallsystem.service.EmergencyCallService;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EmergencyCallServiceImpl implements EmergencyCallService {

    private final FireEmergencyCallRepository fireEmergencyCallRepository;

    private final PoliceEmergencyCallRepository policeEmergencyCallRepository;

    private final JwtTokenService jwtTokenService;
    private final FireMessageRepository fireMessageRepository;
    private final UserRepository userRepository;
    private final EmergencyCallStateRepository emergencyCallStateRepository;

    public EmergencyCallServiceImpl(
            FireEmergencyCallRepository fireEmergencyCallRepository,
            PoliceEmergencyCallRepository policeEmergencyCallRepository,
            JwtTokenService jwtTokenService, FireMessageRepository fireMessageRepository, UserRepository userRepository, EmergencyCallStateRepository emergencyCallStateRepository){
        this.fireEmergencyCallRepository = fireEmergencyCallRepository;
        this.policeEmergencyCallRepository = policeEmergencyCallRepository;
        this.jwtTokenService = jwtTokenService;
        this.fireMessageRepository = fireMessageRepository;
        this.userRepository = userRepository;
        this.emergencyCallStateRepository = emergencyCallStateRepository;
    }

    @Override
    public FireEmergencyResponseDto<List<FireEmergencyDto>> getFireEmergencyCalls(String token) {
        BOSOrganizationEnum organization = jwtTokenService.extractBOSOrganizationFromToken(token);

        if(organization.equals(BOSOrganizationEnum.POLIZEI) || organization.equals(BOSOrganizationEnum.NOTDEFINED))
            return null;

        List<FireEmergencyCall> emergencyCalls = fireEmergencyCallRepository.findAll();
        List<FireEmergencyDto> response = new ArrayList<>();

        for (FireEmergencyCall emergencyCall : emergencyCalls) {
            if(emergencyCall.getEmergencyCallState().getEmergencyCallStateEnum() != EmergencyCallStateEnum.FINISHED)
                response.add(createFireEmergencyDto(emergencyCall));
        }

        return new FireEmergencyResponseDto<>(response);
    }

    @Override
    public FireEmergencyResponseDto<Pair<FireEmergencyDto, List<FireMessageDto>>> getFireEmergencyCallById(long id, String token) {
        BOSOrganizationEnum organization = jwtTokenService.extractBOSOrganizationFromToken(token);

        if(organization.equals(BOSOrganizationEnum.POLIZEI) || organization.equals(BOSOrganizationEnum.NOTDEFINED))
            return null;

        Optional<FireEmergencyCall> fireEmergencyCall = fireEmergencyCallRepository.findById(id);

        if(fireEmergencyCall.isEmpty())
            return null;

        List<FireMessage> messages = fireMessageRepository.findAll();

        List<FireMessageDto> response = new ArrayList<>();
        FireEmergencyDto fireEmergencyDto = createFireEmergencyDto(fireEmergencyCall.get());

        for (FireMessage message : messages) {
            if (message.getFireEmergencyCall().getId().equals(fireEmergencyCall.get().getId()))
                response.add(new FireMessageDto(message.getId(), message.getTimestamp(), message.getText(), message.getDispatcher().getUsername()));
        }

        return new FireEmergencyResponseDto<>(new Pair<>(fireEmergencyDto, response));
    }

    @Override
    public FireEmergencyResponseDto<FireEmergencyDto> createFireEmergencyCall(CreateFireEmergencyDto requestDto, String token){
        BOSOrganizationEnum organization = jwtTokenService.extractBOSOrganizationFromToken(token);

        if(organization.equals(BOSOrganizationEnum.POLIZEI) || organization.equals(BOSOrganizationEnum.NOTDEFINED))
            return null;

        String username = jwtTokenService.extractUsernameFromToken(token);
        Optional<User> dispatcher = userRepository.findByUsername(username);

        if(dispatcher.isEmpty())
            return null;

        EmergencyCallState emergencyCallState = emergencyCallStateRepository.findByEmergencyCallStateEnum(EmergencyCallStateEnum.CREATED);

        if(emergencyCallState == null)
            return null;

        FireEmergencyCall fireEmergencyCall = new FireEmergencyCall();

        fireEmergencyCall.setTimestamp(LocalDateTime.now());
        fireEmergencyCall.setKeyword(FireEmergencyCallKeyword.valueOf(requestDto.getKeyword().toUpperCase()));
        fireEmergencyCall.setLocation(requestDto.getLocation());
        fireEmergencyCall.setInformation(requestDto.getInformation());
        fireEmergencyCall.setCommunicatorName(requestDto.getCommunicatorName());
        fireEmergencyCall.setCommunicatorPhoneNumber(requestDto.getCommunicatorPhoneNumber());
        fireEmergencyCall.setDispatcher(dispatcher.get());
        fireEmergencyCall.setEmergencyCallState(emergencyCallState);

        FireEmergencyCall savedEntity = fireEmergencyCallRepository.save(fireEmergencyCall);

        return new FireEmergencyResponseDto<>(createFireEmergencyDto(savedEntity));
    }

    @Override
    public FireEmergencyResponseDto<FireMessageDto> createFireMessage(CreateFireMessageRequestDto requestDto, String token){
        BOSOrganizationEnum organization = jwtTokenService.extractBOSOrganizationFromToken(token);

        if(organization.equals(BOSOrganizationEnum.POLIZEI) || organization.equals(BOSOrganizationEnum.NOTDEFINED))
            return null;

        String username = jwtTokenService.extractUsernameFromToken(token);

        Optional<User> dispatcher = userRepository.findByUsername(username);
        if(dispatcher.isEmpty())
            return null;

        Optional<FireEmergencyCall> fireEmergencyCall = fireEmergencyCallRepository.findById(requestDto.getEmergencyId());

        if(fireEmergencyCall.isEmpty())
            return null;

        FireMessage fireMessage = new FireMessage(LocalDateTime.now(), requestDto.getMessage(), dispatcher.get(), fireEmergencyCall.get());
        fireMessage = fireMessageRepository.save(fireMessage);
        FireMessageDto response = new FireMessageDto(fireMessage.getId(), fireMessage.getTimestamp(), fireMessage.getText(), fireMessage.getDispatcher().getUsername());
        return new FireEmergencyResponseDto<>(response);
    }

    @Override
    public PoliceEmergencyResponseDto<List<PoliceEmergencyDto>> getPoliceEmergencyCalls(String token) {
        BOSOrganizationEnum organization = jwtTokenService.extractBOSOrganizationFromToken(token);

        if(organization.equals(BOSOrganizationEnum.FEUERWEHR) || organization.equals(BOSOrganizationEnum.NOTDEFINED))
            return null;

        List<PoliceEmergencyCall> emergencyCalls = policeEmergencyCallRepository.findAll();
        List<PoliceEmergencyDto> response = new ArrayList<>();

        for (PoliceEmergencyCall emergencyCall : emergencyCalls) {
            if(emergencyCall.getEmergencyCallState().getEmergencyCallStateEnum() != EmergencyCallStateEnum.FINISHED)
                response.add(createPoliceEmergencyDto(emergencyCall));
        }

        return new PoliceEmergencyResponseDto<>(response);
    }

    @Override
    public PoliceEmergencyCall getPoliceEmergencyCallById(long id) {
        return null;
    }

    private FireEmergencyDto createFireEmergencyDto(FireEmergencyCall emergencyCall){
        return new FireEmergencyDto(
                emergencyCall.getId(),
                emergencyCall.getTimestamp(),
                emergencyCall.getKeyword(),
                emergencyCall.getLocation(),
                emergencyCall.getInformation(),
                emergencyCall.getCommunicatorName(),
                emergencyCall.getCommunicatorPhoneNumber(),
                emergencyCall.getEmergencyCallState(),
                emergencyCall.getDispatcher().getUsername()
        );
    }

    private PoliceEmergencyDto createPoliceEmergencyDto(PoliceEmergencyCall emergencyCall){
        return new PoliceEmergencyDto(
                emergencyCall.getId(),
                emergencyCall.getTimestamp(),
                emergencyCall.getKeyword(),
                emergencyCall.getLocation(),
                emergencyCall.getInformation(),
                emergencyCall.getCommunicatorName(),
                emergencyCall.getCommunicatorPhoneNumber(),
                emergencyCall.getEmergencyCallState(),
                emergencyCall.getDispatcher().getUsername()
        );
    }
}