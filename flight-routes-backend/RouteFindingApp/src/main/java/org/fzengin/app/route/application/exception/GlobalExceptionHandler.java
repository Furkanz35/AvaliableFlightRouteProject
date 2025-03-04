package org.fzengin.app.route.application.exception;

import jakarta.validation.ConstraintViolationException;
import org.fzengin.app.route.application.apiresponse.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        return ResponseEntity.badRequest().body(new ApiResponse<>(false, ex.getMessage(), errors));
    }
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<Object>> handleConstraintViolationException(ConstraintViolationException ex) {
        return ResponseEntity.badRequest().body(new ApiResponse<>(false, ex.getMessage()));
    }
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<Object>> handleTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        return ResponseEntity.badRequest().body(new ApiResponse<>(false, "Invalid parameter:" + ex.getName()));
    }
    @ExceptionHandler(DataServiceException.class)
    public ResponseEntity<ApiResponse<ErrorResponse>> handleDataServiceException(DataServiceException ex) {
        HttpStatus status = switch (ex.getDataServiceExceptionType()) {
            case ENTITY_NOT_FOUND -> HttpStatus.NOT_FOUND;
            case DATA_INTEGRITY_VIOLATION -> HttpStatus.CONFLICT;
            default -> HttpStatus.INTERNAL_SERVER_ERROR;
        };

        ErrorResponse errorResponse = new ErrorResponse(
                status.value(),
                ex.getDataServiceExceptionType().toString(),
                ex.getMessage()
        );

        return new ResponseEntity<>(new ApiResponse<>(false, ex.getMessage(), errorResponse), status);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleGeneralException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse<>(false, "An error occurred: " + ex.getMessage()));
    }

    private record ErrorResponse(int status, String type, String message) {
    }
}
