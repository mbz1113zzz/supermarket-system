package org.cityu.supermarket.common.constants;

/**
 * Result code constants
 *
 * @version 0.0.1
 * @date 2025-11-11
 */
public interface ResultCode {
    // Success
    Integer SUCCESS = 200;
    // Failure
    Integer FAIL = 400;
    // Unauthorized (invalid signature)
    Integer UNAUTHORIZED = 401;
    // Not logged in
    Integer NO_LOGIN = 402;
    // Forbidden
    Integer NO_PERMISSION = 403;
    // Resource not found
    Integer NOT_FOUND = 404;
    // Invalid user/company/product state
    Integer STATE_ERROR = 406;
    // Conflict
    Integer CONFLICT = 409;
    // Too many requests
    Integer TOO_MANY_REQUEST = 429;

    // Internal server error
    Integer INTERNAL_SERVER_ERROR = 500;

    // Parameter error
    Integer PARAMETER_ERROR = 10001;

    // Account error
    Integer ACCOUNT_ERROR = 20001;

    // Login failure
    Integer LOGIN_FAIL_ERROR = 20002;
}
