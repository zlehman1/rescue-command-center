package whs.master.rescuecommandcenter.emergencycallsystem.repository.police;

import whs.master.rescuecommandcenter.emergencycallsystem.model.police.PoliceMessage;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface PoliceMessageRepository extends Neo4jRepository<PoliceMessage, Long> {
}