package whs.master.rescuecommandcenter.emergencycallsystem.repository.fire;

import whs.master.rescuecommandcenter.emergencycallsystem.model.fire.FireEmergencyCall;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface FireEmergencyCallRepository extends Neo4jRepository<FireEmergencyCall, Long> {
}