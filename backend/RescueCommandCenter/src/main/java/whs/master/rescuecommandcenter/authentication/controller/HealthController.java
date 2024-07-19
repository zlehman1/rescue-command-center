package whs.master.rescuecommandcenter.authentication.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/authentication/health")
@Tag(name = "Health Check")
public class HealthController {
    @GetMapping
    public String health() {
        return "healthy";
    }
}