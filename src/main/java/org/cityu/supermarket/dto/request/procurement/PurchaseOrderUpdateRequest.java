package org.cityu.supermarket.dto.request.procurement;

import jakarta.validation.constraints.*;

/**
 * DTO for updating purchase order status
 * 
 * @version 0.0.1
 * @date 2025-12-01
 */
public class PurchaseOrderUpdateRequest {
    
    @NotBlank(message = "Purchase order ID must not be empty")
    private String orderId;
    
    @NotNull(message = "Order status must not be null")
    @Min(value = 0, message = "Status value is invalid")
    @Max(value = 3, message = "Status value is invalid")
    private Integer status;
    
    private String remark;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
