package org.rescue.command.center.emergencycallsystem.service.impl;

import org.javatuples.Pair;

import org.rescue.command.center.base.authentication.service.JwtTokenService;

import org.rescue.command.center.base.emergencycallsystem.enums.BOSOrganizationEnum;
import org.rescue.command.center.base.emergencycallsystem.model.BOSOrganization;
import org.rescue.command.center.base.emergencycallsystem.repository.BOSOrganizationRepository;

import org.rescue.command.center.base.userManagement.model.User;
import org.rescue.command.center.base.userManagement.repository.UserRepository;

import org.rescue.command.center.emergencycallsystem.dto.base.PoliceEmergencyDto;
import org.rescue.command.center.emergencycallsystem.dto.base.PoliceMessageDto;
import org.rescue.command.center.emergencycallsystem.dto.request.CreatePoliceEmergencyDto;
import org.rescue.command.center.emergencycallsystem.dto.request.CreatePoliceMessageRequestDto;
import org.rescue.command.center.emergencycallsystem.dto.response.PoliceEmergencyResponseDto;
import org.rescue.command.center.emergencycallsystem.enums.EmergencyCallStateEnum;
import org.rescue.command.center.emergencycallsystem.enums.PoliceEmergencyCallKeyword;
import org.rescue.command.center.emergencycallsystem.model.EmergencyCallState;
import org.rescue.command.center.emergencycallsystem.model.police.PoliceEmergencyCall;
import org.rescue.command.center.emergencycallsystem.model.police.PoliceMessage;
import org.rescue.command.center.emergencycallsystem.repository.EmergencyCallStateRepository;
import org.rescue.command.center.emergencycallsystem.repository.police.PoliceEmergencyCallRepository;
import org.rescue.command.center.emergencycallsystem.repository.police.PoliceMessageRepository;
import org.rescue.command.center.emergencycallsystem.service.PoliceEmergencyCallService;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PoliceEmergencyCallServiceImpl implements PoliceEmergencyCallService {

    private final PoliceEmergencyCallRepository policeEmergencyCallRepository;

    private final JwtTokenService jwtTokenService;
    private final PoliceMessageRepository policeMessageRepository;
    private final UserRepository userRepository;
    private final EmergencyCallStateRepository emergencyCallStateRepository;
    private final BOSOrganizationRepository bosOrganizationRepository;

    public PoliceEmergencyCallServiceImpl(
            PoliceEmergencyCallRepository policeEmergencyCallRepository,
            JwtTokenService jwtTokenService, PoliceMessageRepository policeMessageRepository, UserRepository userRepository, EmergencyCallStateRepository emergencyCallStateRepository, BOSOrganizationRepository bosOrganizationRepository){
        this.policeEmergencyCallRepository = policeEmergencyCallRepository;
        this.jwtTokenService = jwtTokenService;
        this.policeMessageRepository = policeMessageRepository;
        this.userRepository = userRepository;
        this.emergencyCallStateRepository = emergencyCallStateRepository;
        this.bosOrganizationRepository = bosOrganizationRepository;
    }

    @Override
    public PoliceEmergencyResponseDto<List<PoliceEmergencyDto>> getPoliceEmergencyCalls(String token) {
        BOSOrganizationEnum organization = jwtTokenService.extractBOSOrganizationFromToken(token);
        String district = jwtTokenService.extractDistrictNameFromToken(token);

        if (organization.getOrganizationName().equals(BOSOrganizationEnum.NOTDEFINED.toString()))
            return null;

        List<PoliceEmergencyCall> emergencyCalls = policeEmergencyCallRepository.findAll();
        List<PoliceEmergencyCall> finalEmergencyCalls = new ArrayList<>();

        BOSOrganization bosOrganization = bosOrganizationRepository.findByName(organization.getOrganizationName());

        for (PoliceEmergencyCall emergencyCall : emergencyCalls) {
            if (emergencyCall.getDistrict().getName().equals(district) && emergencyCall.getEmergencyCallState().getEmergencyCallStateEnum() != EmergencyCallStateEnum.FINISHED) {
                for (BOSOrganization emergencyBosOrganization : emergencyCall.getBosOrganization()){
                    if(isSameBosOrganization(emergencyBosOrganization, bosOrganization))
                        finalEmergencyCalls.add(emergencyCall);
                }
            }
        }

        List<PoliceEmergencyDto> response = new ArrayList<>();

        for (PoliceEmergencyCall emergencyCall : finalEmergencyCalls) {
            response.add(createPoliceEmergencyDto(emergencyCall));
        }

        return new PoliceEmergencyResponseDto<>(response);
    }

    private boolean isSameBosOrganization(BOSOrganization firstBosOrganization, BOSOrganization secondBosOrganization){
        return firstBosOrganization.getName().equals(secondBosOrganization.getName());
    }

    @Override
    public PoliceEmergencyResponseDto<Pair<PoliceEmergencyDto, List<PoliceMessageDto>>> getPoliceEmergencyCallById(long id, String token) {
        BOSOrganizationEnum organization = jwtTokenService.extractBOSOrganizationFromToken(token);

        if(organization.equals(BOSOrganizationEnum.FEUERWEHR) || organization.equals(BOSOrganizationEnum.NOTDEFINED))
            return null;

        Optional<PoliceEmergencyCall> policeEmergencyCall = policeEmergencyCallRepository.findById(id);

        if(policeEmergencyCall.isEmpty())
            return null;

        List<PoliceMessage> messages = policeMessageRepository.findAll();

        List<PoliceMessageDto> response = new ArrayList<>();
        PoliceEmergencyDto PoliceEmergencyDto = createPoliceEmergencyDto(policeEmergencyCall.get());

        for (PoliceMessage message : messages) {
            if (message.getPoliceEmergencyCall().getId().equals(policeEmergencyCall.get().getId()))
                response.add(new PoliceMessageDto(message.getId(), message.getTimestamp(), message.getText(), message.getDispatcher().getUsername()));
        }

        return new PoliceEmergencyResponseDto<>(new Pair<>(PoliceEmergencyDto, response));
    }

    @Override
    public PoliceEmergencyResponseDto<PoliceEmergencyDto> createPoliceEmergencyCall(CreatePoliceEmergencyDto requestDto, String token){
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

        PoliceEmergencyCall PoliceEmergencyCall = new PoliceEmergencyCall();

        PoliceEmergencyCall.setTimestamp(LocalDateTime.now());
        PoliceEmergencyCall.setKeyword(PoliceEmergencyCallKeyword.valueOf(requestDto.getKeyword().toUpperCase()));
        PoliceEmergencyCall.setLocation(requestDto.getLocation());
        PoliceEmergencyCall.setInformation(requestDto.getInformation());
        PoliceEmergencyCall.setCommunicatorName(requestDto.getCommunicatorName());
        PoliceEmergencyCall.setCommunicatorPhoneNumber(requestDto.getCommunicatorPhoneNumber());
        PoliceEmergencyCall.setDispatcher(dispatcher.get());
        PoliceEmergencyCall.setEmergencyCallState(emergencyCallState);

        PoliceEmergencyCall savedEntity = policeEmergencyCallRepository.save(PoliceEmergencyCall);

        return new PoliceEmergencyResponseDto<>(createPoliceEmergencyDto(savedEntity));
    }

    @Override
    public PoliceEmergencyResponseDto<PoliceMessageDto> createPoliceMessage(CreatePoliceMessageRequestDto requestDto, String token){
        BOSOrganizationEnum organization = jwtTokenService.extractBOSOrganizationFromToken(token);

        if(organization.equals(BOSOrganizationEnum.POLIZEI) || organization.equals(BOSOrganizationEnum.NOTDEFINED))
            return null;

        String username = jwtTokenService.extractUsernameFromToken(token);

        Optional<User> dispatcher = userRepository.findByUsername(username);
        if(dispatcher.isEmpty())
            return null;

        Optional<PoliceEmergencyCall> PoliceEmergencyCall = policeEmergencyCallRepository.findById(requestDto.getEmergencyId());

        if(PoliceEmergencyCall.isEmpty())
            return null;

        PoliceMessage PoliceMessage = new PoliceMessage(LocalDateTime.now(), requestDto.getMessage(), dispatcher.get(), PoliceEmergencyCall.get());
        PoliceMessage = policeMessageRepository.save(PoliceMessage);
        PoliceMessageDto response = new PoliceMessageDto(PoliceMessage.getId(), PoliceMessage.getTimestamp(), PoliceMessage.getText(), PoliceMessage.getDispatcher().getUsername());
        return new PoliceEmergencyResponseDto<>(response);
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