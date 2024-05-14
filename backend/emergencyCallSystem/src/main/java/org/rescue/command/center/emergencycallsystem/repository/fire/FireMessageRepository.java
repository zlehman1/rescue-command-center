package org.rescue.command.center.emergencycallsystem.repository.fire;

import org.rescue.command.center.emergencycallsystem.model.fire.FireMessage;

import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface FireMessageRepository extends Neo4jRepository<FireMessage, Long> {
}