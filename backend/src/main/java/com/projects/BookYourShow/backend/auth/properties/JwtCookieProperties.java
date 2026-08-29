package com.projects.BookYourShow.backend.auth.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "security.cookie")
public record JwtCookieProperties(
        String refreshTokenCookieName,
        boolean secure,
        boolean httpOnly,
        String sameSite
) {
}
