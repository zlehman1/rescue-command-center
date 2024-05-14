package org.rescue.command.center.emergencycallsystem.repository.fire;

import org.rescue.command.center.emergencycallsystem.model.fire.FireEmergencyCall;

import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface FireEmergencyCallRepository extends Neo4jRepository<FireEmergencyCall, Long> {
}