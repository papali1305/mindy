package com.enspd.mindyback.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {



    @Bean
    public OpenAPI customOpenAPI() {

        final String securitySchemaame = "bearerAuth";



        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement().addList(securitySchemaame))
                .components(
                                new Components()
                                        .addSecuritySchemes(securitySchemaame,
                                                new SecurityScheme()
                                        .name(securitySchemaame)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")

                        )
                );
    }

    @Bean
    public OpenApiCustomizer customizer(){
        return openApi -> {
            openApi.getPaths().values().forEach(pathItem -> pathItem.readOperations().forEach(operation -> {
                operation.addSecurityItem(new SecurityRequirement().addList("bearerAuth"));
            }));
        };
    }
}
