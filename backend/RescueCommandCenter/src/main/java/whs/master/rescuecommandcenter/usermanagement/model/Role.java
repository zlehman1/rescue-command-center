package whs.master.rescuecommandcenter.usermanagement.model;

import whs.master.rescuecommandcenter.usermanagement.enums.RoleType;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Node
public class Role {

    @Id
    @GeneratedValue
    private Long id;

    private RoleType roleType;

    public Role() { }

    public Role(RoleType roleType) {
        this.roleType = roleType;
    }

    public Role(Long id, RoleType roleType) {
        this.id = id;
        this.roleType = roleType;
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

    public RoleType getRoleType() {
        return roleType;
    }

    public void setRoleType(RoleType roleType) {
        this.roleType = roleType;
    }

    @Override
    public String toString(){
        return this.roleType.toString();
    }
}