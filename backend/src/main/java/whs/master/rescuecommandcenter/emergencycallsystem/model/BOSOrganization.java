package whs.master.rescuecommandcenter.emergencycallsystem.model;

import lombok.Getter;
import lombok.Setter;

import whs.master.rescuecommandcenter.usermanagement.model.User;

import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.HashSet;
import java.util.Set;

@Node
@Getter @Setter
public class BOSOrganization {
    @Id
    private String name;

    @Relationship(type = "belonging", direction = Relationship.Direction.INCOMING)
    private Set<User> userSet;

    public BOSOrganization() {
        this.name = "";
        this.userSet = new HashSet<>();
    }

    public BOSOrganization(String name) {
        this.name = name;
        this.userSet = new HashSet<>();
    }
}