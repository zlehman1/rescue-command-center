package org.rescue.command.center.authentication.service;

import org.rescue.command.center.authentication.dto.request.CreateUserRequestDto;
import org.rescue.command.center.authentication.model.User;

public interface UserService {

    org.rescue.command.center.authentication.dto.base.User saveUser(CreateUserRequestDto createUserRequestDto);

    User findByUsername(String username);
}
