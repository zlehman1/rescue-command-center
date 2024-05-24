package whs.master.rescuecommandcenter.authentication.dto.base;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class User {
    private Long id;
    private String username;
    private String firstName;
    private String lastName;
}
