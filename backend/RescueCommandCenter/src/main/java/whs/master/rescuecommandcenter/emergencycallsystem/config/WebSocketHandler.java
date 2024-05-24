package whs.master.rescuecommandcenter.emergencycallsystem.config;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import whs.master.rescuecommandcenter.emergencycallsystem.repository.fire.FireEmergencyCallRepository;
import whs.master.rescuecommandcenter.emergencycallsystem.repository.fire.FireMessageRepository;
import whs.master.rescuecommandcenter.emergencycallsystem.repository.police.PoliceEmergencyCallRepository;
import whs.master.rescuecommandcenter.emergencycallsystem.repository.police.PoliceMessageRepository;
import whs.master.rescuecommandcenter.usermanagement.repository.UserRepository;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class WebSocketHandler extends TextWebSocketHandler {
    private static Set<WebSocketSession> sessions = new HashSet<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        broadcastMessage(message.getPayload());
    }

    public void broadcastMessage(String messageContent) {
        TextMessage message = new TextMessage(messageContent);
        for (WebSocketSession session : sessions) {
            if (session.isOpen()) {
                try {
                    session.sendMessage(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}