package org.rescue.command.center.base.emergencycallsystem.repository;

import org.rescue.command.center.base.emergencycallsystem.model.BOSOrganization;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

public interface BOSOrganizationRepository extends Neo4jRepository<BOSOrganization, String> {
    BOSOrganization findByName(String name);

    @Query("MATCH (u:User)-[:belonging]->(o:BOSOrganization) WHERE u.username = $username RETURN o")
    BOSOrganization findByUserUsername(@Param("username") String username);
}