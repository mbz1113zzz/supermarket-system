package org.cityu.supermarket.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Order entity
 *
 * @version 0.0.1
 * @date 2025-12-01
 */
public class Order implements Serializable {
    /**
     * Order ID
     */
    private String orderid;

    /**
     * Member card ID
     */
    private String cardid;

    /**
     * Total amount in cents
     */
    private Integer totalAmount;

    /**
     * Total points
     */
    private Integer totalIntegral;

    /**
     * Payment method: 0=balance, 1=points, 2=mixed
     */
    private Integer paymentMethod;

    /**
     * Order time
     */
    private Date orderTime;

    /**
     * Order status: 1=completed, 2=cancelled
     */
    private Integer status;

    /**
     * Order item list
     */
    private List<OrderItem> orderItems;

    /**
     * Member info (for joined queries)
     */
    private Member member;

    /**
     * Card info (for joined queries)
     */
    private Card card;

    private static final long serialVersionUID = 1L;

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid == null ? null : orderid.trim();
    }

    public String getCardid() {
        return cardid;
    }

    public void setCardid(String cardid) {
        this.cardid = cardid == null ? null : cardid.trim();
    }

    public Integer getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Integer totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Integer getTotalIntegral() {
        return totalIntegral;
    }

    public void setTotalIntegral(Integer totalIntegral) {
        this.totalIntegral = totalIntegral;
    }

    public Integer getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(Integer paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Date getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    /**
     * Get payment method description
     */
    public String getPaymentMethodDesc() {
        if (paymentMethod == null) {
            return "Unknown";
        }
        switch (paymentMethod) {
            case 0:
                return "Balance payment";
            case 1:
                return "Points payment";
            case 2:
                return "Mixed payment";
            default:
                return "Unknown";
        }
    }

    /**
     * Get order status description
     */
    public String getStatusDesc() {
        if (status == null) {
            return "Unknown";
        }
        switch (status) {
            case 1:
                return "Completed";
            case 2:
                return "Canceled";
            default:
                return "Unknown";
        }
    }

    /**
     * Get total amount in yuan
     */
    public Double getTotalAmountInYuan() {
        return totalAmount != null ? totalAmount / 100.0 : 0.0;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderid='" + orderid + '\'' +
                ", cardid='" + cardid + '\'' +
                ", totalAmount=" + totalAmount +
                ", totalIntegral=" + totalIntegral +
                ", paymentMethod=" + paymentMethod +
                ", orderTime=" + orderTime +
                ", status=" + status +
                '}';
    }
}