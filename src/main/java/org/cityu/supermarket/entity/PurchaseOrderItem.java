package org.cityu.supermarket.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Purchase order item entity
 *
 * @version 0.0.1
 * @date 2025-12-01
 */
@Data
public class PurchaseOrderItem {
    private Integer itemId;
    private String purchaseId;
    private Integer productId;
    private String productName;     // Redundant field, makes frontend display easier
    private String productCategory; // Redundant field, makes frontend display easier
    private Integer quantity;       // How many to buy
    private BigDecimal unitPrice;   // Unit price
    private BigDecimal totalPrice;  // Subtotal

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    // Explicit getters for JSON serialization
    public Integer getItemId() {
        return itemId;
    }

    public String getPurchaseId() {
        return purchaseId;
    }

    public Integer getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public Date getCreateTime() {
        return createTime;
    }
}