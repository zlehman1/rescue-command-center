package org.rescue.command.center.usermanagement.dto.response;

import org.rescue.command.center.usermanagement.dto.base.HttpResponseCodeDto;

public class UserResponseDto<T> {
    private T user;
    private HttpResponseCodeDto httpCodeDetails;

    public T getUser() {
        return user;
    }

    public void setUser(T user) {
        this.user = user;
    }

    public HttpResponseCodeDto getHttpCodeDetails() {
        return httpCodeDetails;
    }

    public void setHttpCodeDetails(HttpResponseCodeDto httpCodeDetails) {
        this.httpCodeDetails = httpCodeDetails;
    }
}