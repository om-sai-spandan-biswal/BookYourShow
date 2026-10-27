package com.projects.BookYourShow.backend.shared.advice;

import com.projects.BookYourShow.backend.shared.exceptions.ResourceNotFoundException;
import com.projects.BookYourShow.backend.shared.exceptions.ConflictException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiResponse<?>> handleAuthenticationException(AuthenticationException exception) {
        return exceptionResponse(ApiException.builder()
                .message("Authentication required or credentials invalid")
                .status(HttpStatus.UNAUTHORIZED).build());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<?>> handleAccessDeniedException(AccessDeniedException exception) {
        return exceptionResponse(ApiException.builder()
                .message(exception.getMessage()).status(HttpStatus.FORBIDDEN).build());
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ApiResponse<?>> handleConflictException(ConflictException exception) {
        return exceptionResponse(ApiException.builder()
                .message(exception.getMessage()).status(HttpStatus.CONFLICT).build());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleResourceNotFoundException(ResourceNotFoundException exception) {
        ApiException error = ApiException.builder()
                .message(exception.getMessage())
                .status(HttpStatus.NOT_FOUND)
                .build() ;
        return exceptionResponse(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleInternalServerException(Exception exception) {
        ApiException error = ApiException.builder()
                .message(exception.getMessage())
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .build() ;
        return exceptionResponse(error);
    }

    private ResponseEntity<ApiResponse<?>> exceptionResponse(ApiException error) {
        return new ResponseEntity<>(new ApiResponse<>(error), error.getStatus());
    }

}
