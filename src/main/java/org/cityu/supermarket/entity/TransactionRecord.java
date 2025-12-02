package org.cityu.supermarket.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * Transaction record entity
 *
 * @version 0.0.1
 * @date 2025-12-01
 */
public class TransactionRecord implements Serializable {
    private Integer id;
    /**
     * Maps to cardid in DB
     */
    private String cardId;

    /**
     * Amount in cents - positive=recharge, negative=spending
     */
    private Integer value;

    /**
     * Transaction time
     */
    private Date time;

    /**
     * Maps to spendtype in DB
     * 0=cash, 1=points
     */
    private Integer spendType;

    /**
     * Related order ID
     */
    private String orderid;

    /**
     * Transaction description
     */
    private String description;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId == null ? null : cardId.trim();
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Integer getSpendType() {
        return spendType;
    }

    public void setSpendType(Integer spendType) {
        this.spendType = spendType;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid == null ? null : orderid.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    /**
     * Get transaction type description
     */
    public String getSpendTypeDesc() {
        if (spendType == null) {
            return "Unknown";
        }
        switch (spendType) {
            case 0:
                return "Cash (CNY)";
            case 1:
                return "Points";
            default:
                return "Unknown";
        }
    }

    /**
     * Get amount in yuan
     */
    public Double getValueInYuan() {
        return value != null ? value / 100.0 : 0.0;
    }

    /**
     * Check if it's an expense record
     */
    public boolean isExpense() {
        return value != null && value < 0;
    }

    /**
     * Check if it's a recharge record
     */
    public boolean isIncome() {
        return value != null && value > 0;
    }

    @Override
    public String toString() {
        return "Record{" +
                "id=" + id +
                ", cardId='" + cardId + '\'' +
                ", value=" + value +
                ", time=" + time +
                ", spendType=" + spendType +
                ", orderid='" + orderid + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}