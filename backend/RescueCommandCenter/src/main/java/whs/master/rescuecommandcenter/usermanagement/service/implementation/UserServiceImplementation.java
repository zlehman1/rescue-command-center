package whs.master.rescuecommandcenter.usermanagement.service.implementation;

import org.apache.logging.log4j.LogManager;

import whs.master.rescuecommandcenter.authentication.service.JwtTokenService;
import whs.master.rescuecommandcenter.usermanagement.enums.RoleType;
import whs.master.rescuecommandcenter.usermanagement.model.Role;
import whs.master.rescuecommandcenter.usermanagement.model.User;
import whs.master.rescuecommandcenter.usermanagement.model.UserState;
import whs.master.rescuecommandcenter.usermanagement.repository.UserRepository;
import whs.master.rescuecommandcenter.usermanagement.dto.base.HttpResponseCodeDto;
import whs.master.rescuecommandcenter.usermanagement.dto.base.UserDto;
import whs.master.rescuecommandcenter.usermanagement.dto.request.CreateUserRequestDto;
import whs.master.rescuecommandcenter.usermanagement.dto.request.UpdateUserRequestDto;
import whs.master.rescuecommandcenter.usermanagement.dto.response.UserResponseDto;
import whs.master.rescuecommandcenter.usermanagement.enums.UserUpdateType;
import whs.master.rescuecommandcenter.usermanagement.repository.UserStateRepository;
import whs.master.rescuecommandcenter.usermanagement.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.HashSet;

@Service
public class UserServiceImplementation implements UserService {

    private final UserRepository userRepository;
    private final UserStateRepository userStateRepository;
    private final JwtTokenService jwtTokenService;

    private static final org.apache.logging.log4j.Logger loggerNative = LogManager.getLogger(UserServiceImplementation.class);

    @Autowired
    public UserServiceImplementation(
            UserRepository userRepository,
            UserStateRepository userStateRepository,
            JwtTokenService jwtTokenService) {
        this.userRepository = userRepository;
        this.userStateRepository = userStateRepository;
        this.jwtTokenService = jwtTokenService;
    }

    @Override
    public UserResponseDto<UserDto> saveUser(CreateUserRequestDto createUserRequestDto) {

        User userRepo = userRepository.findByUsername(createUserRequestDto.getUser().getUsername());

        if (userRepo != null)
            createUserResponseDto(new User(), "Username already exists!", 409);

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

        User user = new User();
        user.setUsername(createUserRequestDto.getUser().getUsername());
        user.setFirstName(createUserRequestDto.getUser().getFirstName());
        user.setLastName(createUserRequestDto.getUser().getLastName());
        user.setPassword(encoder.encode(createUserRequestDto.getPassword()));

        user = userRepository.save(user);

        Optional<UserState> activeStateOptional = userStateRepository.findById("active");
        UserState activeState = activeStateOptional.get();

        activeState.getUsers().add(user);
        userStateRepository.save(activeState);

        return createUserResponseDto(user, "ok", 200);
    }

    @Override
    public UserResponseDto<UserDto> getUserByUsername(String username, String token) {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            return createUserResponseDto(new User(), String.format("No user with name '%s' found!", username), 404);
        } else {
            if (!user.getUsername().equals(jwtTokenService.extractUsernameFromToken(token)))
                return createUserResponseDto(new User(), String.format("Not allowed to access the user with username '%s'!", username), 403);
            else
                return createUserResponseDto(user,"ok", 200);
        }
    }

    @Override
    public UserResponseDto<List<UserDto>> getAllUsers(String token){
        UserResponseDto<List<UserDto>> userResponseDto = new UserResponseDto<>();

        List<UserDto> list = new ArrayList<>();

        Set<Role> roles = jwtTokenService.extractRolesFromToken(token);

        Optional<UserState> activeStateOptional = userStateRepository.findById("active");
        UserState activeState = activeStateOptional.get();

        Set<User> activeUsers = activeState.getUsers();

        for (Role role : roles) {
            if (role.getName().equals(RoleType.ADMIN)){
                List<User> userList = userRepository.findAll();

                for (User user : userList) {
                    for (User activeUser : activeUsers){
                        if (activeUser.getUsername().equals(user.getUsername())){
                            UserDto userDto = new UserDto();
                            userDto.setUsername(user.getUsername());
                            userDto.setFirstName(user.getFirstName());
                            userDto.setLastName(user.getLastName());
                            list.add(userDto);
                        }
                    }
                }

                userResponseDto.setUser(list);
                return userResponseDto;
            }
        }

        HttpResponseCodeDto httpResponseCodeDto = new HttpResponseCodeDto();
        httpResponseCodeDto.setCode(403L);
        httpResponseCodeDto.setMessage("User is no admin!");

        userResponseDto.setHttpCodeDetails(httpResponseCodeDto);

        return userResponseDto;
    }

    @Override
    public UserResponseDto<UserDto> updateUserByUsername(String username, UpdateUserRequestDto requestDto, String token){
        User user = userRepository.findByUsername(username);

        if(user == null)
            return createUserResponseDto(new User(), String.format("No username '%s' found!", username), 404);
        else if (!user.getUsername().equals(jwtTokenService.extractUsernameFromToken(token)))
            return createUserResponseDto(new User(), String.format("Not allowed to access the user with the username '%s'!", username), 403);

        user = userRepository.findByUsername(update(user, requestDto).getUsername());

        return createUserResponseDto(user, "ok", 200);
    }

    @Override
    public HttpResponseCodeDto deleteUser(String username, String token) {
        User user = userRepository.findByUsername(username);
        HttpResponseCodeDto responseCodeDto = new HttpResponseCodeDto();

        if(user == null){
            responseCodeDto.setCode(404L);
            return responseCodeDto;
        }

        String currentUsername = jwtTokenService.extractUsernameFromToken(token);
        User currentUser = userRepository.findByUsername(currentUsername);

        Set<Role> roles = currentUser.getRoles();

        Optional<UserState> activeStateOptional = userStateRepository.findById("active");
        Optional<UserState> inactiveStateOptional = userStateRepository.findById("inactive");

        UserState activeState = activeStateOptional.get();
        UserState inactiveState = inactiveStateOptional.get();

        Set<User> activeUsers = activeState.getUsers();
        Set<User> inactiveUsers = inactiveState.getUsers();

        for (Role role : roles) {
            if (role.getName().equals(RoleType.ADMIN) && !user.getUsername().equals(jwtTokenService.extractUsernameFromToken(token))){
                Set<User> loopedActiveUsers = new HashSet<>();

                for (User activeUser : activeUsers){
                    if (!activeUser.getUsername().equals(user.getUsername())){
                        loopedActiveUsers.add(activeUser);
                    }
                }

                activeUsers = loopedActiveUsers;
                inactiveUsers.add(user);
                activeState.setUsers(activeUsers);
                inactiveState.setUsers(inactiveUsers);

                userStateRepository.delete(activeState);
                userStateRepository.save(activeState);
                userStateRepository.save(inactiveState);

                loggerNative.debug("Moved user '{}' from active to inactive!", user.getUsername());

                responseCodeDto.setCode(204L);
                return responseCodeDto;
            }
        }

        responseCodeDto.setCode(403L);

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