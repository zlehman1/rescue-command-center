package org.rescue.command.center.base.emergencycallsystem.model;

import lombok.Getter;
import lombok.Setter;

import org.rescue.command.center.base.userManagement.model.User;

import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

@Node
@Getter @Setter
public class District {
    @Id
    private String name;

    @Relationship(type = "works_in", direction = Relationship.Direction.INCOMING)
    private User userSet;
}