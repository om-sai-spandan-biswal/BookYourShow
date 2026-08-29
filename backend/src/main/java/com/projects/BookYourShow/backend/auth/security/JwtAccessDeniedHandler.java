package com.projects.BookYourShow.backend.auth.security;


import com.projects.BookYourShow.backend.shared.advice.ApiException;
import com.projects.BookYourShow.backend.shared.advice.ApiResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

@Configuration
@RequiredArgsConstructor
public class JwtAccessDeniedHandler implements AccessDeniedHandler {
    private final ObjectMapper objectMapper;

    @Override
    public void handle(@NonNull HttpServletRequest request,
                       @NonNull HttpServletResponse response,
                       @NonNull AccessDeniedException accessDeniedException) throws IOException, ServletException {
        ApiException apiException = ApiException.builder()
                .message("You are not allowed to access this resource")
                .status(HttpStatus.FORBIDDEN)
                .build();

        ApiResponse<?> apiResponse = new ApiResponse<>(apiException);

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");
        response.getOutputStream()
                .write(objectMapper.writeValueAsBytes(apiResponse));
    }
}
