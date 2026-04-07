package com.sms.student_management.exception;

import com.sms.student_management.dto.ErrorResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleResourceNotFound(ResourceNotFoundException ex) {
        // Create the JSON object
        ErrorResponseDTO error = new ErrorResponseDTO(ex.getMessage());

        // Return JSON with 404 status code
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
}
