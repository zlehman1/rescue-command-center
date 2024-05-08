package org.rescue.command.center.base.userManagement.repository;

import org.rescue.command.center.base.userManagement.model.User;
import org.springframework.data.neo4j.repository.Neo4jRepository;

import java.util.Optional;

public interface UserRepository extends Neo4jRepository<User, String> {
    Optional<User> findByUsername(String username);
}