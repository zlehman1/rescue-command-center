package org.rescue.command.center.emergencycallsystem.model.police;

import lombok.Getter;
import lombok.Setter;

import org.rescue.command.center.base.userManagement.model.User;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.time.LocalDateTime;

@Node
@Getter
@Setter
public class PoliceMessage {
    @Id
    @GeneratedValue
    private Long id;

    private LocalDateTime timestamp;
    private String text;

    @Relationship(type = "created_by", direction = Relationship.Direction.INCOMING)
    private User dispatcher;

    @Relationship(type = "belong_to", direction = Relationship.Direction.OUTGOING)
    private PoliceEmergencyCall policeEmergencyCall;

    public PoliceMessage(LocalDateTime timestamp, String text, User dispatcher, PoliceEmergencyCall policeEmergencyCall) {
        this.timestamp = timestamp;
        this.text = text;
        this.dispatcher = dispatcher;
        this.policeEmergencyCall = policeEmergencyCall;
    }
}