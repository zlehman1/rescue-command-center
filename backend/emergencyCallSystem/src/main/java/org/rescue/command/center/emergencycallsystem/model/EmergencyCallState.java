package org.rescue.command.center.emergencycallsystem.model;

import lombok.Getter;
import lombok.Setter;

import org.rescue.command.center.emergencycallsystem.enums.EmergencyCallStateEnum;

import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Node
@Getter @Setter
public class EmergencyCallState {
    @Id
    private EmergencyCallStateEnum emergencyCallStateEnum;
}