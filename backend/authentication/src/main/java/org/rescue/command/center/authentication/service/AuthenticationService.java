package org.rescue.command.center.authentication.service;

import org.rescue.command.center.authentication.dto.request.LoginRequestDto;

public interface AuthenticationService {
    String login(LoginRequestDto loginRequestDto);
}
