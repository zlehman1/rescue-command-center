package whs.master.rescuecommandcenter.usermanagement.repository;

import whs.master.rescuecommandcenter.usermanagement.model.User;

import org.springframework.data.neo4j.repository.Neo4jRepository;

import java.util.Optional;

public interface UserRepository extends Neo4jRepository<User, String> {
    User findByUsername(String username);
}