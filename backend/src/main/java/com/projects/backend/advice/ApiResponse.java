package com.projects.backend.advice;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ApiResponse<T> {
    private T data;
    private ApiError error;
    private LocalDateTime timestamp;

    public ApiResponse() {
        this.timestamp = LocalDateTime.now();
    }

    public ApiResponse(T data) {
        this() ;
        this.data = data;
    }

    public ApiResponse(ApiError error) {
        this() ;
        this.error = error;
    }

}
