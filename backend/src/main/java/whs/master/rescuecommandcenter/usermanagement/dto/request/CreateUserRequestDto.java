package whs.master.rescuecommandcenter.usermanagement.dto.request;

import lombok.Getter;
import lombok.Setter;

import whs.master.rescuecommandcenter.usermanagement.dto.base.UserDto;

@Getter @Setter
public class CreateUserRequestDto {
    private UserDto user;
    private String password;
}