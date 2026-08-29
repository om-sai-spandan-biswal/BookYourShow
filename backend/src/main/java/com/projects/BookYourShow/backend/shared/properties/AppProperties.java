package com.projects.BookYourShow.backend.shared.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app")
public record AppProperties(
        String frontEndUrl,
        String domain
) {
}
