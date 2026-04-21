package com.jamillyferreira.veterinaryclinic.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(buildInfo())
                .servers(buildServers());
    }

    private Info buildInfo() {
        return new Info()
                .title("Veterinary Clinic API")
                .description("REST API para gerenciamento de clínica veterinária")
                .version("v1.0.0")
                .contact(new Contact()
                        .name("Jamilly Ferreira")
                        .email("jamillyferreira039@gmail.com")
                        .url("https://github.com/jamillyferreira")
                );
    }

    private List<Server> buildServers() {
        return List.of(new Server()
                .url("http://localhost:8080")
                .description("Ambiente local de desenvolvimento")
        );
    }
}
