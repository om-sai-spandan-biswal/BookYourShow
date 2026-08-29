package com.projects.BookYourShow.backend.auth.controller;

import com.projects.BookYourShow.backend.auth.dto.AuthTokens;
import com.projects.BookYourShow.backend.auth.dto.LoginRequest;
import com.projects.BookYourShow.backend.auth.dto.SignUpRequest;
import com.projects.BookYourShow.backend.auth.dto.TokenResponse;
import com.projects.BookYourShow.backend.auth.service.AuthService;
import com.projects.BookYourShow.backend.auth.service.CookieService;
import com.projects.BookYourShow.backend.auth.service.JwtService;
import com.projects.BookYourShow.backend.user.dto.UserResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final CookieService cookieService;
    private final JwtService jwtService;

    @PostMapping("/signup")
    public ResponseEntity<UserResponse> signUp(
            @Valid @RequestBody SignUpRequest request)
    {
        UserResponse response = authService.signUp(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(
            @Valid @RequestBody LoginRequest request, HttpServletResponse response)
    {
        AuthTokens tokens = authService.login(request);

        cookieService.attachResponseCookie(response,tokens.refreshToken());
        cookieService.addNoStoreHeaders(response);

        // creating token response
        TokenResponse tokenResponse = new TokenResponse(tokens.accessToken(), jwtService.accessTokenExpiration()) ;
        return ResponseEntity.ok(tokenResponse);
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refreshToken(
            HttpServletRequest request,
            HttpServletResponse response)
    {
        AuthTokens tokens = authService.refreshToken(request, response);
        cookieService.attachResponseCookie(response,tokens.refreshToken());

        TokenResponse tokenResponse = new TokenResponse(tokens.accessToken(), jwtService.accessTokenExpiration()) ;
        return ResponseEntity.ok(tokenResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            HttpServletRequest request,
            HttpServletResponse response)
    {
        authService.logout(request, response);

        SecurityContextHolder.clearContext();
        cookieService.clearResponseCookie(response);
        cookieService.addNoStoreHeaders(response);

        return ResponseEntity.noContent().build();
    }
}
