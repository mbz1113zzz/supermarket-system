package org.cityu.supermarket.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Purchase order entity
 *
 * @version 0.0.1
 * @date 2025-12-01
 */
@Data
public class PurchaseOrder {
    private String purchaseId;
    private String supplierId;
    private String supplierName;   // Redundant field for frontend display
    private String operator;        // Operator
    private String remark;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date orderDate;         // Order date

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date expectedDate;      // Expected arrival date

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date actualDate;        // Actual arrival date

    private BigDecimal totalAmount; // Total purchase amount
    private Integer status;          // Status: 1=pending, 2=confirmed, 3=received, 4=cancelled

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    // Related order items
    private List<PurchaseOrderItem> items;

    // Explicit setters for compilation
    public void setPurchaseId(String purchaseId) {
        this.purchaseId = purchaseId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public void setExpectedDate(Date expectedDate) {
        this.expectedDate = expectedDate;
    }

    public void setActualDate(Date actualDate) {
        this.actualDate = actualDate;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
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

    public void setItems(List<PurchaseOrderItem> items) {
        this.items = items;
    }

    // Explicit getters for JSON serialization
    public String getPurchaseId() {
        return purchaseId;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public String getOperator() {
        return operator;
    }

    public String getRemark() {
        return remark;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public Date getExpectedDate() {
        return expectedDate;
    }

    public Date getActualDate() {
        return actualDate;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
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

    public List<PurchaseOrderItem> getItems() {
        return items;
    }
}