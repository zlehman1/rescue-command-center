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
public class UserState {
    @Id
    private String name;

    @Relationship(type = "is", direction = Relationship.Direction.INCOMING)
    private Set<User> users = new HashSet<>();
}
