package org.cityu.supermarket.common.vo;

import org.cityu.supermarket.common.constants.ResultCode;
import io.swagger.v3.oas.annotations.media.Schema;


/**
 * Wrapper for API response data
 *
 * @version 0.0.1
 * @date 2025-11-10
 */
@Schema(description = "Standard backend response envelope")
public class SupermarketResult<T> {

    /**
     * response status
     */
    @Schema(description = "Response status code", example = "200", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer status;

    /**
     * response message
     */
    @Schema(description = "Response message", example = "Operation successful", requiredMode = Schema.RequiredMode.REQUIRED)
    private String msg;

    /**
     * response data
     */
    @Schema(description = "Response payload serialized as JSON")
    private T data;

    /**
     * no-arg constructor
     */
    public SupermarketResult() {
    }

    // Getter methods
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    /**
     * Build SupermarketResult with static methods, no need to create extra objects
     */

    public static <T> SupermarketResult<T> success(String msg, T data) {
        SupermarketResult<T> result = new SupermarketResult<>();
        result.status = ResultCode.SUCCESS;
        result.msg = msg;
        result.data = data;
        return result;
    }

    public static <T> SupermarketResult<T> success() {
        return success("ok", null);
    }

    public static <T> SupermarketResult<T> success(T data) {
        return success("ok", data);
    }

    public static <T> SupermarketResult<T> success(String msg) {
        return success(msg, null);
    }


    public static <T> SupermarketResult<T> failure(Integer code, String msg) {
        return failure(code, msg, null);
    }

    public static <T> SupermarketResult<T> failure(Integer code, String msg, T data) {
        SupermarketResult<T> result = new SupermarketResult<>();
        result.status = code;
        result.msg = msg;
        result.data = data;
        return result;
    }

}
