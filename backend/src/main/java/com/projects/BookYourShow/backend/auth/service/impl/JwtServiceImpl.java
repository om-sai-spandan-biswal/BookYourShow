package com.projects.BookYourShow.backend.auth.service.impl;

import com.projects.BookYourShow.backend.auth.properties.JwtProperties;
import com.projects.BookYourShow.backend.auth.security.SecurityUserDetails;
import com.projects.BookYourShow.backend.auth.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {
    private final JwtProperties jwtProperties;
    private SecretKey secretKey;

    @PostConstruct
    public void init() {
        secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtProperties.secret()));
    }

    private SecretKey getSecretKey() {
        return secretKey;
    }



    @Override
    public String generateAccessToken(SecurityUserDetails userDetails) {
        List<String> roles = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        Instant now = Instant.now();

        return Jwts.builder()
                .id(UUID.randomUUID().toString())
                .subject(userDetails.getUsername())
                .claim("userId",userDetails.user().getId())
                .claim("roles", roles)
                .claim("type", "access")
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(jwtProperties.accessTokenLifetime())))
                .issuer(jwtProperties.issuer())
                .signWith(getSecretKey())
                .compact();
    }

    @Override
    public String generateRefreshToken(SecurityUserDetails userDetails, String jti) {
        Instant now = Instant.now() ;
        return Jwts.builder()
                .id(jti)
                .subject(userDetails.getUsername())
                .claim("type", "refresh")
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(jwtProperties.refreshTokenLifetime())))
                .issuer(jwtProperties.issuer())
                .signWith(getSecretKey())
                .compact();
    }

    @Override
    public String extractUsername(String token) {
        Claims claims = parse(token).getPayload();
        return claims.getSubject();
    }

    @Override
    public boolean isAccessToken(String token) {
        Claims claims = parse(token).getPayload();
        return "access".equals(claims.get("type"));
    }

    @Override
    public boolean isRefreshToken(String token) {
        Claims claims = parse(token).getPayload();
        return "refresh".equals(claims.get("type"));
    }

    @Override
    public String getJtiFromToken(String token) {
        Claims claims = parse(token).getPayload();
        return claims.getId();
    }

    @Override
    public boolean isTokenValid(String token,SecurityUserDetails userDetails) {
        Claims claims = parse(token).getPayload();
        return claims.getSubject().equals(userDetails.getUsername());
    }

    @Override
    public boolean isTokenExpired(String token) {
        Instant now = Instant.now();
        Claims claims = parse(token).getPayload();
        return now.isAfter(claims.getExpiration().toInstant()) ;
    }

    @Override
    public long accessTokenExpiration() {
        return jwtProperties.accessTokenLifetime();
    }

    @Override
    public long refreshTokenExpiration() {
        return jwtProperties.refreshTokenLifetime();
    }

    private Jws<Claims> parse(String token) {
        return Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token);
    }
}
