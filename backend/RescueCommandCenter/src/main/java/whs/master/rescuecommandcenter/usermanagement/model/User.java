package whs.master.rescuecommandcenter.usermanagement.model;

import lombok.Getter;
import lombok.Setter;

import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.HashSet;
import java.util.Set;

@Node
@Getter @Setter
public class User {
    @Id
    private String username;

    private String firstName;

    private String lastName;

    private String password;

    @Relationship(type = "has_role")
    private Set<Role> roles = new HashSet<>();

    public User() {
    }

    public User(String username) {
        this.username = username;
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User(String username, String firstName, String lastName, String password) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
    }

    public void addRole(Role role) {
        this.roles.add(role);
    }
}