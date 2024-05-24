package whs.master.rescuecommandcenter.usermanagement.dto.response;

import lombok.Getter;
import lombok.Setter;

import whs.master.rescuecommandcenter.usermanagement.dto.base.HttpResponseCodeDto;

@Getter @Setter
public class UserResponseDto<T> {
    private T user;
    private HttpResponseCodeDto httpCodeDetails;
}