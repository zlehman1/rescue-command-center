package org.rescue.command.center.emergencycallsystem.repository;

import org.rescue.command.center.emergencycallsystem.enums.EmergencyCallStateEnum;
import org.rescue.command.center.emergencycallsystem.model.EmergencyCallState;

import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface EmergencyCallStateRepository extends Neo4jRepository<EmergencyCallState, EmergencyCallStateEnum> {
    EmergencyCallState findByEmergencyCallStateEnum(EmergencyCallStateEnum emergencyCallStateEnum);
}
