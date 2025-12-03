package org.cityu.supermarket.dto.request.promotion;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * DTO for updating a promotion
 * 
 * @version 0.0.2
 * @date 2025-12-01
 */
@Data
public class PromotionUpdateRequest {
    
    @NotBlank(message = "Promotion ID must not be empty")
    private String promotionId;
    
    @Size(max = 100, message = "Promotion name must not exceed 100 characters")
    private String promotionName;
    
    @Min(value = 1, message = "Promotion type value is invalid")
    @Max(value = 4, message = "Promotion type value is invalid")
    private Integer promotionType;
    
    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;
    
    private Date startTime;
    
    private Date endTime;
    
    @DecimalMin(value = "0.00", message = "Minimum spend amount cannot be negative")
    private BigDecimal minAmount;
    
    @DecimalMin(value = "0.00", message = "Discount rate cannot be negative")
    @DecimalMax(value = "1.00", message = "Discount rate cannot exceed 1")
    private BigDecimal discountRate;
    
    @DecimalMin(value = "0.00", message = "Discount amount cannot be negative")
    private BigDecimal discountAmount;
    
    @Min(value = 0, message = "Member-only flag is invalid")
    @Max(value = 1, message = "Member-only flag is invalid")
    private Integer memberOnly;
    
    @Min(value = 0, message = "Status value is invalid")
    @Max(value = 3, message = "Status value is invalid")
    private Integer status;
}
