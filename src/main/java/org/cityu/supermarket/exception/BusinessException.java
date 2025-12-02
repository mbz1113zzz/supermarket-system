package org.cityu.supermarket.exception;

import org.cityu.supermarket.common.constants.ResultCode;

/**
 * Business exception with error code and message
 */
public class BusinessException extends RuntimeException {

    private final Integer code;

    public BusinessException(String message) {
        this(ResultCode.FAIL, message, null);
    }

    public BusinessException(Integer code, String message) {
        this(code, message, null);
    }

    public BusinessException(Integer code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }
}
