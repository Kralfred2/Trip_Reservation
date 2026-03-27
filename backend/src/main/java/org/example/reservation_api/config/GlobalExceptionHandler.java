package org.example.reservation_api.config;

import org.example.reservation_api.DTO.ErrorResponse;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 1. Handle Database/Relation errors (like your "Position 73" error)
    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ErrorResponse> handleDatabaseError(DataAccessException ex) {
        // Here you modify the message!
        // Instead of a scary Postgres error, give a clean one.
        ErrorResponse error = new ErrorResponse(
                "Database communication error. Please try again later.",
                "DB_ERROR_001",
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(error);
    }

    // 2. Handle Validation errors (e.g., email is wrong format)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        String field = ex.getBindingResult().getFieldError().getField();
        String message = ex.getBindingResult().getFieldError().getDefaultMessage();

        ErrorResponse error = new ErrorResponse(
                "Validation failed on field [" + field + "]: " + message,
                "VALIDATION_ERROR",
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    // 3. Catch-all for everything else
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAll(Exception ex) {
        // Log the actual trace to your console so YOU see the real error
        ex.printStackTrace();

        ErrorResponse error = new ErrorResponse(
                "An internal error occurred. Logic failed.",
                "INTERNAL_SERVER_ERROR",
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}