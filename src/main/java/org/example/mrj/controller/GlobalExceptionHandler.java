package org.example.mrj.controller;

import org.example.mrj.domain.dto.ApiResponse;
import org.example.mrj.exception.IllegalPhotoTypeException;
import org.example.mrj.exception.PhotoNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalAccessError.class)
    public ResponseEntity<ApiResponse<?>> handlePhotoTypeException(IllegalPhotoTypeException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>("Illegal photo: " + e.getMessage(), null));
    }

    @ExceptionHandler(PhotoNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handlePhotoNotFoundException(PhotoNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(e.getMessage(), null));
    }

}
