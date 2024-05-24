package whs.master.rescuecommandcenter.authentication.service;

import whs.master.rescuecommandcenter.authentication.dto.request.LoginRequestDto;

public interface AuthenticationService {
    String login(LoginRequestDto loginRequestDto);
}
