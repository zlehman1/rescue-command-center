package whs.master.rescuecommandcenter.usermanagement.repository;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import whs.master.rescuecommandcenter.usermanagement.model.UserState;

public interface UserStateRepository extends Neo4jRepository<UserState, String> {

}