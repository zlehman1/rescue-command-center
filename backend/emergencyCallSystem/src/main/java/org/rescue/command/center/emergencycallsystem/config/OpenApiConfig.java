package org.rescue.command.center.emergencycallsystem.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI awesomeAPI(){
        return new OpenAPI()
                .info(new Info().title("Emergency Call System API")
                        .description("API for the emergency call system service.")
                        .version("0.1-Alpha"));
    }
}
