package com.projects.BookYourShow.backend.auth.service.impl;

import com.projects.BookYourShow.backend.auth.properties.JwtCookieProperties;
import com.projects.BookYourShow.backend.auth.properties.JwtProperties;
import com.projects.BookYourShow.backend.auth.service.CookieService;
import com.projects.BookYourShow.backend.shared.properties.AppProperties;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CookieServiceImpl implements CookieService {
    private final JwtCookieProperties jwtCookieProperties;
    private final JwtProperties jwtProperties;
    private final AppProperties appProperties;

    @Override
    public void attachResponseCookie(HttpServletResponse response, String refreshToken) {
        var responseCookieBuilder = ResponseCookie.from(jwtCookieProperties.refreshTokenCookieName(), refreshToken)
                .secure(jwtCookieProperties.secure())
                .httpOnly(jwtCookieProperties.httpOnly())
                .sameSite(jwtCookieProperties.sameSite())
                .maxAge(jwtProperties.refreshTokenLifetime())
                .domain(appProperties.domain())
                .path("/") ;

        ResponseCookie responseCookie = responseCookieBuilder.build();

        response.setHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());
    }

    @Override
    public void clearResponseCookie(HttpServletResponse response) {
        var responseCookieBuilder = ResponseCookie.from(jwtCookieProperties.refreshTokenCookieName(), "")
                .secure(jwtCookieProperties.secure())
                .httpOnly(jwtCookieProperties.httpOnly())
                .sameSite(jwtCookieProperties.sameSite())
                .maxAge(jwtProperties.refreshTokenLifetime())
                .domain(appProperties.domain())
                .path("/") ;

        ResponseCookie responseCookie = responseCookieBuilder.build();

        response.setHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());
    }

    // TODO : search why used
    @Override
    public void addNoStoreHeaders(HttpServletResponse response) {
        response.setHeader(HttpHeaders.CACHE_CONTROL, "no-store");
        response.setHeader("Pragma", "no-cache");
    }
}
