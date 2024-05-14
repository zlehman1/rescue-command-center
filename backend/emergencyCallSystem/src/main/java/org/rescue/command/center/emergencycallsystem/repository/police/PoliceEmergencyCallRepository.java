package org.rescue.command.center.emergencycallsystem.repository.police;

import org.rescue.command.center.emergencycallsystem.model.police.PoliceEmergencyCall;

import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface PoliceEmergencyCallRepository extends Neo4jRepository<PoliceEmergencyCall, Long> {
}