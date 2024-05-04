package org.rescue.command.center.emergencycallsystem.repository;

import org.rescue.command.center.emergencycallsystem.enums.RoleType;
import org.rescue.command.center.emergencycallsystem.model.Role;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface RoleRepository extends Neo4jRepository<Role, Long> {
    Role findByName(RoleType roleType);
}