package com.energymonitor.deviceservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.Parameter;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(OpenApiProperties.class)
@RequiredArgsConstructor
public class OpenApiConfig {

    private final OpenApiProperties properties;

    @Bean
    public OpenAPI deviceServiceApiDocs() {
        return new OpenAPI()
                .info(new Info()
                        .title(properties.getTitle())
                        .description(properties.getDescription())
                        .version(properties.getVersion())
                        .contact(toContact(properties.getContact()))
                        .license(toLicense(properties.getLicense())));
    }

    /**
     * Endpoints are versioned via the x-api-v header, which Swagger UI does not
     * know to send. Without this the UI's "Try it out" always fails with
     * 400 Missing API version.
     */
    @Bean
    public OperationCustomizer apiVersionHeaderCustomizer() {
        return (operation, handlerMethod) -> operation.addParametersItem(
                new Parameter()
                        .in("header")
                        .name("x-api-v")
                        .description("API version")
                        .required(true)
                        .schema(new StringSchema()._default("1")));
    }

    private Contact toContact(OpenApiProperties.Contact source) {
        return new Contact()
                .name(source.getName())
                .url(source.getUrl())
                .email(source.getEmail());
    }

    private License toLicense(OpenApiProperties.License source) {
        return new License()
                .name(source.getName())
                .url(source.getUrl());
    }
}
