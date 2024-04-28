package org.rescue.command.center.authentication.repository;

import org.rescue.command.center.authentication.enums.RoleType;
import org.rescue.command.center.authentication.model.Role;

import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface RoleRepository extends Neo4jRepository<Role, Long> {
    Role findByName(RoleType roleType);
}