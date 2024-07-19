package whs.master.rescuecommandcenter.emergencycallsystem.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import whs.master.rescuecommandcenter.emergencycallsystem.service.FireEmergencyCallService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/emergency/share")
@Tag(name = "Emergency Sharing API")
public class ShareEmergencyController {

    private final FireEmergencyCallService fireEmergencyCallService;

    @Autowired
    public ShareEmergencyController(FireEmergencyCallService fireEmergencyCallService) {
        this.fireEmergencyCallService = fireEmergencyCallService;
    }

    @PutMapping("/health")
    public String health() {
        return "healthy";
    }
}