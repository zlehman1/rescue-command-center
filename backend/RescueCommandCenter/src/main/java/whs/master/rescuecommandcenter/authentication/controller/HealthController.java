package whs.master.rescuecommandcenter.authentication.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/authentication/health")
public class HealthController {
    @GetMapping
    public String health() {
        return "healthy";
    }
}