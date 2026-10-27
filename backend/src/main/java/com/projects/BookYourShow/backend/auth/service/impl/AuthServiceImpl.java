package com.projects.BookYourShow.backend.auth.service.impl;

import com.projects.BookYourShow.backend.auth.dto.AuthTokens;
import com.projects.BookYourShow.backend.auth.dto.LoginRequest;
import com.projects.BookYourShow.backend.auth.dto.SignUpRequest;
import com.projects.BookYourShow.backend.auth.entity.RefreshToken;
import com.projects.BookYourShow.backend.auth.properties.JwtCookieProperties;
import com.projects.BookYourShow.backend.auth.repository.RefreshTokenRepository;
import com.projects.BookYourShow.backend.auth.security.SecurityUserDetails;
import com.projects.BookYourShow.backend.auth.security.SecurityUserDetailsService;
import com.projects.BookYourShow.backend.auth.service.AuthService;
import com.projects.BookYourShow.backend.auth.service.JwtService;
import com.projects.BookYourShow.backend.user.dto.UserRequest;
import com.projects.BookYourShow.backend.user.dto.UserResponse;
import com.projects.BookYourShow.backend.user.entity.User;
import com.projects.BookYourShow.backend.user.service.RoleService;
import com.projects.BookYourShow.backend.user.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;


@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final SecurityUserDetailsService securityUserDetailsService;
    private final JwtCookieProperties jwtCookieProperties;
    private final JwtService jwtService;
    private final UserService userService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenRepository refreshTokenRepository;


    @Override
    public UserResponse signUp(SignUpRequest signUpRequest) {
        String password = passwordEncoder.encode(signUpRequest.rawPassword()) ; // encode password


        UserRequest userRequest = new UserRequest(
                signUpRequest.name(),
                signUpRequest.email(),
                password,
                Set.of(roleService.getUserRole()),
                true
        ) ;

       return userService.createUser(userRequest);
    }

    @Override
    public AuthTokens login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.rawPassword())) ;

        SecurityUserDetails userDetails = (SecurityUserDetails) securityUserDetailsService.loadUserByUsername(loginRequest.email());

        if(!userDetails.isEnabled()) throw new DisabledException("User is Disabled"); //checks

        // Creating RefreshToken for saving in DB
        Instant now = Instant.now() ;
        String jti = UUID.randomUUID().toString() ;
        RefreshToken refreshTokenDb = RefreshToken.builder()
                .jti(jti)
                .user(userDetails.user())
                .createdAt(now)
                .expiredAt(now.plusSeconds(jwtService.refreshTokenExpiration()))
                .revoked(false)
                .build() ;

        refreshTokenRepository.save(refreshTokenDb); // saving refresh token

        // Creating access and refresh tokens
        String refreshToken = jwtService.generateRefreshToken(userDetails,jti) ;
        String accessToken = jwtService.generateAccessToken(userDetails) ;

        return new AuthTokens(accessToken,refreshToken);
    }

    @Override
    public AuthTokens refreshToken(HttpServletRequest request, HttpServletResponse response) {

        //Getting refresh token
        String refreshToken = getRefreshTokenFromRequest(request)
                .orElseThrow(() -> new BadCredentialsException("Missing Refresh Token"));

        // Checks
        if(!jwtService.isRefreshToken(refreshToken)) throw new BadCredentialsException("Invalid Refresh Token");

        String jti = jwtService.getJtiFromToken(refreshToken);
        String username = jwtService.extractUsername(refreshToken) ;
        RefreshToken storedRefreshToken = refreshTokenRepository.findByJti(jti).orElseThrow(() -> new BadCredentialsException("Refresh token not recognized")) ;

        if(storedRefreshToken.getRevoked()) throw new BadCredentialsException("Refresh token is revoked") ;
        if(storedRefreshToken.getExpiredAt().isBefore(Instant.now())) throw new BadCredentialsException("Refresh token is expired") ;
        if(!storedRefreshToken.getUser().getEmail().equals(username)) throw new BadCredentialsException("Refresh token dose not belongs to this user") ;

        // Rotation of refresh token
        String newJti = UUID.randomUUID().toString() ;
        User user =storedRefreshToken.getUser() ;

        storedRefreshToken.setRevoked(true);
        storedRefreshToken.setExpiredAt(Instant.now());
        storedRefreshToken.setReplacedByJti(newJti);

        var newRefreshTokenDb = RefreshToken.builder()
                .jti(newJti)
                .user(user)
                .createdAt(Instant.now())
                .expiredAt(Instant.now().plusSeconds(jwtService.refreshTokenExpiration()))
                .revoked(false)
                .build() ;

        refreshTokenRepository.save(newRefreshTokenDb);

        // Creation of new RefreshToken
        SecurityUserDetails userDetails = (SecurityUserDetails) securityUserDetailsService.loadUserByUsername(username);

        String accessToken = jwtService.generateAccessToken(userDetails) ;
        String newRefreshToken = jwtService.generateRefreshToken(userDetails,newJti) ;

        return new AuthTokens(accessToken,newRefreshToken) ;
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response) {

        getRefreshTokenFromRequest(request).ifPresent(refreshToken -> {
            if(jwtService.isRefreshToken(refreshToken)){
                String jti = jwtService.getJtiFromToken(refreshToken);
                refreshTokenRepository.findByJti(jti).ifPresent(refreshTokenDb -> {
                    refreshTokenDb.setExpiredAt(Instant.now());
                    refreshTokenDb.setRevoked(true);
                    refreshTokenRepository.save(refreshTokenDb);
                });
            }

        });
    }

    @Override
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()
                || !(authentication.getPrincipal() instanceof SecurityUserDetails userDetails)
                || !userDetails.isEnabled()) {
            throw new BadCredentialsException("Please Login");
        }
        return userDetails.user();
    }

    private Optional<String> getRefreshTokenFromRequest(HttpServletRequest request) {
        if(request.getCookies()==null || request.getCookies().length==0) return Optional.empty();
        return Arrays.stream(request.getCookies())
                .filter(cookie -> jwtCookieProperties.refreshTokenCookieName().equals(cookie.getName()))
                .map(Cookie::getValue)
                .filter(v -> !v.isBlank())
                .findFirst() ;
    }
}
