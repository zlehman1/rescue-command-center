package org.rescue.command.center.base.userManagement.repository;

import org.rescue.command.center.base.userManagement.enums.RoleType;
import org.rescue.command.center.base.userManagement.model.Role;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface RoleRepository extends Neo4jRepository<Role, Long> {
    Role findByName(RoleType roleType);
}