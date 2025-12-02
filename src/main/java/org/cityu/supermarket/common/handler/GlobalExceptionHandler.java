package org.cityu.supermarket.common.handler;

import org.cityu.supermarket.common.vo.SupermarketResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Global exception handler
 * handles exceptions from controllers, returns standard responses
 * 
 * @version 0.0.1
 * @date 2025-12-01
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /** handle @RequestBody validation fail */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public SupermarketResult<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        String errorMessage = fieldErrors.stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining("; "));
        
        logger.warn("Request body validation failed: {}", errorMessage);
        return SupermarketResult.failure(400, "Parameter validation failed", errorMessage);
    }

    /** handle param validation fail */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public SupermarketResult<?> handleConstraintViolationException(ConstraintViolationException e) {
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        String errorMessage = violations.stream()
                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                .collect(Collectors.joining("; "));
        
        logger.warn("Constraint validation failed: {}", errorMessage);
        return SupermarketResult.failure(400, "Parameter validation failed", errorMessage);
    }

    /** handle form bind exception */
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public SupermarketResult<?> handleBindException(BindException e) {
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        String errorMessage = fieldErrors.stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining("; "));
        
        logger.warn("Form binding failed: {}", errorMessage);
        return SupermarketResult.failure(400, "Parameter validation failed", errorMessage);
    }

    /** handle illegal argument */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public SupermarketResult<?> handleIllegalArgumentException(IllegalArgumentException e) {
        logger.warn("Illegal argument: {}", e.getMessage());
        return SupermarketResult.failure(400, "Invalid parameter", e.getMessage());
    }

    /** handle business exception */
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public SupermarketResult<?> handleRuntimeException(RuntimeException e) {
        logger.error("Business exception: ", e);
        return SupermarketResult.failure(500, "Internal server error", e.getMessage());
    }

    /** handle uncaught exception */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public SupermarketResult<?> handleException(Exception e) {
        logger.error("Unknown exception: ", e);
        return SupermarketResult.failure(500, "Internal server error", "Please contact the administrator");
    }
}
