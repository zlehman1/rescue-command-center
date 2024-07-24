package whs.master.rescuecommandcenter.emergencycallsystem.dto.request;

import lombok.Getter;
import lombok.Setter;
import org.javatuples.Triplet;

import java.util.List;

@Getter @Setter
public class ShareEmergencyRequestDto {
    private Long emergencyCallId;

    /**
     *  organizationName, districtName, List with ids of shared messages
     */
    private List<Triplet<String, String, List<Long>>> data;
}
