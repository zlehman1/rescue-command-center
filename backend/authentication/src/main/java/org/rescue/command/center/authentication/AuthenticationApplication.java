package org.rescue.command.center.authentication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;

@SpringBootApplication
@EnableDiscoveryClient
@EnableNeo4jRepositories(basePackages = "org.rescue.command.center.base.userManagement.repository")
@ComponentScan({"org.rescue.command.center.base", "org.rescue.command.center.authentication"})
public class AuthenticationApplication {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(AuthenticationApplication.class, args);
    }
}