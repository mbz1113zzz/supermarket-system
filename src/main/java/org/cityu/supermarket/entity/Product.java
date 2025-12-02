package org.cityu.supermarket.entity;

import java.io.Serializable;
import java.util.Date;


/**
 * Product entity
 *
 * @version 0.0.1
 * @date 2025-12-01
 */
public class Product implements Serializable {
    /**
     * Maps to productid in DB
     */
    private Integer productId;

    private String name;

    private Integer price;

    private Integer integral;

    private String category;

    private Integer stock;

    private String unit;

    private Date time;

    private static final long serialVersionUID = 1L;

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getIntegral() {
        return integral;
    }

    public void setIntegral(Integer integral) {
        this.integral = integral;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category == null ? null : category.trim();
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit == null ? null : unit.trim();
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Product{" +
                "productId=" + productId +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", integral=" + integral +
                ", category='" + category + '\'' +
                ", stock=" + stock +
                ", unit='" + unit + '\'' +
                ", time=" + time +
                '}';
    }
}