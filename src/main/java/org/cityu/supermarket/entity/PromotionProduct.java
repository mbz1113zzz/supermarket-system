package org.cityu.supermarket.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Promotion product entity
 *
 * @version 0.0.1
 * @date 2025-12-01
 */
@Data
public class PromotionProduct {
    private Integer id;
    private String promotionId;
    private Integer productId;
    private String productName;     // Redundant field for frontend display
    private String productCategory; // Redundant field for frontend display
    private BigDecimal specialPrice; // Special price
    private Integer buyQuantity;    // How many to buy
    private Integer freeQuantity;   // How many free
    private BigDecimal discountRate; // Discount rate

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
}