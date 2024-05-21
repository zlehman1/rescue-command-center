package org.rescue.command.center.emergencycallsystem.service.impl;

import org.javatuples.Triplet;
import org.rescue.command.center.base.authentication.service.JwtTokenService;
import org.rescue.command.center.base.emergencycallsystem.enums.BOSOrganizationEnum;
import org.rescue.command.center.base.emergencycallsystem.model.BOSOrganization;
import org.rescue.command.center.base.emergencycallsystem.repository.BOSOrganizationRepository;
import org.rescue.command.center.base.userManagement.repository.UserRepository;
import org.rescue.command.center.emergencycallsystem.model.fire.FireEmergencyCall;
import org.rescue.command.center.emergencycallsystem.model.police.PoliceEmergencyCall;
import org.rescue.command.center.emergencycallsystem.repository.EmergencyCallStateRepository;
import org.rescue.command.center.emergencycallsystem.repository.fire.FireEmergencyCallRepository;
import org.rescue.command.center.emergencycallsystem.repository.fire.FireMessageRepository;
import org.rescue.command.center.emergencycallsystem.repository.police.PoliceEmergencyCallRepository;
import org.rescue.command.center.emergencycallsystem.service.EmergencyShareService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmergencyShareServiceImpl implements EmergencyShareService {
    private final FireEmergencyCallRepository fireEmergencyCallRepository;

    private final PoliceEmergencyCallRepository policeEmergencyCallRepository;

    private final JwtTokenService jwtTokenService;
    private final FireMessageRepository fireMessageRepository;
    private final UserRepository userRepository;
    private final EmergencyCallStateRepository emergencyCallStateRepository;
    private final BOSOrganizationRepository bosOrganizationRepository;

    public EmergencyShareServiceImpl(FireEmergencyCallRepository fireEmergencyCallRepository, PoliceEmergencyCallRepository policeEmergencyCallRepository, JwtTokenService jwtTokenService, FireMessageRepository fireMessageRepository, UserRepository userRepository, EmergencyCallStateRepository emergencyCallStateRepository, BOSOrganizationRepository bosOrganizationRepository) {
        this.fireEmergencyCallRepository = fireEmergencyCallRepository;
        this.policeEmergencyCallRepository = policeEmergencyCallRepository;
        this.jwtTokenService = jwtTokenService;
        this.fireMessageRepository = fireMessageRepository;
        this.userRepository = userRepository;
        this.emergencyCallStateRepository = emergencyCallStateRepository;
        this.bosOrganizationRepository = bosOrganizationRepository;
    }

    @Override
    public boolean shareEmergency(String token, Long emergencyId, List<Triplet<String, String, List<Long>>> data) {
        BOSOrganizationEnum userBosOrganization = jwtTokenService.extractBOSOrganizationFromToken(token);

        if (userBosOrganization.getOrganizationName().equals(BOSOrganizationEnum.FEUERWEHR.toString()))
            return shareFireEmergency(emergencyId, data);
        else
            return sharePoliceEmergency(emergencyId, data);
    }

    private boolean shareFireEmergency(Long emergencyId, List<Triplet<String, String, List<Long>>> data){
        Optional<FireEmergencyCall> fireEmergencyCall = fireEmergencyCallRepository.findById(emergencyId);

        if (fireEmergencyCall.isEmpty())
            return false;

        for (Triplet<String, String, List<Long>> triplet : data) {
            BOSOrganization bosOrganization = bosOrganizationRepository.findByName(triplet.getValue0());
            fireEmergencyCall.get().getBosOrganization().add(bosOrganization);
            fireEmergencyCallRepository.save(fireEmergencyCall.get());
        }

        return true;
    }

    private boolean sharePoliceEmergency(Long emergencyId, List<Triplet<String, String, List<Long>>> data){
        Optional<PoliceEmergencyCall> policeEmergencyCall = policeEmergencyCallRepository.findById(emergencyId);

        if (policeEmergencyCall.isEmpty())
            return false;

        for (Triplet<String, String, List<Long>> triplet : data) {
            BOSOrganization bosOrganization = bosOrganizationRepository.findByName(triplet.getValue0());
            policeEmergencyCall.get().getBosOrganization().add(bosOrganization);
            policeEmergencyCallRepository.save(policeEmergencyCall.get());
        }

        return true;
    }
}
