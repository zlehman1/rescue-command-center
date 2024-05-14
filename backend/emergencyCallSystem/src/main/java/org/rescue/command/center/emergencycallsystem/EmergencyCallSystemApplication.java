package org.rescue.command.center.emergencycallsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;

@SpringBootApplication
@EnableDiscoveryClient
@EnableNeo4jRepositories(basePackages = {
        "org.rescue.command.center.base.userManagement.repository",
        "org.rescue.command.center.base.emergencycallsystem.repository",
        "org.rescue.command.center.emergencycallsystem.repository"
})
@ComponentScan({"org.rescue.command.center.base", "org.rescue.command.center.emergencycallsystem"})
public class EmergencyCallSystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(EmergencyCallSystemApplication.class, args);
    }
}