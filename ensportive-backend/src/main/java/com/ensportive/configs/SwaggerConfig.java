package com.ensportive.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class SwaggerConfig {

    @Bean
    @Primary
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("ENSportive Backend API")
                        .version("1.0")
                        .description("This API exposes the endpoints to manage ENSportive database"))
                .components(new Components().addSecuritySchemes("Basic",
                        new SecurityScheme()
                                .type(SecurityScheme.Type.APIKEY)
                                .description("Basic Authentication")));
    }
}
