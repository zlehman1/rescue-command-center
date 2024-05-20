package org.rescue.command.center.usermanagement;

import org.rescue.command.center.base.userManagement.enums.RoleType;
import org.rescue.command.center.base.userManagement.model.Role;
import org.rescue.command.center.base.userManagement.model.User;
import org.rescue.command.center.base.userManagement.repository.RoleRepository;
import org.rescue.command.center.base.userManagement.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;


    public DatabaseSeeder(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        createInitialData();
    }

    private void createInitialData(){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

        List<User> user = userRepository.findAll();

        if (user.size() > 4)
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

        Role roleUser = roleRepository.findByName(RoleType.USER);
        Role roleAdmin = roleRepository.findByName(RoleType.ADMIN);

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