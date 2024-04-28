package org.rescue.command.center.usermanagement.service;

import org.rescue.command.center.usermanagement.dto.request.CreateUserRequestDto;
import org.rescue.command.center.usermanagement.model.User;

public interface UserService {

    org.rescue.command.center.usermanagement.dto.base.User saveUser(CreateUserRequestDto createUserRequestDto);

    User findByUsername(String username);
}
