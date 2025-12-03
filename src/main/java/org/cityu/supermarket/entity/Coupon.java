package org.cityu.supermarket.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Coupon entity
 *
 * @version 0.0.1
 * @date 2025-12-01
 */
@Data
public class Coupon {
    private String couponId;
    private String couponName;
    private Integer couponType;     // Coupon type: 1-threshold coupon 2-discount coupon 3-points coupon
    private BigDecimal denomination; // Face value
    private BigDecimal discountRate;  // Discount rate
    private BigDecimal minAmount;     // Minimum spending amount
    private Integer validDays;        // Valid days
    private Integer totalQuantity;    // Total issued quantity
    private Integer usedQuantity;     // Used quantity
    private Integer status;           // Status: 0-inactive 1-claimable

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    // Explicit setters for compilation
    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }

    public void setCouponName(String couponName) {
        this.couponName = couponName;
    }

    public void setCouponType(Integer couponType) {
        this.couponType = couponType;
    }

    public void setDenomination(BigDecimal denomination) {
        this.denomination = denomination;
    }

    public void setDiscountRate(BigDecimal discountRate) {
        this.discountRate = discountRate;
    }

    public void setMinAmount(BigDecimal minAmount) {
        this.minAmount = minAmount;
    }

    public void setValidDays(Integer validDays) {
        this.validDays = validDays;
    }

    public void setTotalQuantity(Integer totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public void setUsedQuantity(Integer usedQuantity) {
        this.usedQuantity = usedQuantity;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    // Explicit getters for JSON serialization
    public String getCouponId() {
        return couponId;
    }

    public String getCouponName() {
        return couponName;
    }

    public Integer getCouponType() {
        return couponType;
    }

    public BigDecimal getDenomination() {
        return denomination;
    }

    public BigDecimal getDiscountRate() {
        return discountRate;
    }

    public BigDecimal getMinAmount() {
        return minAmount;
    }

    public Integer getValidDays() {
        return validDays;
    }

    public Integer getTotalQuantity() {
        return totalQuantity;
    }

    public Integer getUsedQuantity() {
        return usedQuantity;
    }

    public Integer getStatus() {
        return status;
    }

    public Date getCreateTime() {
        return createTime;
    }
}