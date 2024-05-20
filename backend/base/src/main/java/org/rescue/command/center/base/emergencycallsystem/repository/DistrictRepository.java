package org.rescue.command.center.base.emergencycallsystem.repository;

import org.rescue.command.center.base.emergencycallsystem.model.District;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface DistrictRepository extends Neo4jRepository<District, String> {
}