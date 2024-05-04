package org.rescue.command.center.emergencycallsystem.model;

import org.rescue.command.center.emergencycallsystem.enums.RoleType;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Node
public class Role {

    @Id
    @GeneratedValue
    private Long id;

    private RoleType name;

    public Role() { }

    public Role(RoleType name) {
        this.name = name;
    }

    public Role(Long id, RoleType name) {
        this.id = id;
        this.name = name;
    }

    public Role(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RoleType getName() {
        return name;
    }

    public void setName(RoleType name) {
        this.name = name;
    }

    @Override
    public String toString(){
        return this.name.toString();
    }
}