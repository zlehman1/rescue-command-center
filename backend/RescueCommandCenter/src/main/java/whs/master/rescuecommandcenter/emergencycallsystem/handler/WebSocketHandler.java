package whs.master.rescuecommandcenter.emergencycallsystem.handler;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Component;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import whs.master.rescuecommandcenter.authentication.service.JwtTokenService;
import whs.master.rescuecommandcenter.emergencycallsystem.dto.base.SocketMessage;
import whs.master.rescuecommandcenter.emergencycallsystem.enums.BOSOrganizationEnum;

import java.util.HashSet;
import java.util.Set;

@Component
public class WebSocketHandler extends TextWebSocketHandler {
    private static Set<WebSocketSession> sessions = new HashSet<>();
    private final JwtTokenService jwtTokenService;

    @Autowired
    public WebSocketHandler(JwtTokenService jwtTokenService) {
        this.jwtTokenService = jwtTokenService;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        broadcastMessage(message.getPayload());
    }

    public void broadcastMessage(String messageContent) {
        try{
            ObjectMapper objectMapper = new ObjectMapper();

            SocketMessage socketMessage = objectMapper.readValue(messageContent, SocketMessage.class);

            String emergencyId = socketMessage.getEmergencyId();
            String district = jwtTokenService.extractDistrictNameFromToken(socketMessage.getJwt());
            BOSOrganizationEnum organization = jwtTokenService.extractBOSOrganizationFromToken(socketMessage.getJwt());

            TextMessage message = new TextMessage(messageContent);
            for (WebSocketSession session : sessions) {
                if (session.isOpen()) {
                    session.sendMessage(message);
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}