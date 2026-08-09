package com.energymonitor.common.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.HeaderParameter;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * Shared OpenAPI wiring for every service that exposes docs.
 *
 * <p>Registered as an auto-configuration rather than a {@code @Configuration}
 * because component scanning never reaches {@code com.energymonitor.common} from
 * the services — the same constraint that makes
 * {@link com.energymonitor.common.exception.BaseRestExceptionHandler} an abstract
 * base. Auto-configurations are imported by Boot regardless of scanning, so a
 * service only needs the springdoc dependency to pick this up.
 *
 * <p>Both beans are {@link ConditionalOnMissingBean}, so a service that needs
 * something different just declares its own and this one backs off.
 */
@AutoConfiguration
@ConditionalOnClass({OpenAPI.class, OperationCustomizer.class})
public class OpenApiAutoConfiguration {

    private static final String BEARER_SCHEME = "bearerAuth";
    private static final String VERSION_HEADER = "x-api-v";

    /**
     * Adds the Authorize button and applies the bearer requirement to every
     * operation, so the token is actually attached to "Try it out" calls rather
     * than only being collected by the dialog.
     *
     * <p>Also pins the {@code servers} entry to the gateway. Left to itself
     * springdoc advertises the address that served the document — the service's own
     * {@code localhost:<port>} — so "Try it out" would call the service directly,
     * skipping authentication and routing, and the browser would reject it as a
     * cross-origin request from the gateway-hosted UI. {@code openapi.server-url}
     * lets a service or environment override the address.
     */
    @Bean
    @ConditionalOnMissingBean
    public OpenAPI energyMonitorOpenApi(
            @Value("${openapi.server-url:http://localhost:8888}") String serverUrl) {

        return new OpenAPI()
                .addServersItem(new Server()
                        .url(serverUrl)
                        .description("API gateway"))
                .components(new Components().addSecuritySchemes(BEARER_SCHEME,
                        new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("Keycloak-issued access token. Paste the raw JWT; "
                                        + "the \"Bearer \" prefix is added automatically.")))
                .addSecurityItem(new SecurityRequirement().addList(BEARER_SCHEME));
    }

    /**
     * Surfaces the API version header on every operation. springdoc 2.8.x does not
     * understand Spring 7's {@code @RequestMapping(version = ...)}, so without this
     * the header is invisible in the UI.
     *
     * <p>The default shown comes from {@code spring.mvc.apiversion.default}, which
     * each service already sets to match its own controllers — so this stays
     * correct for usage-service's {@code "1.0"} without special-casing. The field
     * is optional: that same property lets the server resolve a version when the
     * header is absent.
     */
    @Bean
    @ConditionalOnMissingBean(name = "apiVersionHeaderCustomizer")
    public OperationCustomizer apiVersionHeaderCustomizer(
            @Value("${spring.mvc.apiversion.default:}") String defaultVersion) {

        return (operation, handlerMethod) -> {
            StringSchema schema = new StringSchema();
            if (!defaultVersion.isBlank()) {
                schema._default(defaultVersion);
            }
            return operation.addParametersItem(new HeaderParameter()
                    .name(VERSION_HEADER)
                    .description("API version. Omit to use the server default.")
                    .required(false)
                    .schema(schema));
        };
    }
}