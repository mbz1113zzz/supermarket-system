package org.cityu.supermarket.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.cityu.supermarket.common.constants.ResultCode;
import org.cityu.supermarket.common.vo.SupermarketResult;
import org.cityu.supermarket.entity.Order;
import org.cityu.supermarket.entity.PageResult;
import org.cityu.supermarket.service.OrderService;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import java.util.List;

/**
 * Order controller
 * @version 0.0.1
 * @date 2025-11-01
 */
@CrossOrigin
@RestController
@RequestMapping("/orders")
public class OrderController {
    @Resource
    private OrderService orderService;

    /**
     * Create order
     * @param order order info
     * @return result
     */
    @PostMapping
    public SupermarketResult<Order> createOrder(@RequestBody Order order) {
        return orderService.createOrder(order);
    }

    /**
     * Get all orders (paginated)
     * @param pageIndex page number (1-based)
     * @param pageSize items per page
     * @return order list and total count
     */
    @GetMapping
    public SupermarketResult<PageResult<Order>> getAllOrders(@RequestParam(defaultValue = "1") String pageIndex, 
                                                           @RequestParam(defaultValue = "10") String pageSize) {
        try {
            // Parse pagination params
            int pageNum = 1;
            int size = 10;
            
            if (pageIndex != null && !pageIndex.isEmpty()) {
                pageNum = Integer.parseInt(pageIndex);
            }
            if (pageSize != null && !pageSize.isEmpty()) {
                size = Integer.parseInt(pageSize);
            }
            
            // Using PageHelper for pagination
            PageHelper.startPage(pageNum, size);
            List<Order> orders = orderService.getAllOrders();
            PageInfo<Order> pageInfo = new PageInfo<>(orders);
            
            // Wrap pagination result
            PageResult<Order> pageResult = new PageResult<>();
            pageResult.setList(orders);
            pageResult.setPageTotal(pageInfo.getTotal());
            
            return SupermarketResult.success(pageResult);
        } catch (NumberFormatException e) {
            return SupermarketResult.failure(ResultCode.PARAMETER_ERROR, "Invalid pagination parameter format");
        }
    }

    /**
     * Get order by ID
     * @param orderid order ID
     * @return order details
     */
    @GetMapping("/{orderid}")
    public SupermarketResult<Order> getOrderById(@PathVariable String orderid) {
        return orderService.getOrderById(orderid);
    }

    /**
     * Get orders by card ID
     * @param cardid card ID
     * @return order list
     */
    @GetMapping("/card/{cardid}")
    public SupermarketResult<List<Order>> getOrdersByCardId(@PathVariable String cardid) {
        return orderService.getOrdersByCardId(cardid);
    }

    /**
     * Update order status
     * @param orderid order ID
     * @param status new status
     * @return result
     */
    @PatchMapping("/{orderid}/status")
    public SupermarketResult<?> updateOrderStatus(@PathVariable String orderid, @RequestParam Integer status) {
        return orderService.updateOrderStatus(orderid, status);
    }

    /**
     * Cancel order
     * @param orderid order ID
     * @return result
     */
    @PostMapping("/{orderid}/cancel")
    public SupermarketResult<?> cancelOrder(@PathVariable String orderid) {
        return orderService.cancelOrder(orderid);
    }

    /**
     * Delete order
     * @param orderid order ID
     * @return result
     */
    @DeleteMapping("/{orderid}")
    public SupermarketResult<?> deleteOrder(@PathVariable String orderid) {
        return orderService.deleteOrder(orderid);
    }

    /**
     * Get orders by time range
     * @param startTime start time
     * @param endTime end time
     * @return order list
     */
    @GetMapping("/search/time")
    public SupermarketResult<List<Order>> getOrdersByTimeRange(@RequestParam String startTime, @RequestParam String endTime) {
        return orderService.getOrdersByTimeRange(startTime, endTime);
    }

    /**
     * Count total orders
     * @return total order count
     */
    @GetMapping("/count")
    public SupermarketResult<Integer> countOrders() {
        Integer count = orderService.countOrders();
        return SupermarketResult.success(count);
    }

    /**
     * Sum order amount by time range
     * @param startTime start time
     * @param endTime end time
     * @return total amount
     */
    @GetMapping("/amount/sum")
    public SupermarketResult<Integer> sumAmountByTimeRange(@RequestParam String startTime, @RequestParam String endTime) {
        Integer amount = orderService.sumAmountByTimeRange(startTime, endTime);
        return SupermarketResult.success(amount);
    }
}