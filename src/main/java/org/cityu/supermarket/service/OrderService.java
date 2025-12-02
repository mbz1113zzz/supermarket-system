package org.cityu.supermarket.service;

import org.cityu.supermarket.common.vo.SupermarketResult;
import org.cityu.supermarket.entity.Order;
import org.cityu.supermarket.entity.PageResult;

import java.util.List;

/**
 * Order service contract for CRUD, queries, and statistics.
 * @version 0.0.1
 * @date 2025-11-01
 */
public interface OrderService {
    /**
     * Create a new order.
     * @param order order payload
     * @return execution result with persisted order
     */
    SupermarketResult<Order> createOrder(Order order);

    /**
     * Retrieve every order without pagination (service use only).
     * @return order collection
     */
    List<Order> getAllOrders();

    /**
     * Query order details by primary id.
     * @param orderid order id
     * @return order details
     */
    SupermarketResult<Order> getOrderById(String orderid);

    /**
     * Query orders by membership card id.
     * @param cardid membership card id
     * @return orders tied to the card
     */
    SupermarketResult<List<Order>> getOrdersByCardId(String cardid);

    /**
     * Update order status.
     * @param orderid order id
     * @param status new status code
     * @return execution result
     */
    SupermarketResult<?> updateOrderStatus(String orderid, Integer status);

    /**
     * Cancel an order.
     * @param orderid order id
     * @return execution result
     */
    SupermarketResult<?> cancelOrder(String orderid);

    /**
     * Delete an order record.
     * @param orderid order id
     * @return execution result
     */
    SupermarketResult<?> deleteOrder(String orderid);

    /**
     * Query orders placed inside a time window.
     * @param startTime inclusive start timestamp
     * @param endTime inclusive end timestamp
     * @return orders inside range
     */
    SupermarketResult<List<Order>> getOrdersByTimeRange(String startTime, String endTime);

    /**
     * Count total orders.
     * @return order count
     */
    Integer countOrders();

    /**
     * Aggregate total order value inside a time window.
     * @param startTime inclusive start timestamp
     * @param endTime inclusive end timestamp
     * @return total amount within range
     */
    Integer sumAmountByTimeRange(String startTime, String endTime);
}