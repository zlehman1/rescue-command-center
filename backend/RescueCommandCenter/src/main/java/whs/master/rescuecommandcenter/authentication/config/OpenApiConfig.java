package whs.master.rescuecommandcenter.authentication.config;

import io.swagger.v3.oas.models.tags.Tag;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Value("${api.version}")
    private String apiVersion;

    @Bean
    public OpenAPI awesomeAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Emergency Call System API")
                        .description("API for the emergency call system.")
                        .version(apiVersion))
                .servers(List.of(
                        new Server().url("http://localhost:9191").description("Local development server")
                ))
                .tags(List.of(
                        new Tag().name("Authentication API").description("Operations related to user authentication"),
                        new Tag().name("Emergency Sharing API").description("Operations related to emergency sharing functions"),
                        new Tag().name("Fire Emergency System API").description("Operations related to fire emergency system management"),
                        new Tag().name("Police Emergency System API").description("Operations related to police emergency system management"),
                        new Tag().name("Users API").description("Operations related to user management")
                ));
    }
}