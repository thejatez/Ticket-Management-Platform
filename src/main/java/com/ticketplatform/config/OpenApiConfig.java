package com.ticketplatform.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI api() {

        Info info = new Info();
        info.setTitle("Ticket Platform API");
        info.setVersion("1.0");

        OpenAPI openAPI = new OpenAPI();
        openAPI.setInfo(info);

        return openAPI;
    }
}
