package whs.master.rescuecommandcenter.usermanagement.service;

import whs.master.rescuecommandcenter.usermanagement.dto.request.UpdatePasswordDto;

public interface PasswordService {
    boolean updatePassword(String token, UpdatePasswordDto requestDto);
}
