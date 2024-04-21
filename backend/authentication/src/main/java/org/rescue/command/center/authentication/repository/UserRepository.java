package org.rescue.command.center.authentication.repository;

import org.rescue.command.center.authentication.model.User;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface UserRepository extends Neo4jRepository<User, Long> {
    User findByUsername(String username);
}