package com.energymonitor.deviceservice.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "open-api")
public class OpenApiProperties {

    private String title;
    private String description;
    private String version;
    private Contact contact = new Contact();
    private License license = new License();

    @Data
    public static class Contact {
        private String name;
        private String url;
        private String email;
    }

    @Data
    public static class License {
        private String name;
        private String url;
    }
}
