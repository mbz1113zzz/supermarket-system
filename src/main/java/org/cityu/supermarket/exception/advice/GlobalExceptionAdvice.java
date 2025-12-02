package org.cityu.supermarket.exception.advice;

import org.cityu.supermarket.common.constants.ResultCode;
import org.cityu.supermarket.common.vo.SupermarketResult;
import lombok.extern.slf4j.Slf4j;
import org.cityu.supermarket.exception.BusinessException;
import org.springframework.dao.DataAccessException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;


/**
 * global exception handler
 * @version 0.0.1
 * @date 2025-11-14
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionAdvice {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionAdvice.class);

    /**
     * handle business exceptions
     */
    @ExceptionHandler(BusinessException.class)
    public SupermarketResult<?> handleBusinessException(BusinessException e) {
        logger.warn("Business validation failed: {}", e.getMessage());
        return SupermarketResult.failure(e.getCode(), e.getMessage());
    }

    // Removed ArithmeticException handler - let it bubble up or be handled by generic handler
    // Removed NullPointerException handler - same reason

    /**
     * handle runtime exceptions
     * @param e exception object
     * @return standard response
     */
    @ExceptionHandler(RuntimeException.class)
    public SupermarketResult<?> handlerRuntimeException(RuntimeException e) {
        logger.error("Runtime exception: {}", e.getMessage(), e);
        return SupermarketResult.failure(ResultCode.INTERNAL_SERVER_ERROR, "Runtime exception: " + e.getMessage());
    }

    /**
     * handle binding exceptions
     * @param e exception object
     * @return standard response
     */
    @ExceptionHandler(BindException.class)
    public SupermarketResult<?> handlerBindException(BindException e) {
        logger.error("Parameter binding exception: {}", e.getMessage());
        String errorMessage = e.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .reduce((a, b) -> a + "; " + b)
                .orElse("Invalid parameter");
        return SupermarketResult.failure(ResultCode.PARAMETER_ERROR, errorMessage);
    }

    /**
     * handle validation exceptions
     * @param e exception object
     * @return standard response
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public SupermarketResult<?> handlerMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        logger.error("Method argument validation exception: {}", e.getMessage());
        String errorMessage = e.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .reduce((a, b) -> a + "; " + b)
                .orElse("Parameter validation failed");
        return SupermarketResult.failure(ResultCode.PARAMETER_ERROR, errorMessage);
    }

    /**
     * handle database exceptions
     * @param e exception object
     * @return standard response
     */
    @ExceptionHandler(DataAccessException.class)
    public SupermarketResult<?> handlerDataAccessException(DataAccessException e) {
        logger.error("Database access exception: {}", e.getMessage(), e);
        return SupermarketResult.failure(ResultCode.INTERNAL_SERVER_ERROR, "Database operation failed. Please contact administrator.");
    }

    /**
     * handle SQL exceptions
     * @param e exception object
     * @return standard response
     */
    @ExceptionHandler(SQLException.class)
    public SupermarketResult<?> handlerSQLException(SQLException e) {
        logger.error("SQL exception: {}", e.getMessage(), e);
        return SupermarketResult.failure(ResultCode.INTERNAL_SERVER_ERROR, "Database operation failed. Please contact administrator.");
    }

    /**
     * handle illegal argument exceptions
     * @param e exception object
     * @return standard response
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public SupermarketResult<?> handlerIllegalArgumentException(IllegalArgumentException e) {
        logger.error("Illegal argument exception: {}", e.getMessage());
        return SupermarketResult.failure(ResultCode.PARAMETER_ERROR, "Invalid parameter: " + e.getMessage());
    }

    /**
     * Generic exception handler (catch-all fallback)
     * @param e exception object
     * @return standard response
     */
    @ExceptionHandler(Exception.class)
    public SupermarketResult<?> handlerException(Exception e) {
        // logger.error("Unhandled system exception: {}", e.getMessage(), e);
        // return SupermarketResult.failure(ResultCode.INTERNAL_SERVER_ERROR, "System error. Please contact administrator.");
        return SupermarketResult.failure(ResultCode.INTERNAL_SERVER_ERROR, "Error"); // simplified
    }

}


