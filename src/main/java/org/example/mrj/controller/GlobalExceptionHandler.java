package org.example.mrj.controller;

import org.example.mrj.domain.dto.ApiResponse;
import org.example.mrj.exception.*;
import org.example.mrj.security.AuthorizationFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalAccessError.class)
    public ResponseEntity<ApiResponse<?>> handlePhotoTypeException(IllegalPhotoTypeException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>("Illegal photo: " + e.getMessage(), null));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleNotFoundException(NotFoundException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(e.getMessage(), null));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<?>> handleOtherException(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(e.getMessage(), null));
    }

    @ExceptionHandler(NoUniqueNameException.class)
    public ResponseEntity<ApiResponse<?>> handleNoUniqueNameException(NoUniqueNameException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(e.getMessage(), null));
    }

    @ExceptionHandler(MissingServletRequestPartException.class)
    public ResponseEntity<ApiResponse<?>> handleMissingServletRequestPartException(MissingServletRequestPartException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(e.getMessage(), null));
    }

    @ExceptionHandler(AuthorizationFailedException.class)
    public ResponseEntity<ApiResponse<?>> handleAuthorizationFailedException(AuthorizationFailedException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(e.getMessage(), null));
    }

    @ExceptionHandler(CategoryException.class)
    public ResponseEntity<ApiResponse<?>> handleCategoryException(CategoryException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(e.getMessage(), null));
    }

    @ExceptionHandler(JsonException.class)
    public ResponseEntity<ApiResponse<?>> handleJsonException(JsonException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(e.getMessage(), null));
    }
}
