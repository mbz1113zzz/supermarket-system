package org.cityu.supermarket.mapper;

import org.cityu.supermarket.entity.OrderItem;

import java.util.List;

/**
 * Order item DAO interface
 * @version 0.0.1
 * @date 2025-11-01
 */
public interface OrderItemMapper {
    /**
     * Add new order item
     * @param orderItem
     */
    void insertOrderItem(OrderItem orderItem);

    /**
     * Batch insert order items
     * @param orderItems
     */
    void batchInsertOrderItem(List<OrderItem> orderItems);

    /**
     * Get order items by order ID
     * @param orderid
     * @return
     */
    List<OrderItem> selectOrderItemsByOrderId(String orderid);

    /**
     * Delete order items by order ID
     * @param orderid
     */
    void deleteOrderItemsByOrderId(String orderid);

    /**
     * Get order items by product ID
     * @param productid
     * @return
     */
    List<OrderItem> selectOrderItemsByProductId(Integer productid);

    /**
     * Update order item
     * @param orderItem
     */
    void updateOrderItem(OrderItem orderItem);

    /**
     * Delete order item by ID
     * @param itemId
     */
    void deleteOrderItemById(Integer itemId);
}