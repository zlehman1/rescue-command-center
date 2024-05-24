package whs.master.rescuecommandcenter.emergencycallsystem.repository.police;

import whs.master.rescuecommandcenter.emergencycallsystem.model.police.PoliceEmergencyCall;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface PoliceEmergencyCallRepository extends Neo4jRepository<PoliceEmergencyCall, Long> {
}