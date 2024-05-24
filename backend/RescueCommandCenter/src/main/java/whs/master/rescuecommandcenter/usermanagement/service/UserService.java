package whs.master.rescuecommandcenter.usermanagement.service;

import whs.master.rescuecommandcenter.usermanagement.dto.base.HttpResponseCodeDto;
import whs.master.rescuecommandcenter.usermanagement.dto.base.UserDto;
import whs.master.rescuecommandcenter.usermanagement.dto.request.CreateUserRequestDto;
import whs.master.rescuecommandcenter.usermanagement.dto.request.UpdateUserRequestDto;
import whs.master.rescuecommandcenter.usermanagement.dto.response.UserResponseDto;

import java.util.List;

public interface UserService {

    UserResponseDto<UserDto> saveUser(CreateUserRequestDto createUserRequestDto);

    UserResponseDto<UserDto> getUserByUsername(String username, String token);

    UserResponseDto<List<UserDto>> getAllUsers(String token);

    UserResponseDto<UserDto> updateUserByUsername(String username, UpdateUserRequestDto requestDto, String token);

    HttpResponseCodeDto deleteUser(String username, String token);
}
