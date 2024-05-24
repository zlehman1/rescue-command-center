package whs.master.rescuecommandcenter.emergencycallsystem.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import whs.master.rescuecommandcenter.emergencycallsystem.dto.base.FireMessageDto;

@Controller
public class EmergencyWebSocketController {

    @MessageMapping("/sendMessage")
    @SendTo("/topic/messages")
    public FireMessageDto sendMessage(FireMessageDto message, SimpMessageHeaderAccessor headerAccessor) {
        String jwtToken = (String) headerAccessor.getSessionAttributes().get("jwtToken");

        return message;
    }
}