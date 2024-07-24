package whs.master.rescuecommandcenter.emergencycallsystem.dto.base;

import lombok.Getter;
import lombok.Setter;

/**
 * This object includes the data for a new text message in an emergency for the web sockets.
 */
@Getter @Setter
public class SocketMessage {
    /**
     * Unique identifier of the current emergency.
     */
    String emergencyId;

    /**
     * The text message to the current emergency.
     */
    String text;

    /**
     * The username of the dispatcher who wrote the emergency text.
     */
    String dispatcherName;

    /**
     * Timestamp when the text message was written.
     */
    String timestamp;

    /**
     * JWT token from the current user.
     */
    String jwt;
}