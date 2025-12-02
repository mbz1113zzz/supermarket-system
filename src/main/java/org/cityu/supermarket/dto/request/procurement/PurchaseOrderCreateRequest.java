package org.cityu.supermarket.dto.request.procurement;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * DTO for creating a purchase order
 * 
 * @version 0.0.1
 * @date 2025-12-01
 */
public class PurchaseOrderCreateRequest {
    
    @NotNull(message = "Supplier ID must not be null")
    @Min(value = 1, message = "Supplier ID is invalid")
    private Integer supplierId;
    
    @NotEmpty(message = "Purchase items must not be empty")
    @Size(min = 1, max = 100, message = "Number of purchase items must be between 1 and 100")
    private List<PurchaseOrderItemRequest> items;
    
    private String remark;

    public Integer getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Integer supplierId) {
        this.supplierId = supplierId;
    }

    public List<PurchaseOrderItemRequest> getItems() {
        return items;
    }

    public void setItems(List<PurchaseOrderItemRequest> items) {
        this.items = items;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * Purchase order line item
     */
    public static class PurchaseOrderItemRequest {
        
        @NotBlank(message = "Product code must not be empty")
        private String productId;
        
        @NotNull(message = "Quantity must not be null")
        @Min(value = 1, message = "Quantity must be at least 1")
        @Max(value = 100000, message = "Quantity per order cannot exceed 100000")
        private Integer quantity;
        
        @NotNull(message = "Unit price must not be null")
        @DecimalMin(value = "0.01", message = "Unit price must be greater than 0")
        private BigDecimal unitPrice;

        public String getProductId() {
            return productId;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }

        public Integer getQuantity() {
            return quantity;
        }

        public void setQuantity(Integer quantity) {
            this.quantity = quantity;
        }

        public BigDecimal getUnitPrice() {
            return unitPrice;
        }

        public void setUnitPrice(BigDecimal unitPrice) {
            this.unitPrice = unitPrice;
        }
    }
}
