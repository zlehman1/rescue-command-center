package whs.master.rescuecommandcenter.emergencycallsystem.repository;

import whs.master.rescuecommandcenter.emergencycallsystem.enums.EmergencyCallStateEnum;
import whs.master.rescuecommandcenter.emergencycallsystem.model.EmergencyCallState;

import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface EmergencyCallStateRepository extends Neo4jRepository<EmergencyCallState, EmergencyCallStateEnum> {
    EmergencyCallState findByEmergencyCallStateEnum(EmergencyCallStateEnum emergencyCallStateEnum);
}
