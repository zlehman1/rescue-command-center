package whs.master.rescuecommandcenter.emergencycallsystem.service.impl;

import org.javatuples.Pair;
import whs.master.rescuecommandcenter.authentication.service.JwtTokenService;

import whs.master.rescuecommandcenter.emergencycallsystem.dto.request.UpdatePoliceEmergencyRequestDto;
import whs.master.rescuecommandcenter.emergencycallsystem.enums.BOSOrganizationEnum;
import whs.master.rescuecommandcenter.emergencycallsystem.model.BOSOrganization;
import whs.master.rescuecommandcenter.emergencycallsystem.model.District;
import whs.master.rescuecommandcenter.emergencycallsystem.repository.BOSOrganizationRepository;

import whs.master.rescuecommandcenter.emergencycallsystem.repository.DistrictRepository;
import whs.master.rescuecommandcenter.usermanagement.model.User;
import whs.master.rescuecommandcenter.usermanagement.repository.UserRepository;

import whs.master.rescuecommandcenter.emergencycallsystem.dto.base.PoliceEmergencyDto;
import whs.master.rescuecommandcenter.emergencycallsystem.dto.base.PoliceMessageDto;
import whs.master.rescuecommandcenter.emergencycallsystem.dto.request.CreatePoliceEmergencyDto;
import whs.master.rescuecommandcenter.emergencycallsystem.dto.request.CreatePoliceMessageRequestDto;
import whs.master.rescuecommandcenter.emergencycallsystem.dto.response.PoliceEmergencyResponseDto;
import whs.master.rescuecommandcenter.emergencycallsystem.enums.EmergencyCallStateEnum;
import whs.master.rescuecommandcenter.emergencycallsystem.enums.PoliceEmergencyCallKeyword;
import whs.master.rescuecommandcenter.emergencycallsystem.model.EmergencyCallState;
import whs.master.rescuecommandcenter.emergencycallsystem.model.police.PoliceEmergencyCall;
import whs.master.rescuecommandcenter.emergencycallsystem.model.police.PoliceMessage;
import whs.master.rescuecommandcenter.emergencycallsystem.repository.EmergencyCallStateRepository;
import whs.master.rescuecommandcenter.emergencycallsystem.repository.police.PoliceEmergencyCallRepository;
import whs.master.rescuecommandcenter.emergencycallsystem.repository.police.PoliceMessageRepository;
import whs.master.rescuecommandcenter.emergencycallsystem.service.PoliceEmergencyCallService;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class PoliceEmergencyCallServiceImpl implements PoliceEmergencyCallService {

    private final PoliceEmergencyCallRepository policeEmergencyCallRepository;

    private final JwtTokenService jwtTokenService;
    private final PoliceMessageRepository policeMessageRepository;
    private final UserRepository userRepository;
    private final EmergencyCallStateRepository emergencyCallStateRepository;
    private final BOSOrganizationRepository bosOrganizationRepository;
    private final DistrictRepository districtRepository;

    public PoliceEmergencyCallServiceImpl(
            PoliceEmergencyCallRepository policeEmergencyCallRepository,
            JwtTokenService jwtTokenService,
            PoliceMessageRepository policeMessageRepository,
            UserRepository userRepository,
            EmergencyCallStateRepository emergencyCallStateRepository,
            BOSOrganizationRepository bosOrganizationRepository,
            DistrictRepository districtRepository){
        this.policeEmergencyCallRepository = policeEmergencyCallRepository;
        this.jwtTokenService = jwtTokenService;
        this.policeMessageRepository = policeMessageRepository;
        this.userRepository = userRepository;
        this.emergencyCallStateRepository = emergencyCallStateRepository;
        this.bosOrganizationRepository = bosOrganizationRepository;
        this.districtRepository = districtRepository;
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
        BOSOrganizationEnum organizationName = jwtTokenService.extractBOSOrganizationFromToken(token);
        BOSOrganization organization = bosOrganizationRepository.findByName(organizationName.toString());

        String districtName = jwtTokenService.extractDistrictNameFromToken(token);

        District district = districtRepository.findByName(districtName);

        if(organization.equals(BOSOrganizationEnum.FEUERWEHR) || organization.equals(BOSOrganizationEnum.NOTDEFINED))
            return null;

        String username = jwtTokenService.extractUsernameFromToken(token);
        User dispatcher = userRepository.findByUsername(username);

        if(dispatcher == null)
            return null;

        EmergencyCallState emergencyCallState = emergencyCallStateRepository.findByEmergencyCallStateEnum(EmergencyCallStateEnum.CREATED);

        if(emergencyCallState == null)
            return null;

        PoliceEmergencyCall policeEmergencyCall = new PoliceEmergencyCall();

        policeEmergencyCall.setTimestamp(LocalDateTime.now());
        policeEmergencyCall.setKeyword(PoliceEmergencyCallKeyword.valueOf(requestDto.getKeyword().toUpperCase()));
        policeEmergencyCall.setLocation(requestDto.getLocation());
        policeEmergencyCall.setInformation(requestDto.getInformation());
        policeEmergencyCall.setCommunicatorName(requestDto.getCommunicatorName());
        policeEmergencyCall.setCommunicatorPhoneNumber(requestDto.getCommunicatorPhoneNumber());
        policeEmergencyCall.setDispatcher(dispatcher);
        policeEmergencyCall.setEmergencyCallState(emergencyCallState);
        policeEmergencyCall.setDistrict(district);
        Set<BOSOrganization> bosOrganizations = new HashSet<>();
        bosOrganizations.add(organization);
        policeEmergencyCall.setBosOrganization(bosOrganizations);

        PoliceEmergencyCall savedEntity = policeEmergencyCallRepository.save(policeEmergencyCall);

        return new PoliceEmergencyResponseDto<>(createPoliceEmergencyDto(savedEntity));
    }

    @Override
    public PoliceEmergencyResponseDto<PoliceMessageDto> createPoliceMessage(CreatePoliceMessageRequestDto requestDto, String token){
        BOSOrganizationEnum organization = jwtTokenService.extractBOSOrganizationFromToken(token);

        if(organization.equals(BOSOrganizationEnum.FEUERWEHR) || organization.equals(BOSOrganizationEnum.NOTDEFINED))
            return null;

        String username = jwtTokenService.extractUsernameFromToken(token);

        User dispatcher = userRepository.findByUsername(username);

        if(dispatcher == null)
            return null;

        Optional<PoliceEmergencyCall> PoliceEmergencyCall = policeEmergencyCallRepository.findById(requestDto.getEmergencyId());

        if(PoliceEmergencyCall.isEmpty())
            return null;

        PoliceMessage PoliceMessage = new PoliceMessage(LocalDateTime.now(), requestDto.getMessage(), dispatcher, PoliceEmergencyCall.get());
        PoliceMessage = policeMessageRepository.save(PoliceMessage);
        PoliceMessageDto response = new PoliceMessageDto(PoliceMessage.getId(), PoliceMessage.getTimestamp(), PoliceMessage.getText(), PoliceMessage.getDispatcher().getUsername());
        return new PoliceEmergencyResponseDto<>(response);
    }

    @Override
    public boolean updatePoliceEmergencyCall(String token, long id, UpdatePoliceEmergencyRequestDto requestDto){
        if (!isValid(token, id))
            return false;

        PoliceEmergencyCall policeEmergencyCall = policeEmergencyCallRepository.findById(id).get();

        switch (requestDto.getNumber()){
            case 1:
                policeEmergencyCall.setLocation(requestDto.getValue());
                break;
            case 2:
                policeEmergencyCall.setCommunicatorName(requestDto.getValue());
                break;
            case 3:
                policeEmergencyCall.setCommunicatorPhoneNumber(requestDto.getValue());
                break;
            case 4:
                policeEmergencyCall.setKeyword(PoliceEmergencyCallKeyword.valueOf(requestDto.getValue().toUpperCase()));
                break;
        }

        policeEmergencyCallRepository.save(policeEmergencyCall);

        return true;
    }

    private boolean isValid(String token, long id){
        BOSOrganizationEnum organization = jwtTokenService.extractBOSOrganizationFromToken(token);

        if(organization.equals(BOSOrganizationEnum.FEUERWEHR) || organization.equals(BOSOrganizationEnum.NOTDEFINED))
            return false;

        String username = jwtTokenService.extractUsernameFromToken(token);

        User dispatcher = userRepository.findByUsername(username);

        if(dispatcher == null)
            return false;

        Optional<PoliceEmergencyCall> policeEmergencyCall = policeEmergencyCallRepository.findById(id);

        if(policeEmergencyCall.isEmpty())
            return false;

        return true;
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