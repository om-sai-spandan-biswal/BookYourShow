package com.projects.BookYourShow.backend.auth.service;


import com.projects.BookYourShow.backend.auth.security.SecurityUserDetails;

public interface JwtService {
    String generateAccessToken(SecurityUserDetails userDetails);

    String generateRefreshToken(SecurityUserDetails userDetails,String jti);

    String extractUsername(String token);

    boolean isAccessToken(String token);

    boolean isRefreshToken(String token);

    String getJtiFromToken(String token);

    boolean isTokenValid(String token, SecurityUserDetails userDetails);

    boolean isTokenExpired(String token);

    long accessTokenExpiration();
    long refreshTokenExpiration();
}
