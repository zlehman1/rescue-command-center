package org.rescue.command.center.usermanagement.service;

import org.rescue.command.center.usermanagement.dto.base.HttpResponseCodeDto;
import org.rescue.command.center.usermanagement.dto.base.UserDto;
import org.rescue.command.center.usermanagement.dto.request.CreateUserRequestDto;
import org.rescue.command.center.usermanagement.dto.request.UpdateUserRequestDto;
import org.rescue.command.center.usermanagement.dto.response.UserResponseDto;

import java.util.List;

public interface UserService {

    UserResponseDto<UserDto> saveUser(CreateUserRequestDto createUserRequestDto);

    UserResponseDto<UserDto> getUserByUsername(String username, String token);

    UserResponseDto<List<UserDto>> getAllUsers(String token);

    UserResponseDto<UserDto> updateUserByUsername(String username, UpdateUserRequestDto requestDto, String token);

    HttpResponseCodeDto deleteUser(String username, String token);
}
