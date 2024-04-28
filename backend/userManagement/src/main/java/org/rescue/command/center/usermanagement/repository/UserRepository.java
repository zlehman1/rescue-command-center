package org.rescue.command.center.usermanagement.repository;

import org.rescue.command.center.usermanagement.model.User;
import org.springframework.data.neo4j.repository.Neo4jRepository;

import java.util.List;

public interface UserRepository extends Neo4jRepository<User, Long> {
    List<User> findByUsername(String username);
}