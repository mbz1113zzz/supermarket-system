package org.cityu.supermarket.entity;

import java.io.Serializable;

/**
 * Order item entity
 *
 * @version 0.0.1
 * @date 2025-12-01
 */
public class OrderItem implements Serializable {
    /**
     * Order item ID
     */
    private Integer itemId;

    /**
     * Order ID
     */
    private String orderid;

    /**
     * Product ID
     */
    private Integer productid;

    /**
     * Quantity
     */
    private Integer quantity;

    /**
     * Unit price in cents at purchase time
     */
    private Integer price;

    /**
     * Subtotal in cents
     */
    private Integer subtotal;

    /**
     * Product info (for joined queries)
     */
    private Product product;

    private static final long serialVersionUID = 1L;

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid == null ? null : orderid.trim();
    }

    public Integer getProductid() {
        return productid;
    }

    public void setProductid(Integer productid) {
        this.productid = productid;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Integer subtotal) {
        this.subtotal = subtotal;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    /**
     * Get unit price in yuan
     */
    public Double getPriceInYuan() {
        return price != null ? price / 100.0 : 0.0;
    }

    /**
     * Get subtotal in yuan
     */
    public Double getSubtotalInYuan() {
        return subtotal != null ? subtotal / 100.0 : 0.0;
    }

    /**
     * Calculate subtotal
     */
    public void calculateSubtotal() {
        if (price != null && quantity != null) {
            this.subtotal = price * quantity;
        }
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "itemId=" + itemId +
                ", orderid='" + orderid + '\'' +
                ", productid=" + productid +
                ", quantity=" + quantity +
                ", price=" + price +
                ", subtotal=" + subtotal +
                '}';
    }
}