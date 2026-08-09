package com.projects.BookYourShow.backend.shared.advice;

import com.projects.BookYourShow.backend.shared.exceptions.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

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
