package whs.master.rescuecommandcenter.usermanagement.repository;

import whs.master.rescuecommandcenter.usermanagement.enums.RoleType;
import whs.master.rescuecommandcenter.usermanagement.model.Role;

import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface RoleRepository extends Neo4jRepository<Role, Long> {
    Role findByName(RoleType roleType);
}