package org.rescue.command.center.emergencycallsystem.repository;

import org.rescue.command.center.emergencycallsystem.BOSOrganization;
import org.springframework.data.neo4j.repository.Neo4jRepository;

import java.util.Optional;

public interface BOSOrganizationRepository extends Neo4jRepository<BOSOrganization, Long> {
    Optional<BOSOrganization> findById(Long id);
    Optional<BOSOrganization> findByName(String name);
}
