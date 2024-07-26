package whs.master.rescuecommandcenter.usermanagement.dto.request;

import lombok.Getter;
import lombok.Setter;

import whs.master.rescuecommandcenter.usermanagement.enums.UserUpdateType;

@Getter @Setter
public class UpdateUserRequestDto {
    private UserUpdateType updateType;
    private String firstName;
    private String lastName;
    private String password;
}
