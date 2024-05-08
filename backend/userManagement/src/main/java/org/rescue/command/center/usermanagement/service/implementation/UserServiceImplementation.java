package org.rescue.command.center.usermanagement.service.implementation;

import org.rescue.command.center.base.authentication.service.JwtTokenService;
import org.rescue.command.center.base.userManagement.enums.RoleType;
import org.rescue.command.center.usermanagement.dto.base.HttpResponseCodeDto;
import org.rescue.command.center.usermanagement.dto.base.UserDto;
import org.rescue.command.center.usermanagement.dto.request.CreateUserRequestDto;
import org.rescue.command.center.usermanagement.dto.request.UpdateUserRequestDto;
import org.rescue.command.center.usermanagement.dto.response.UserResponseDto;
import org.rescue.command.center.usermanagement.enums.UserUpdateType;
import org.rescue.command.center.usermanagement.service.UserService;

import org.rescue.command.center.base.userManagement.model.Role;
import org.rescue.command.center.base.userManagement.model.User;
import org.rescue.command.center.base.userManagement.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImplementation implements UserService {

    private final UserRepository userRepository;
    private final JwtTokenService jwtTokenService;

    @Autowired
    public UserServiceImplementation(UserRepository userRepository, JwtTokenService jwtTokenService) {
        this.userRepository = userRepository;
        this.jwtTokenService = jwtTokenService;
    }

    @Override
    public UserResponseDto<UserDto> saveUser(CreateUserRequestDto createUserRequestDto) {

        List<User> userRepo = userRepository.findByUsername(createUserRequestDto.getUser().getUsername());

        if (userRepo.size() > 0)
            createUserResponseDto(new User(), "Username already exists!", 409);

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

        User user = new User();
        user.setUsername(createUserRequestDto.getUser().getUsername());
        user.setFirstName(createUserRequestDto.getUser().getFirstName());
        user.setLastName(createUserRequestDto.getUser().getLastName());
        user.setPassword(encoder.encode(createUserRequestDto.getPassword()));

        user = userRepository.save(user);

        return createUserResponseDto(user, "ok", 200);
    }

    @Override
    public UserResponseDto<UserDto> getUserByUsername(String username, String token) {
        List<User> users = userRepository.findByUsername(username);
        if (users.size() == 0) {
            return createUserResponseDto(new User(), String.format("No user with name '%s' found!", username), 404);
        } else if (users.size() > 1) {
            return createUserResponseDto(users.get(0), String.format("More than one user with the name '%s' found!", username), 409);
        } else {
            if (!users.get(0).getUsername().equals(jwtTokenService.extractUsernameFromToken(token)))
                return createUserResponseDto(new User(), String.format("Not allowed to access the user with username '%s'!", username), 403);
            else
                return createUserResponseDto(users.get(0),"ok", 200);
        }
    }

    @Override
    public UserResponseDto<List<UserDto>> getAllUsers(String token){
        UserResponseDto<List<UserDto>> userResponseDto = new UserResponseDto<>();

        List<UserDto> list = new ArrayList<>();

        String username = jwtTokenService.extractUsernameFromToken(token);

        List<User> users = userRepository.findByUsername(username);

        Set<Role> roles = users.get(0).getRoles();

        for (Role role : roles) {
            if (role.getName().equals(RoleType.ADMIN)){
                List<User> userList = userRepository.findAll();

                for (User user : userList) {
                    UserDto userDto = new UserDto();
                    userDto.setUsername(user.getUsername());
                    userDto.setFirstName(user.getFirstName());
                    userDto.setLastName(user.getLastName());
                    list.add(userDto);
                }

                userResponseDto.setUser(list);
                return userResponseDto;
            }
        }

        HttpResponseCodeDto httpResponseCodeDto = new HttpResponseCodeDto();
        httpResponseCodeDto.setCode(403);
        httpResponseCodeDto.setMessage("User is no admin!");

        userResponseDto.setHttpCodeDetails(httpResponseCodeDto);

        return userResponseDto;
    }

    @Override
    public UserResponseDto<UserDto> updateUserByUsername(String username, UpdateUserRequestDto requestDto, String token){
        List<User> user = userRepository.findByUsername(username);

        if(user.size() == 0)
            return createUserResponseDto(new User(), String.format("No username '%s' found!", username), 404);
        else if (!user.get(0).getUsername().equals(jwtTokenService.extractUsernameFromToken(token)))
            return createUserResponseDto(new User(), String.format("Not allowed to access the user with the username '%s'!", username), 403);

        user = userRepository.findByUsername(update(user.get(0), requestDto).getUsername());

        return createUserResponseDto(user.get(0), "ok", 200);
    }

    @Override
    public HttpResponseCodeDto deleteUser(String username, String token) {
        List<User> user = userRepository.findByUsername(username);
        HttpResponseCodeDto responseCodeDto = new HttpResponseCodeDto();

        if(user.size() == 0){
            responseCodeDto.setCode(404);
            return responseCodeDto;
        }

        Set<Role> roles = user.get(0).getRoles();

        for (Role role : roles) {
            if (role.getName().equals(RoleType.ADMIN) && !user.get(0).getUsername().equals(jwtTokenService.extractUsernameFromToken(token))){
                userRepository.delete(user.get(0));
                responseCodeDto.setCode(204);
            }
        }

        responseCodeDto.setCode(403);

        return responseCodeDto;
    }

    private UserResponseDto createUserResponseDto(User user, String errorMessage, long errorCode){
        UserResponseDto userResponseDto = new UserResponseDto();

        UserDto userDto = new UserDto();
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setUsername(user.getUsername());

        HttpResponseCodeDto httpResponseCodeDto = new HttpResponseCodeDto();
        httpResponseCodeDto.setCode(errorCode);
        httpResponseCodeDto.setMessage(errorMessage);

        userResponseDto.setUser(userDto);
        userResponseDto.setHttpCodeDetails(httpResponseCodeDto);

        return userResponseDto;
    }

    private User update(User user, UpdateUserRequestDto requestDto){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

        if(requestDto.getUpdateType() == UserUpdateType.ALL){
            user.setFirstName(requestDto.getFirstName());
            user.setLastName(requestDto.getLastName());
            user.setPassword(encoder.encode(requestDto.getPassword()));
        } else if (requestDto.getUpdateType() == UserUpdateType.USER){
            user.setFirstName(requestDto.getFirstName());
            user.setLastName(requestDto.getLastName());
        } else if (requestDto.getUpdateType() == UserUpdateType.PASSWORD){
            user.setPassword(encoder.encode(requestDto.getPassword()));
        }

        return userRepository.save(user);
    }
}