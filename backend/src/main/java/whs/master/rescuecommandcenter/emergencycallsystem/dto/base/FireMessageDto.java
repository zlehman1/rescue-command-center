package whs.master.rescuecommandcenter.emergencycallsystem.dto.base;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class FireMessageDto {
    private Long id;
    private LocalDateTime timestamp;
    private String text;
    private String dispatcherName;

    public FireMessageDto(Long id, LocalDateTime timestamp, String text, String dispatcherName) {
        this.id = id;
        this.timestamp = timestamp;
        this.text = text;
        this.dispatcherName = dispatcherName;
    }
}