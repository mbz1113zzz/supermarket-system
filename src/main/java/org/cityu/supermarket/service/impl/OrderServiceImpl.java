package org.cityu.supermarket.service.impl;

import org.cityu.supermarket.mapper.*;
import org.cityu.supermarket.common.vo.SupermarketResult;
import org.cityu.supermarket.entity.*;
import org.cityu.supermarket.entity.TransactionRecord;
import org.cityu.supermarket.service.OrderService;
import org.cityu.supermarket.service.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Order service implementation layer.
 * @version 0.0.1
 * @date 2025-11-01
 */
@Service
public class OrderServiceImpl implements OrderService {
    @Resource
    private OrderMapper orderMapper;
    @Resource
    private OrderItemMapper orderItemMapper;
    @Resource
    private ProductService productService;
    @Resource
    private CardMapper cardMapper;
    @Resource
    private RecordMapper recordMapper;

    @Override
    @Transactional
    public SupermarketResult<Order> createOrder(Order order) {
        if (order.getCardid() == null || order.getCardid().trim().isEmpty()) {
            return SupermarketResult.failure(400, "Card ID can't be empty");
        }
        if (order.getOrderItems() == null || order.getOrderItems().isEmpty()) {
            return SupermarketResult.failure(400, "Order needs at least one item");
        }

        // TODO: scalability
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String orderid = sdf.format(new Date()) + UUID.randomUUID().toString().substring(0, 6);
        System.out.println("Generated Order ID: " + orderid); // debug log
        order.setOrderid(orderid);
        order.setOrderTime(new Date());
        order.setStatus(1);
        // TODO: magic number, use enum for status

        Integer totalAmount = 0;
        Integer totalIntegral = 0;

        for (OrderItem item : order.getOrderItems()) {
            Product product = productService.getProductById(item.getProductid().toString());
            
            if (product == null) {
                return SupermarketResult.failure(404, "Product not found: " + item.getProductid());
            }
            
            // check stock
            if (product.getStock() < item.getQuantity()) {
                return SupermarketResult.failure(400, "Not enough stock for " + product.getName());
            }

            // store item price
            item.setPrice(product.getPrice());
            item.calculateSubtotal();

            totalAmount += item.getSubtotal();
            totalIntegral += product.getIntegral() * item.getQuantity();
        }

        order.setTotalAmount(totalAmount);
        order.setTotalIntegral(totalIntegral);

        // process payment via card
        Card card = cardMapper.selectCardById(order.getCardid());
        if (card == null) {
            return SupermarketResult.failure(404, "Card not found");
        }

        // ensure card not lost
        if (card.getLose() != null && card.getLose() == 1) {
            return SupermarketResult.failure(400, "Card is reported lost");
        }

        // payment: 0=balance, 1=points
        // rule: 100 points = 1 yuan
        final int INTEGRAL_TO_CENT_RATIO = 1; // 1 point = 1 cent
        
        Integer paymentMethod = order.getPaymentMethod();
        if (paymentMethod == null) {
            paymentMethod = 0;
        }
        
        if (paymentMethod == 0) { // balance payment
            if (card.getBalance() < totalAmount) {
                return SupermarketResult.failure(400, "Balance too low, got " + card.getBalance() / 100.0 + " need " + totalAmount / 100.0);
            }
            // deduct balance
            card.setBalance(card.getBalance() - totalAmount);
            // balance payment earns points
            card.setIntegral(card.getIntegral() + totalIntegral);
        } else if (paymentMethod == 1) { // points payment
            // required points = amount / ratio
            int requiredIntegral = totalAmount / INTEGRAL_TO_CENT_RATIO;
            if (card.getIntegral() < requiredIntegral) {
                return SupermarketResult.failure(400, "Not enough points, got " + card.getIntegral() + " need " + requiredIntegral);
            }
            // deduct points, no new points
            card.setIntegral(card.getIntegral() - requiredIntegral);
            // persist actual points used
            order.setTotalIntegral(requiredIntegral);
        } else {
            // unsupported payment method
            return SupermarketResult.failure(400, "Unsupported payment method: " + paymentMethod);
        }

        // persist card update
        cardMapper.updateCardById(card);

        // reduce stock fail rollback
        for (OrderItem item : order.getOrderItems()) {
            SupermarketResult<?> stockResult = productService.decreaseStock(item.getProductid().toString(), item.getQuantity());
            if (stockResult.getStatus() != 200) {
                throw new RuntimeException("Failed to deduct stock: " + stockResult.getMsg());
            }
        }

        // persist order header
        orderMapper.insertOrder(order);

        // persist order items
        for (OrderItem item : order.getOrderItems()) {
            item.setOrderid(orderid);
            orderItemMapper.insertOrderItem(item);
        }

        // TODO: clearer model
        TransactionRecord record = new TransactionRecord();
        record.setCardId(order.getCardid());
        record.setValue(-totalAmount);  // Negative = expense
        record.setTime(new Date());
        record.setSpendType(order.getPaymentMethod());
        record.setOrderid(orderid);
        record.setDescription("Product purchase order");
        // TODO: detailed desc with product name
        recordMapper.insertRecord(record);

        return SupermarketResult.success(order);
    }

    @Override
    public List<Order> getAllOrders() {
        return orderMapper.selectAllOrder();
    }

    @Override
    public SupermarketResult<Order> getOrderById(String orderid) {
        Order order = orderMapper.selectOrderById(orderid);
        if (order != null) {
            return SupermarketResult.success(order);
        } else {
            return SupermarketResult.failure(404, "Order not found");
        }
    }

    @Override
    public SupermarketResult<List<Order>> getOrdersByCardId(String cardid) {
        List<Order> orders = orderMapper.selectOrdersByCardId(cardid);
        return SupermarketResult.success(orders);
    }

    @Override
    public SupermarketResult<?> updateOrderStatus(String orderid, Integer status) {
        orderMapper.updateOrderStatus(orderid, status);
        return SupermarketResult.success();
    }

    @Override
    @Transactional
    public SupermarketResult<?> cancelOrder(String orderid) {
        // cancel order: restore stock + refund
        Order order = orderMapper.selectOrderById(orderid);
        if (order == null) {
            return SupermarketResult.failure(404, "Order does not exist");
        }

        if (order.getStatus() != 1) {
            return SupermarketResult.failure(400, "Current order status cannot be canceled");
        }

        // replenish stock
        List<OrderItem> items = orderItemMapper.selectOrderItemsByOrderId(orderid);
        for (OrderItem item : items) {
            Product product = productService.getProductById(item.getProductid().toString());
            if (product != null) {
                productService.updateStock(item.getProductid().toString(), product.getStock() + item.getQuantity());
            }
        }

        // refund: return balance or points
        Card card = cardMapper.selectCardById(order.getCardid());
        if (card != null) {
            final int INTEGRAL_TO_CENT_RATIO = 1;
            if (order.getPaymentMethod() == 0) {
                // balance payment: refund money, deduct gifted points
                card.setBalance(card.getBalance() + order.getTotalAmount());
                // remove points earned
                int earnedIntegral = order.getTotalIntegral() != null ? order.getTotalIntegral() : 0;
                card.setIntegral(Math.max(0, card.getIntegral() - earnedIntegral));
            } else if (order.getPaymentMethod() == 1) {
                // points payment: refund spent points
                int refundIntegral = order.getTotalAmount() / INTEGRAL_TO_CENT_RATIO;
                card.setIntegral(card.getIntegral() + refundIntegral);
            }
            cardMapper.updateCardById(card);

            // record refund transaction
            TransactionRecord refundRecord = new TransactionRecord();
            refundRecord.setCardId(order.getCardid());
            refundRecord.setValue(order.getTotalAmount()); // Positive = refund
            refundRecord.setTime(new Date());
            refundRecord.setSpendType(order.getPaymentMethod());
            refundRecord.setOrderid(orderid);
            refundRecord.setDescription("Order cancellation refund");
            recordMapper.insertRecord(refundRecord);
        }

        // mark order canceled status 2
        orderMapper.updateOrderStatus(orderid, 2);

        return SupermarketResult.success("Order canceled and refund completed");
    }

    @Override
    @Transactional
    public SupermarketResult<?> deleteOrder(String orderid) {
        // delete items first
        orderItemMapper.deleteOrderItemsByOrderId(orderid);
        // then delete order itself
        orderMapper.deleteOrderById(orderid);
        return SupermarketResult.success();
    }

    @Override
    public SupermarketResult<List<Order>> getOrdersByTimeRange(String startTime, String endTime) {
        List<Order> orders = orderMapper.selectOrdersByTimeRange(startTime, endTime);
        return SupermarketResult.success(orders);
    }

    @Override
    public Integer countOrders() {
        return orderMapper.countOrders();
    }

    @Override
    public Integer sumAmountByTimeRange(String startTime, String endTime) {
        return orderMapper.sumAmountByTimeRange(startTime, endTime);
    }
}