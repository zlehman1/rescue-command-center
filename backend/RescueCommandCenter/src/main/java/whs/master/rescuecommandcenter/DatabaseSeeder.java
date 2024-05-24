package whs.master.rescuecommandcenter;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import whs.master.rescuecommandcenter.emergencycallsystem.enums.BOSOrganizationEnum;
import whs.master.rescuecommandcenter.emergencycallsystem.model.BOSOrganization;
import whs.master.rescuecommandcenter.emergencycallsystem.model.District;
import whs.master.rescuecommandcenter.emergencycallsystem.repository.BOSOrganizationRepository;
import whs.master.rescuecommandcenter.emergencycallsystem.repository.DistrictRepository;

import whs.master.rescuecommandcenter.usermanagement.enums.RoleType;
import whs.master.rescuecommandcenter.usermanagement.model.Role;
import whs.master.rescuecommandcenter.usermanagement.model.User;
import whs.master.rescuecommandcenter.usermanagement.repository.RoleRepository;
import whs.master.rescuecommandcenter.usermanagement.repository.UserRepository;

import whs.master.rescuecommandcenter.emergencycallsystem.enums.EmergencyCallStateEnum;
import whs.master.rescuecommandcenter.emergencycallsystem.enums.FireEmergencyCallKeyword;
import whs.master.rescuecommandcenter.emergencycallsystem.enums.PoliceEmergencyCallKeyword;
import whs.master.rescuecommandcenter.emergencycallsystem.model.EmergencyCallState;
import whs.master.rescuecommandcenter.emergencycallsystem.model.fire.FireEmergencyCall;
import whs.master.rescuecommandcenter.emergencycallsystem.model.fire.FireMessage;
import whs.master.rescuecommandcenter.emergencycallsystem.model.police.PoliceEmergencyCall;
import whs.master.rescuecommandcenter.emergencycallsystem.model.police.PoliceMessage;
import whs.master.rescuecommandcenter.emergencycallsystem.repository.EmergencyCallStateRepository;
import whs.master.rescuecommandcenter.emergencycallsystem.repository.fire.FireEmergencyCallRepository;
import whs.master.rescuecommandcenter.emergencycallsystem.repository.fire.FireMessageRepository;
import whs.master.rescuecommandcenter.emergencycallsystem.repository.police.PoliceEmergencyCallRepository;
import whs.master.rescuecommandcenter.emergencycallsystem.repository.police.PoliceMessageRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final BOSOrganizationRepository bOSOrganizationRepository;
    private final FireMessageRepository fireMessageRepository;
    private final PoliceEmergencyCallRepository policeEmergencyCallRepository;
    private final PoliceMessageRepository policeMessageRepository;
    private final FireEmergencyCallRepository fireEmergencyCallRepository;
    private final EmergencyCallStateRepository emergencyCallStateRepository;
    private final DistrictRepository districtRepository;
    private final RoleRepository roleRepository;

    public DatabaseSeeder(
            UserRepository userRepository,
            BOSOrganizationRepository bOSOrganizationRepository,
            FireEmergencyCallRepository fireEmergencyCallRepository,
            FireMessageRepository fireMessageRepository,
            PoliceEmergencyCallRepository policeEmergencyCallRepository,
            PoliceMessageRepository policeMessageRepository,
            EmergencyCallStateRepository emergencyCallStateRepository,
            DistrictRepository districtRepository,
            RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.bOSOrganizationRepository = bOSOrganizationRepository;
        this.fireEmergencyCallRepository = fireEmergencyCallRepository;
        this.fireMessageRepository = fireMessageRepository;
        this.policeEmergencyCallRepository = policeEmergencyCallRepository;
        this.policeMessageRepository = policeMessageRepository;
        this.emergencyCallStateRepository = emergencyCallStateRepository;
        this.districtRepository = districtRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        createInitialData();
    }

    private void createInitialData() {
        createInitialUserData();

        User johndoe = userRepository.findByUsername("johndoe");
        User janedoe = userRepository.findByUsername("janedoe");

        User erikaMustermann = userRepository.findByUsername("erikamustermann");
        User maxMustermann = userRepository.findByUsername("maxmustermann");

        BOSOrganization feuerwehr = new BOSOrganization(BOSOrganizationEnum.FEUERWEHR.toString());
        BOSOrganization polizei = new BOSOrganization(BOSOrganizationEnum.POLIZEI.toString());

        if(bOSOrganizationRepository.findAll().isEmpty()){
            Set<User> fireUserSet = new HashSet<>();
            Set<User> policeUserSet = new HashSet<>();

            fireUserSet.add(johndoe);
            fireUserSet.add(janedoe);

            policeUserSet.add(maxMustermann);
            policeUserSet.add(erikaMustermann);

            feuerwehr.setUserSet(fireUserSet);
            polizei.setUserSet(policeUserSet);

            bOSOrganizationRepository.save(feuerwehr);
            bOSOrganizationRepository.save(polizei);
        }

        if(emergencyCallStateRepository.findAll().isEmpty()){
            EmergencyCallState createdState = new EmergencyCallState();
            createdState.setEmergencyCallStateEnum(EmergencyCallStateEnum.CREATED);
            EmergencyCallState dispatchedState = new EmergencyCallState();
            dispatchedState.setEmergencyCallStateEnum(EmergencyCallStateEnum.DISPATCHED);
            EmergencyCallState runningState = new EmergencyCallState();
            runningState.setEmergencyCallStateEnum(EmergencyCallStateEnum.RUNNING);
            EmergencyCallState completedState = new EmergencyCallState();
            completedState.setEmergencyCallStateEnum(EmergencyCallStateEnum.COMPLETED);
            EmergencyCallState finishedState = new EmergencyCallState();
            finishedState.setEmergencyCallStateEnum(EmergencyCallStateEnum.FINISHED);

            emergencyCallStateRepository.save(createdState);
            emergencyCallStateRepository.save(dispatchedState);
            emergencyCallStateRepository.save(runningState);
            emergencyCallStateRepository.save(completedState);
            emergencyCallStateRepository.save(finishedState);
        }

        if(districtRepository.findAll().isEmpty()){
            Set<User> userSetOne  = new HashSet<>();
            Set<User> userSetTwo  = new HashSet<>();

            userSetOne.add(johndoe);
            userSetOne.add(janedoe);

            userSetTwo.add(maxMustermann);
            userSetTwo.add(erikaMustermann);

            District coesfeld = new District("Coesfeld", userSetOne);
            District steinfurt = new District("Steinfurt", userSetTwo);

            District wesel = new District("Wesel");
            District borken = new District("Borken");
            District recklinghausen = new District("Recklinghausen");

            districtRepository.save(coesfeld);
            districtRepository.save(steinfurt);
            districtRepository.save(wesel);
            districtRepository.save(borken);
            districtRepository.save(recklinghausen);
        }

        Long fireEmergencyCallId = 0L;
        Long policeEmergencyCallId = 0L;

        if(fireEmergencyCallRepository.findAll().isEmpty()){
            FireEmergencyCall fireEmergencyCall = new FireEmergencyCall();
            fireEmergencyCall.setTimestamp(LocalDateTime.now());
            fireEmergencyCall.setKeyword(FireEmergencyCallKeyword.FEUER);
            fireEmergencyCall.setLocation("Rottkamp 15 48653 Coesfeld ");
            fireEmergencyCall.setInformation("Wohnungsbrand, starke Verrauchung im Treppenhaus, Flammen schlagen aus dem Fenster");
            fireEmergencyCall.setCommunicatorName("Max Mustermann");
            fireEmergencyCall.setCommunicatorPhoneNumber("+49 123 45678901");
            fireEmergencyCall.setDispatcher(johndoe);
            fireEmergencyCall.setEmergencyCallState(emergencyCallStateRepository.findByEmergencyCallStateEnum(EmergencyCallStateEnum.RUNNING));
            fireEmergencyCall.setDistrict(districtRepository.findByName("Coesfeld"));
            Set<BOSOrganization> bosOrganizations = new HashSet<>();
            bosOrganizations.add(feuerwehr);
            fireEmergencyCall.setBosOrganization(bosOrganizations);

            fireEmergencyCallId = fireEmergencyCallRepository.save(fireEmergencyCall).getId();
        }

        if(policeEmergencyCallRepository.findAll().isEmpty()){
            PoliceEmergencyCall policeEmergencyCall = new PoliceEmergencyCall();
            policeEmergencyCall.setTimestamp(LocalDateTime.now());
            policeEmergencyCall.setKeyword(PoliceEmergencyCallKeyword.LEICHE);
            policeEmergencyCall.setLocation("Liedekerker Straße 70 48565 Steinfurt");
            policeEmergencyCall.setInformation("Leiche aufgefunden, ca. 80 jahre alt");
            policeEmergencyCall.setCommunicatorName("Max Mustermann");
            policeEmergencyCall.setCommunicatorPhoneNumber("+49 123 45678901");
            policeEmergencyCall.setDispatcher(erikaMustermann);
            policeEmergencyCall.setEmergencyCallState(emergencyCallStateRepository.findByEmergencyCallStateEnum(EmergencyCallStateEnum.RUNNING));
            policeEmergencyCall.setDistrict(districtRepository.findByName("Steinfurt"));
            Set<BOSOrganization> bosOrganizations = new HashSet<>();
            bosOrganizations.add(polizei);
            policeEmergencyCall.setBosOrganization(bosOrganizations);

            policeEmergencyCallId = policeEmergencyCallRepository.save(policeEmergencyCall).getId();
        }

        if(fireMessageRepository.findAll().isEmpty()){
            FireEmergencyCall fireEmergencyCall = fireEmergencyCallRepository.findById(fireEmergencyCallId).get();

            fireMessageRepository.save(new FireMessage(LocalDateTime.now(),
                    "LST: laut EMA 4 Personen gemeldet", johndoe, fireEmergencyCall));
            fireMessageRepository.save(new FireMessage(LocalDateTime.now().plusMinutes(1),
                    "Florian Bocholt 1 - HLF 20/1: Flammenschein sichtbar, kein Mensch oder Tier in Gefahr, Brandbekämpfung mit 1 1 C-Rohr und 1 Trupp unter PA vor", johndoe, fireEmergencyCall));
            fireMessageRepository.save(new FireMessage(LocalDateTime.now().plusMinutes(2),
                    "Florian Bocholt 1 - ELW 1: Feuer unter Kontrolle", janedoe, fireEmergencyCall));
            fireMessageRepository.save(new FireMessage(LocalDateTime.now().plusMinutes(3),
                    "Florian Bocholt 1 - ELW 1: Nachlöscharbeiten laufen, 1 Person mit Verdacht auf Rauchgasintoxikation ins KH Bocholt", johndoe, fireEmergencyCall));
            fireMessageRepository.save(new FireMessage(LocalDateTime.now().plusMinutes(4),
                    "Florian Bocholt 1 - ELW 1: Feuer aus", johndoe, fireEmergencyCall));
            fireMessageRepository.save(new FireMessage(LocalDateTime.now().plusMinutes(5),
                    "Florian Bocholt 1 - ELW 1: Aufräumarbeiten im Gange", janedoe, fireEmergencyCall));
            fireMessageRepository.save(new FireMessage(LocalDateTime.now().plusMinutes(6),
                    "Florian Bocholt 1 - ELW 1: alle eingesetzten Fahrzeuge rücken wieder einsatzbereit ein", johndoe, fireEmergencyCall));
        }

        if(policeMessageRepository.findAll().isEmpty()){
            PoliceEmergencyCall policeEmergencyCall = policeEmergencyCallRepository.findById(policeEmergencyCallId).get();

            policeMessageRepository.save(new PoliceMessage(LocalDateTime.now(),
                    "LST: laut EMA 4 Personen gemeldet", erikaMustermann, policeEmergencyCall));
            policeMessageRepository.save(new PoliceMessage(LocalDateTime.now().plusMinutes(1),
                    "1121: Leiche verwest, NA Anforderung", erikaMustermann, policeEmergencyCall));
            policeMessageRepository.save(new PoliceMessage(LocalDateTime.now().plusMinutes(2),
                    "LST: Dr. Mustermann erreicht, kommt", maxMustermann, policeEmergencyCall));
            policeMessageRepository.save(new PoliceMessage(LocalDateTime.now().plusMinutes(3),
                    "1121: ungeklärte Todesursache", erikaMustermann, policeEmergencyCall));
            policeMessageRepository.save(new PoliceMessage(LocalDateTime.now().plusMinutes(4),
                    "LST: K-Wache alarmiert", erikaMustermann, policeEmergencyCall));
            policeMessageRepository.save(new PoliceMessage(LocalDateTime.now().plusMinutes(5),
                    "1121: ES an K-Wache übergeben", maxMustermann, policeEmergencyCall));
            policeMessageRepository.save(new PoliceMessage(LocalDateTime.now().plusMinutes(6),
                    "9121: ein Leichenbericht geschrieben, Bestatter hat Leichnam abgeholt", erikaMustermann, policeEmergencyCall));
        }
    }

    private void createInitialUserData(){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

        List<User> user = userRepository.findAll();

        if (user.size() >= 4)
            return;

        User user1 = new User("johndoe", "John", "Doe",encoder.encode("Password123!"));
        User user2 = new User("janedoe", "Jane", "Doe",encoder.encode("Password123!"));
        User user3 = new User("maxmustermann", "Max", "Mustermann",encoder.encode("Password123!"));
        User user4 = new User("erikamustermann", "Erika", "Mustermann",encoder.encode("Password123!"));

        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);
        userRepository.save(user4);

        Role role1 = new Role(RoleType.USER);
        Role role2 = new Role(RoleType.ADMIN);

        roleRepository.save(role1);
        roleRepository.save(role2);

        Role roleUser = roleRepository.findByRoleType(RoleType.USER);
        Role roleAdmin = roleRepository.findByRoleType(RoleType.ADMIN);

        user1 = userRepository.findById(user1.getUsername()).orElseThrow();
        user2 = userRepository.findById(user2.getUsername()).orElseThrow();
        user3 = userRepository.findById(user3.getUsername()).orElseThrow();
        user4 = userRepository.findById(user4.getUsername()).orElseThrow();

        user1.addRole(roleAdmin);
        user1.addRole(roleUser);
        user2.addRole(roleUser);
        user3.addRole(roleUser);
        user4.addRole(roleUser);

        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);
        userRepository.save(user4);
    }
}