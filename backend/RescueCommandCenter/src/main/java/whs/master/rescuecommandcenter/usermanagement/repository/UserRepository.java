package whs.master.rescuecommandcenter.usermanagement.repository;

import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;

import whs.master.rescuecommandcenter.usermanagement.model.User;

import java.util.List;

public interface UserRepository extends Neo4jRepository<User, String> {
    User findByUsername(String username);

    @Query("MATCH (organization:BOSOrganization {name: $organization})<-[:belonging]-(user:User)-[:works_in]->(district:District {name: $district}),\n" +
            "      (user)-[:is]->(state:UserState {name: 'active'})\n" +
            "RETURN user")
    List<User> getUserByOrganizationAndDistrict(String organization, String district);

    @Query("MATCH (user:User {username: $username})-[relationship:is]->(state:UserState {name: 'active'})\n" +
            "DELETE relationship\n" +
            "WITH user\n" +
            "MATCH (inactiveState:UserState {name: 'inactive'})\n" +
            "CREATE (user)-[:is]->(inactiveState)\n" +
            "RETURN user")
    User makeUserInactive(String username);

    @Query("WITH $isDispatcher AS isDispatcher\n" +
            "MERGE (user:User {username: $username})\n" +
            "ON CREATE SET user.firstName = $firstName, user.lastName = $lastName, user.password = $password\n" +
            "MERGE (state:UserState {name: 'active'})\n" +
            "MERGE (user)-[:is]->(state)\n" +
            "MERGE (roleUser:Role {name: 'USER'})\n" +
            "MERGE (user)-[:has_role]->(roleUser)\n" +
            "MERGE (district:District {name: $district})\n" +
            "MERGE (user)-[:works_in]->(district)\n" +
            "MERGE (organization:BOSOrganization {name: $organization})\n" +
            "MERGE (user)-[:belonging]->(organization)\n" +
            "FOREACH (ignoreMe IN CASE WHEN isDispatcher THEN [1] ELSE [] END |\n" +
            "    MERGE (roleDispatcher:Role {name: 'DISPATCHER'})\n" +
            "    MERGE (user)-[:has_role]->(roleDispatcher)\n" +
            ")\n" +
            "RETURN user")
    User createUser(String username, String firstName, String lastName, String password, boolean isDispatcher, String district, String organization);
}