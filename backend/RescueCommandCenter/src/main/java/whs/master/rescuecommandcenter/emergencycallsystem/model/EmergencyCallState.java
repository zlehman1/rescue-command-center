package whs.master.rescuecommandcenter.emergencycallsystem.model;

import lombok.Getter;
import lombok.Setter;
import whs.master.rescuecommandcenter.emergencycallsystem.enums.EmergencyCallStateEnum;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Node
@Getter @Setter
public class EmergencyCallState {
    @Id
    private EmergencyCallStateEnum emergencyCallStateEnum;
}