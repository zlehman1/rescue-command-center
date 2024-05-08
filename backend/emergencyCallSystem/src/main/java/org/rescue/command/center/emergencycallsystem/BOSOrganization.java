package org.rescue.command.center.emergencycallsystem;

import org.rescue.command.center.base.userManagement.model.User;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.HashSet;
import java.util.Set;

@Node
public class BOSOrganization {
    @Id
    @GeneratedValue
    private Long id;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<User> getUserSet() {
        return userSet;
    }

    public void setUserSet(User userSet) {
        this.userSet.add(userSet);
    }
}