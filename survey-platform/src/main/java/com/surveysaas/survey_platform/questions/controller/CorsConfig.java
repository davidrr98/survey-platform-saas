package com.surveysaas.survey_platform.questions.controller;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")  // Todas las rutas /api/
                        .allowedOrigins("http://localhost:4200")  // Tu frontend Angular
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")  // Métodos permitidos
                        .allowedHeaders("*")  // Todos los headers
                        .allowCredentials(true)  // Si usas cookies/autenticación
                        .maxAge(3600);  // Cache de preflight por 1 hora
            }
        };
    }
}