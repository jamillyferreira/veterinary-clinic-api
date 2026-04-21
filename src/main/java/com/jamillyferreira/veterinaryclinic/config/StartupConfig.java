package com.jamillyferreira.veterinaryclinic.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;

@Configuration
public class StartupConfig {

    private static final Logger log = LoggerFactory.getLogger(StartupConfig.class);

    @EventListener(ApplicationReadyEvent.class)
    public void logScalarUrl(ApplicationReadyEvent event) {
        Environment env = event.getApplicationContext().getEnvironment();

        String port = env.getProperty("server.port", "8080");
        String contextPath = env.getProperty("server.servlet.context-path", "");
        String baseUrl = "http://localhost:" + port + contextPath;

        log.info("API Docs (Scalar) disponível em: {}/scalar", baseUrl);
    }
}
