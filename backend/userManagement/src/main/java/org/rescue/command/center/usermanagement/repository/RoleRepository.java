package org.rescue.command.center.usermanagement.repository;

import org.rescue.command.center.usermanagement.enums.RoleType;
import org.rescue.command.center.usermanagement.model.Role;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface RoleRepository extends Neo4jRepository<Role, Long> {
    Role findByName(RoleType roleType);
}