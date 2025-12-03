package org.cityu.supermarket.dto.request.coupon;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

/**
 * DTO for updating a coupon
 * 
 * @version 0.0.2
 * @date 2025-12-01
 */
@Data
public class CouponUpdateRequest {
    
    @NotBlank(message = "Coupon ID must not be empty")
    private String couponId;
    
    @Size(max = 100, message = "Coupon name must not exceed 100 characters")
    private String couponName;
    
    @Min(value = 1, message = "Coupon type value is invalid")
    @Max(value = 3, message = "Coupon type value is invalid")
    private Integer couponType;
    
    @DecimalMin(value = "0.00", message = "Denomination cannot be negative")
    private BigDecimal denomination;
    
    @DecimalMin(value = "0.00", message = "Discount rate cannot be negative")
    @DecimalMax(value = "1.00", message = "Discount rate cannot exceed 1")
    private BigDecimal discountRate;
    
    @DecimalMin(value = "0.00", message = "Minimum spend amount cannot be negative")
    private BigDecimal minAmount;
    
    @Min(value = 1, message = "Valid days must be at least 1")
    @Max(value = 365, message = "Valid days cannot exceed 365")
    private Integer validDays;
    
    @Min(value = 1, message = "Total quantity must be at least 1")
    private Integer totalQuantity;
    
    @Min(value = 0, message = "Status value is invalid")
    @Max(value = 1, message = "Status value is invalid")
    private Integer status;
}
