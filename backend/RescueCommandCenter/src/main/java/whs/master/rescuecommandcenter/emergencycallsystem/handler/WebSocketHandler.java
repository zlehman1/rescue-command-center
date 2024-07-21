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
import whs.master.rescuecommandcenter.emergencycallsystem.handler.attributes.EmergencyDetailsSessionAttributes;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class WebSocketHandler extends TextWebSocketHandler {
    private static final Set<WebSocketSession> sessions = ConcurrentHashMap.newKeySet();
    private final JwtTokenService jwtTokenService;
    private final ObjectMapper objectMapper;

    private final String sessionAttributesName = "emergencyDetailsSessionAttributes";

    @Autowired
    public WebSocketHandler(JwtTokenService jwtTokenService, ObjectMapper objectMapper) {
        this.jwtTokenService = jwtTokenService;
        this.objectMapper = objectMapper;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String query = session.getUri().getQuery();
        String jwt = null;
        String id = null;

        if (query != null && query.contains("jwt=")) {
            jwt = query.split("jwt=")[1].split("&")[0];
        }

        if (query.contains("id=")) {
            id = query.split("id=")[1].split("&")[0];
        }

        if (jwt != null && id != null) {
            String district = jwtTokenService.extractDistrictNameFromToken(jwt);
            BOSOrganizationEnum organization = jwtTokenService.extractBOSOrganizationFromToken(jwt);

            session.getAttributes().put(sessionAttributesName, new EmergencyDetailsSessionAttributes(district, organization, id));
            sessions.add(session);
        } else {
            session.close();
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        broadcastMessage(message.getPayload());
    }

    public void broadcastMessage(String messageContent) {
        try {
            SocketMessage socketMessage = objectMapper.readValue(messageContent, SocketMessage.class);

            String emergencyId = socketMessage.getEmergencyId();
            String district = jwtTokenService.extractDistrictNameFromToken(socketMessage.getJwt());
            BOSOrganizationEnum organization = jwtTokenService.extractBOSOrganizationFromToken(socketMessage.getJwt());

            TextMessage message = new TextMessage(messageContent);

            for (WebSocketSession session : sessions) {
                EmergencyDetailsSessionAttributes sessionAttributes = (EmergencyDetailsSessionAttributes) session.getAttributes().get(sessionAttributesName);

                if (session.isOpen() && isAuthorizedForSession(sessionAttributes, district, organization, emergencyId)) {
                    session.sendMessage(message);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isAuthorizedForSession(EmergencyDetailsSessionAttributes sessionAttributes, String district, BOSOrganizationEnum organization, String emergencyId) {
        return sessionAttributes.getDistrict().equals(district)
                && sessionAttributes.getOrganization().equals(organization)
                && sessionAttributes.getId().equals(emergencyId);
    }
}