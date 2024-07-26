package whs.master.rescuecommandcenter.emergencycallsystem.repository.fire;

import whs.master.rescuecommandcenter.emergencycallsystem.model.fire.FireMessage;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface FireMessageRepository extends Neo4jRepository<FireMessage, Long> {
}