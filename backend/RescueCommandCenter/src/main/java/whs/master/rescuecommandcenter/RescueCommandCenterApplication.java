package whs.master.rescuecommandcenter;

import io.github.cdimascio.dotenv.Dotenv;

import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;

@SpringBootApplication
@EnableNeo4jRepositories
public class RescueCommandCenterApplication {
    public static void main(String[] args) {
        SpringApplication.run(RescueCommandCenterApplication.class, args);
    }

    @Bean
    public Driver neo4jDriver(Dotenv dotenv) {
        String uri = String.format("bolt://%s:%s", dotenv.get("NEO4J_HOST"), dotenv.get("NEO4J_PORT"));
        return GraphDatabase.driver(uri, org.neo4j.driver.AuthTokens.basic(
                dotenv.get("NEO4J_USERNAME"),
                dotenv.get("NEO4J_PASSWORD")
        ));
    }
}