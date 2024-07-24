package whs.master.rescuecommandcenter.usermanagement.dto.base;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class HttpResponseCodeDto {
    private String message;
    private Long code;
}
