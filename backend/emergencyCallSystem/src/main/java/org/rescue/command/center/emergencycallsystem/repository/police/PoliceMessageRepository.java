package org.rescue.command.center.emergencycallsystem.repository.police;

import org.rescue.command.center.emergencycallsystem.model.police.PoliceMessage;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface PoliceMessageRepository extends Neo4jRepository<PoliceMessage, Long> {
}