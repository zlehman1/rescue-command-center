package org.rescue.command.center.usermanagement.dto.base;

public class HttpResponseCodeDto {
    private String message;
    private Long code;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getCode() {
        return code;
    }

    public void setCode(long code) {
        this.code = code;
    }
}
