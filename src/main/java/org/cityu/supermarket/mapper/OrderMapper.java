package org.cityu.supermarket.mapper;

import org.cityu.supermarket.entity.Order;

import java.util.List;

/**
 * Order data access interface
 * @version 0.0.1
 * @date 2025-11-01
 */
public interface OrderMapper {
    /**
     * insert order
     * @param order
     */
    void insertOrder(Order order);

    /**
     * get all orders
     * @return
     */
    List<Order> selectAllOrder();

    /**
     * get order by ID (includes items)
     * @param orderid
     * @return
     */
    Order selectOrderById(String orderid);

    /**
     * get orders by card ID
     * @param cardid
     * @return
     */
    List<Order> selectOrdersByCardId(String cardid);

    /**
     * update order status
     * @param orderid
     * @param status
     */
    void updateOrderStatus(String orderid, Integer status);

    /**
     * delete order (cascade deletes items)
     * @param orderid
     */
    void deleteOrderById(String orderid);

    /**
     * delete orders by card ID (cascade deletes items)
     * @param cardid
     */
    void deleteOrdersByCardId(String cardid);

    /**
     * get orders by time range
     * @param startTime start time
     * @param endTime end time
     * @return
     */
    List<Order> selectOrdersByTimeRange(String startTime, String endTime);

    /**
     * count total orders
     * @return
     */
    Integer countOrders();

    /**
     * sum total amount in time range
     * @param startTime start time
     * @param endTime end time
     * @return
     */
    Integer sumAmountByTimeRange(String startTime, String endTime);
}