package org.rescue.command.center.emergencycallsystem;

import org.rescue.command.center.base.userManagement.model.User;
import org.rescue.command.center.base.userManagement.repository.UserRepository;
import org.rescue.command.center.emergencycallsystem.repository.BOSOrganizationRepository;

import org.springframework.boot.CommandLineRunner;

import org.springframework.stereotype.Component;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    private final UserRepository userRepository;

    private final BOSOrganizationRepository bOSOrganizationRepository;


    public DatabaseSeeder(UserRepository userRepository, BOSOrganizationRepository bOSOrganizationRepository) {
        this.userRepository = userRepository;
        this.bOSOrganizationRepository = bOSOrganizationRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        createInitialData();
    }

    private void createInitialData() {
        userRepository.findByUsername("johndoe").ifPresent(johndoe -> {
            BOSOrganization feuerwehr = new BOSOrganization("Feuerwehr");
            feuerwehr.setUserSet(johndoe);
            bOSOrganizationRepository.save(feuerwehr);
        });

        userRepository.findByUsername("janedoe").ifPresent(janedoe -> {
            BOSOrganization polizei = new BOSOrganization("Polizei");
            polizei.setUserSet(janedoe);
            bOSOrganizationRepository.save(polizei);
        });
    }
}