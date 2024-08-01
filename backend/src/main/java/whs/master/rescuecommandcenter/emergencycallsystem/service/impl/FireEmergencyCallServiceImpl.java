package whs.master.rescuecommandcenter.emergencycallsystem.service.impl;

import org.javatuples.Pair;

import whs.master.rescuecommandcenter.authentication.service.JwtTokenService;

import whs.master.rescuecommandcenter.emergencycallsystem.dto.request.UpdateFireEmergencyRequestDto;
import whs.master.rescuecommandcenter.emergencycallsystem.enums.BOSOrganizationEnum;
import whs.master.rescuecommandcenter.emergencycallsystem.model.BOSOrganization;
import whs.master.rescuecommandcenter.emergencycallsystem.model.District;
import whs.master.rescuecommandcenter.emergencycallsystem.repository.BOSOrganizationRepository;

import whs.master.rescuecommandcenter.emergencycallsystem.repository.DistrictRepository;
import whs.master.rescuecommandcenter.usermanagement.model.User;
import whs.master.rescuecommandcenter.usermanagement.repository.UserRepository;

import whs.master.rescuecommandcenter.emergencycallsystem.dto.base.FireEmergencyDto;
import whs.master.rescuecommandcenter.emergencycallsystem.dto.base.FireMessageDto;
import whs.master.rescuecommandcenter.emergencycallsystem.dto.base.PoliceEmergencyDto;
import whs.master.rescuecommandcenter.emergencycallsystem.dto.request.CreateFireEmergencyDto;
import whs.master.rescuecommandcenter.emergencycallsystem.dto.request.CreateFireMessageRequestDto;
import whs.master.rescuecommandcenter.emergencycallsystem.dto.response.FireEmergencyResponseDto;
import whs.master.rescuecommandcenter.emergencycallsystem.dto.response.PoliceEmergencyResponseDto;
import whs.master.rescuecommandcenter.emergencycallsystem.enums.EmergencyCallStateEnum;
import whs.master.rescuecommandcenter.emergencycallsystem.enums.FireEmergencyCallKeyword;
import whs.master.rescuecommandcenter.emergencycallsystem.model.EmergencyCallState;
import whs.master.rescuecommandcenter.emergencycallsystem.model.fire.FireEmergencyCall;
import whs.master.rescuecommandcenter.emergencycallsystem.model.fire.FireMessage;
import whs.master.rescuecommandcenter.emergencycallsystem.model.police.PoliceEmergencyCall;
import whs.master.rescuecommandcenter.emergencycallsystem.repository.EmergencyCallStateRepository;
import whs.master.rescuecommandcenter.emergencycallsystem.repository.fire.FireEmergencyCallRepository;
import whs.master.rescuecommandcenter.emergencycallsystem.repository.fire.FireMessageRepository;
import whs.master.rescuecommandcenter.emergencycallsystem.repository.police.PoliceEmergencyCallRepository;
import whs.master.rescuecommandcenter.emergencycallsystem.service.FireEmergencyCallService;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class FireEmergencyCallServiceImpl implements FireEmergencyCallService {

    private final FireEmergencyCallRepository fireEmergencyCallRepository;

    private final PoliceEmergencyCallRepository policeEmergencyCallRepository;

    private final JwtTokenService jwtTokenService;
    private final FireMessageRepository fireMessageRepository;
    private final UserRepository userRepository;
    private final EmergencyCallStateRepository emergencyCallStateRepository;
    private final BOSOrganizationRepository bosOrganizationRepository;
    private final DistrictRepository districtRepository;

    public FireEmergencyCallServiceImpl(
            FireEmergencyCallRepository fireEmergencyCallRepository,
            PoliceEmergencyCallRepository policeEmergencyCallRepository,
            JwtTokenService jwtTokenService, FireMessageRepository fireMessageRepository, UserRepository userRepository, EmergencyCallStateRepository emergencyCallStateRepository, BOSOrganizationRepository bosOrganizationRepository, DistrictRepository districtRepository){
        this.fireEmergencyCallRepository = fireEmergencyCallRepository;
        this.policeEmergencyCallRepository = policeEmergencyCallRepository;
        this.jwtTokenService = jwtTokenService;
        this.fireMessageRepository = fireMessageRepository;
        this.userRepository = userRepository;
        this.emergencyCallStateRepository = emergencyCallStateRepository;
        this.bosOrganizationRepository = bosOrganizationRepository;
        this.districtRepository = districtRepository;
    }

    @Override
    public FireEmergencyResponseDto<List<FireEmergencyDto>> getFireEmergencyCalls(String token) {
        BOSOrganizationEnum organization = jwtTokenService.extractBOSOrganizationFromToken(token);
        String district = jwtTokenService.extractDistrictNameFromToken(token);

        if (organization.getOrganizationName().equals(BOSOrganizationEnum.NOTDEFINED.toString()))
            return null;

        List<FireEmergencyCall> emergencyCalls = fireEmergencyCallRepository.findAll();
        List<FireEmergencyCall> finalEmergencyCalls = new ArrayList<>();

        BOSOrganization bosOrganization = bosOrganizationRepository.findByName(organization.getOrganizationName());

        for (FireEmergencyCall emergencyCall : emergencyCalls) {
            if (emergencyCall.getDistrict().getName().equals(district) && emergencyCall.getEmergencyCallState().getEmergencyCallStateEnum() != EmergencyCallStateEnum.FINISHED) {
                for (BOSOrganization emergencyBosOrganization : emergencyCall.getBosOrganization()){
                    if(isSameBosOrganization(emergencyBosOrganization, bosOrganization))
                        finalEmergencyCalls.add(emergencyCall);
                }
            }
        }

        List<FireEmergencyDto> response = new ArrayList<>();

        for (FireEmergencyCall emergencyCall : finalEmergencyCalls) {
            response.add(createFireEmergencyDto(emergencyCall));
        }

        return new FireEmergencyResponseDto<>(response);
    }

    private boolean isSameBosOrganization(BOSOrganization firstBosOrganization, BOSOrganization secondBosOrganization){
        return firstBosOrganization.getName().equals(secondBosOrganization.getName());
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
        BOSOrganizationEnum organizationName = jwtTokenService.extractBOSOrganizationFromToken(token);
        BOSOrganization organization = bosOrganizationRepository.findByName(organizationName.toString());

        String districtName = jwtTokenService.extractDistrictNameFromToken(token);

        District district = districtRepository.findByName(districtName);

        if(organizationName.equals(BOSOrganizationEnum.POLIZEI) || organizationName.equals(BOSOrganizationEnum.NOTDEFINED))
            return null;

        String username = jwtTokenService.extractUsernameFromToken(token);
        User dispatcher = userRepository.findByUsername(username);

        if(dispatcher == null)
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
        fireEmergencyCall.setDispatcher(dispatcher);
        fireEmergencyCall.setEmergencyCallState(emergencyCallState);
        fireEmergencyCall.setDistrict(district);
        Set<BOSOrganization> bosOrganizations = new HashSet<>();
        bosOrganizations.add(organization);
        fireEmergencyCall.setBosOrganization(bosOrganizations);

        FireEmergencyCall savedEntity = fireEmergencyCallRepository.save(fireEmergencyCall);

        return new FireEmergencyResponseDto<>(createFireEmergencyDto(savedEntity));
    }

    @Override
    public FireEmergencyResponseDto<FireMessageDto> createFireMessage(CreateFireMessageRequestDto requestDto, String token){
        BOSOrganizationEnum organization = jwtTokenService.extractBOSOrganizationFromToken(token);

        if(organization.equals(BOSOrganizationEnum.POLIZEI) || organization.equals(BOSOrganizationEnum.NOTDEFINED))
            return null;

        String username = jwtTokenService.extractUsernameFromToken(token);

        User dispatcher = userRepository.findByUsername(username);

        if(dispatcher == null)
            return null;

        Optional<FireEmergencyCall> fireEmergencyCall = fireEmergencyCallRepository.findById(requestDto.getEmergencyId());

        if(fireEmergencyCall.isEmpty())
            return null;

        FireMessage fireMessage = new FireMessage(LocalDateTime.now(), requestDto.getMessage(), dispatcher, fireEmergencyCall.get());
        fireMessage = fireMessageRepository.save(fireMessage);
        FireMessageDto response = new FireMessageDto(fireMessage.getId(), fireMessage.getTimestamp(), fireMessage.getText(), fireMessage.getDispatcher().getUsername());
        return new FireEmergencyResponseDto<>(response);
    }

    @Override
    public boolean updateFireEmergencyCall(String token, long id, UpdateFireEmergencyRequestDto requestDto){
        if (!isValid(token, id))
            return false;

        FireEmergencyCall fireEmergencyCall = fireEmergencyCallRepository.findById(id).get();

        switch (requestDto.getNumber()){
            case 1:
                fireEmergencyCall.setLocation(requestDto.getValue());
                break;
            case 2:
                fireEmergencyCall.setCommunicatorName(requestDto.getValue());
                break;
            case 3:
                fireEmergencyCall.setCommunicatorPhoneNumber(requestDto.getValue());
                break;
            case 4:
                fireEmergencyCall.setKeyword(FireEmergencyCallKeyword.valueOf(requestDto.getValue().toUpperCase()));
                break;
            case 5:
                EmergencyCallState emergencyCallState = new EmergencyCallState();
                emergencyCallState.setEmergencyCallStateEnum(EmergencyCallStateEnum.valueOf(requestDto.getValue().toUpperCase()));
                fireEmergencyCall.setEmergencyCallState(emergencyCallState);
                break;
        }

        fireEmergencyCallRepository.save(fireEmergencyCall);

        User systemUser = userRepository.findByUsername("system");

        FireMessage fireMessage = new FireMessage(LocalDateTime.now(), requestDto.getMessage(), systemUser, fireEmergencyCall);
        fireMessageRepository.save(fireMessage);

        return true;
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

    private boolean isValid(String token, long id){
        BOSOrganizationEnum organization = jwtTokenService.extractBOSOrganizationFromToken(token);

        if(organization.equals(BOSOrganizationEnum.POLIZEI) || organization.equals(BOSOrganizationEnum.NOTDEFINED))
            return false;

        String username = jwtTokenService.extractUsernameFromToken(token);

        User dispatcher = userRepository.findByUsername(username);

        if(dispatcher == null)
            return false;

        Optional<FireEmergencyCall> fireEmergencyCall = fireEmergencyCallRepository.findById(id);

        if(fireEmergencyCall.isEmpty())
            return false;

        return true;
    }
}