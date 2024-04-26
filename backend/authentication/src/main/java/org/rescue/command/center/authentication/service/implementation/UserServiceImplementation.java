package org.rescue.command.center.authentication.service.implementation;

import org.rescue.command.center.authentication.dto.request.CreateUserRequestDto;
import org.rescue.command.center.authentication.model.User;
import org.rescue.command.center.authentication.repository.UserRepository;
import org.rescue.command.center.authentication.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImplementation implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImplementation(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public org.rescue.command.center.authentication.dto.base.User saveUser(CreateUserRequestDto createUserRequestDto) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

        User user = new User();
        user.setUsername(createUserRequestDto.getUser().getUsername());
        user.setFirstName(createUserRequestDto.getUser().getFirstName());
        user.setLastName(createUserRequestDto.getUser().getLastName());
        user.setPassword(encoder.encode(createUserRequestDto.getPassword()));

        user = userRepository.save(user);

        return createUserDto(user);
    }

    @Override
    public User findByUsername(String username) {
        List<User> users = userRepository.findByUsername(username);
        if (users.isEmpty()) {
            return null;
        } else if (users.size() > 1) {
            throw new IllegalStateException("More than one user found with username: " + username);
        } else {
            return users.get(0);
        }
    }

    private org.rescue.command.center.authentication.dto.base.User createUserDto(User user){
        org.rescue.command.center.authentication.dto.base.User userDto = new org.rescue.command.center.authentication.dto.base.User();
        userDto.setId(user.getId());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setUsername(user.getUsername());
        return userDto;
    }
}