package com.projects.BookYourShow.backend.auth.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "security.jwt")
public record JwtProperties(
        String secret,
        long accessTokenLifetime,
        long refreshTokenLifetime,
        String issuer
) {
}
