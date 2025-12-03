package org.cityu.supermarket.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Promotion entity
 *
 * @version 0.0.1
 * @date 2025-12-01
 */
@Data
public class Promotion {
    private String promotionId;
    private String promotionName;
    private Integer promotionType;   // Promo type: 1=threshold discount, 2=percentage off, 3=buy-get-free, 4=member special
    private String description;     // Activity description

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startTime;         // Start time

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;           // End time

    private BigDecimal minAmount;  // Minimum spending amount
    private BigDecimal discountRate; // Discount rate
    private BigDecimal discountAmount; // Discount amount
    private Integer memberOnly;     // Members only: 0=no, 1=yes
    private Integer status;         // Status: 0=not started, 1=active, 2=ended, 3=cancelled

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    // Related promotion products
    private List<PromotionProduct> products;

    // Explicit setters for compilation
    public void setPromotionId(String promotionId) {
        this.promotionId = promotionId;
    }

    public void setPromotionName(String promotionName) {
        this.promotionName = promotionName;
    }

    public void setPromotionType(Integer promotionType) {
        this.promotionType = promotionType;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public void setMinAmount(BigDecimal minAmount) {
        this.minAmount = minAmount;
    }

    public void setDiscountRate(BigDecimal discountRate) {
        this.discountRate = discountRate;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public void setMemberOnly(Integer memberOnly) {
        this.memberOnly = memberOnly;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public void setProducts(List<PromotionProduct> products) {
        this.products = products;
    }

    // Explicit getters for JSON serialization
    public String getPromotionId() {
        return promotionId;
    }

    public String getPromotionName() {
        return promotionName;
    }

    public Integer getPromotionType() {
        return promotionType;
    }

    public String getDescription() {
        return description;
    }

    public Date getStartTime() {
        return startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public BigDecimal getMinAmount() {
        return minAmount;
    }

    public BigDecimal getDiscountRate() {
        return discountRate;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public Integer getMemberOnly() {
        return memberOnly;
    }

    public Integer getStatus() {
        return status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public List<PromotionProduct> getProducts() {
        return products;
    }
}