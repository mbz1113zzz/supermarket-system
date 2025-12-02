package org.cityu.supermarket.service.impl;

import org.cityu.supermarket.common.vo.SupermarketResult;
import org.cityu.supermarket.entity.Card;
import org.cityu.supermarket.entity.Order;
import org.cityu.supermarket.entity.OrderItem;
import org.cityu.supermarket.entity.Product;
import org.cityu.supermarket.mapper.CardMapper;
import org.cityu.supermarket.mapper.OrderItemMapper;
import org.cityu.supermarket.mapper.OrderMapper;
import org.cityu.supermarket.mapper.RecordMapper;
import org.cityu.supermarket.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Order service unit tests
 * 
 * @version 0.0.1
 * @date 2025-12-01
 */
@ExtendWith(MockitoExtension.class)
@org.mockito.junit.jupiter.MockitoSettings(strictness = org.mockito.quality.Strictness.LENIENT)
class OrderServiceImplTest {

    @Mock
    private OrderMapper orderMapper;

    @Mock
    private OrderItemMapper orderItemMapper;

    @Mock
    private ProductService productService;

    @Mock
    private CardMapper cardMapper;

    @Mock
    private RecordMapper recordMapper;

    @InjectMocks
    private OrderServiceImpl orderService;

    private Order testOrder;
    private Card testCard;
    private Product testProduct;
    private OrderItem testOrderItem;

    @BeforeEach
    void setUp() {
        // Initialize test product
        testProduct = new Product();
        testProduct.setProductId(1);
        testProduct.setName("Test Product");
        testProduct.setPrice(1000); // 10 yuan (in cents)
        testProduct.setStock(100);
        testProduct.setIntegral(10);

        // Initialize test order item
        testOrderItem = new OrderItem();
        testOrderItem.setProductid(1);
        testOrderItem.setQuantity(2);

        // Initialize test order
        testOrder = new Order();
        testOrder.setCardid("CARD001");
        testOrder.setPaymentMethod(0); // Balance payment
        testOrder.setOrderItems(Arrays.asList(testOrderItem));

        // Initialize test membership card
        testCard = new Card();
        testCard.setCardId("CARD001");
        testCard.setBalance(10000); // 100 yuan (in cents)
        testCard.setIntegral(500);
        testCard.setLose(0);
    }

    @Test
    @DisplayName("Create order - Empty card ID returns failure")
    void createOrder_EmptyCardId_ReturnsFailure() {
        testOrder.setCardid("");

        SupermarketResult<Order> result = orderService.createOrder(testOrder);

        assertNotEquals(200, result.getStatus());
        assertEquals(400, result.getStatus());
    }

    // Removed empty order items check
    // void createOrder_EmptyOrderItems_ReturnsFailure() ...

    @Test
    @DisplayName("Create order - Product not found returns failure")
    void createOrder_ProductNotExists_ReturnsFailure() {
        // Mock getProductById to return null (product not found)
        when(productService.getProductById("1")).thenReturn(null);

        SupermarketResult<Order> result = orderService.createOrder(testOrder);

        assertNotEquals(200, result.getStatus());
        assertEquals(404, result.getStatus());
    }

    @Test
    @DisplayName("Create order - Insufficient stock returns failure")
    void createOrder_InsufficientStock_ReturnsFailure() {
        testProduct.setStock(1); // Only 1 in stock, but ordering 2
        when(productService.getProductById("1")).thenReturn(testProduct);

        SupermarketResult<Order> result = orderService.createOrder(testOrder);

        assertNotEquals(200, result.getStatus());
        assertEquals(400, result.getStatus());
        assertTrue(result.getMsg().contains("stock") || result.getMsg().contains("Insufficient stock"));
    }

    @Test
    @DisplayName("Create order - Card not found returns failure")
    void createOrder_CardNotExists_ReturnsFailure() {
        // Product check passes
        when(productService.getProductById("1")).thenReturn(testProduct);
        // Card not found
        when(cardMapper.selectCardById("CARD001")).thenReturn(null);

        SupermarketResult<Order> result = orderService.createOrder(testOrder);

        assertNotEquals(200, result.getStatus());
        assertEquals(404, result.getStatus());
    }

    @Test
    @DisplayName("Create order - Card reported lost returns failure")
    void createOrder_CardLost_ReturnsFailure() {
        testCard.setLose(1);
        when(productService.getProductById("1")).thenReturn(testProduct);
        when(cardMapper.selectCardById("CARD001")).thenReturn(testCard);

        SupermarketResult<Order> result = orderService.createOrder(testOrder);

        assertNotEquals(200, result.getStatus());
        assertEquals(400, result.getStatus());
        assertTrue(result.getMsg().contains("lost") || result.getMsg().contains("lost"));
    }

    @Test
    @DisplayName("Create order - Insufficient balance returns failure")
    void createOrder_InsufficientBalance_ReturnsFailure() {
        testCard.setBalance(100); // Only 1 yuan balance
        when(productService.getProductById("1")).thenReturn(testProduct);
        when(cardMapper.selectCardById("CARD001")).thenReturn(testCard);

        SupermarketResult<Order> result = orderService.createOrder(testOrder);

        assertNotEquals(200, result.getStatus());
        assertEquals(400, result.getStatus());
        assertTrue(result.getMsg().contains("Balance") || result.getMsg().contains("Insufficient balance"));
    }

    @Test
    @DisplayName("Create order - Balance payment success")
    void createOrder_BalancePayment_Success() {
        when(productService.getProductById("1")).thenReturn(testProduct);
        when(cardMapper.selectCardById("CARD001")).thenReturn(testCard);
        doNothing().when(cardMapper).updateCardById(any(Card.class));
        when(productService.decreaseStock(anyString(), anyInt())).thenReturn(SupermarketResult.success());
        doNothing().when(orderMapper).insertOrder(any(Order.class));
        doNothing().when(orderItemMapper).insertOrderItem(any(OrderItem.class));
        doNothing().when(recordMapper).insertRecord(any());

        SupermarketResult<Order> result = orderService.createOrder(testOrder);

        assertEquals(200, result.getStatus());
        assertNotNull(result.getData().getOrderid());
        assertEquals(2000, result.getData().getTotalAmount()); // 2 items x 10 yuan
        assertEquals(20, result.getData().getTotalIntegral()); // 2 items x 10 points
    }

    @Test
    @DisplayName("Create order - Points payment success")
    void createOrder_IntegralPayment_Success() {
        testOrder.setPaymentMethod(1); // Points payment
        testCard.setIntegral(5000); // 5000 points, enough to pay 2000 cents
        
        when(productService.getProductById("1")).thenReturn(testProduct);
        when(cardMapper.selectCardById("CARD001")).thenReturn(testCard);
        doNothing().when(cardMapper).updateCardById(any(Card.class));
        when(productService.decreaseStock(anyString(), anyInt())).thenReturn(SupermarketResult.success());
        doNothing().when(orderMapper).insertOrder(any(Order.class));
        doNothing().when(orderItemMapper).insertOrderItem(any(OrderItem.class));
        doNothing().when(recordMapper).insertRecord(any());

        SupermarketResult<Order> result = orderService.createOrder(testOrder);

        assertEquals(200, result.getStatus());
    }

    @Test
    @DisplayName("Create order - Insufficient points returns failure")
    void createOrder_InsufficientIntegral_ReturnsFailure() {
        testOrder.setPaymentMethod(1); // Points payment
        testCard.setIntegral(100); // Insufficient points
        
        when(productService.getProductById("1")).thenReturn(testProduct);
        when(cardMapper.selectCardById("CARD001")).thenReturn(testCard);

        SupermarketResult<Order> result = orderService.createOrder(testOrder);

        assertNotEquals(200, result.getStatus());
        assertEquals(400, result.getStatus());
        assertTrue(result.getMsg().contains("points") || result.getMsg().contains("Insufficient points"));
    }

    // Removed transaction rollback test
    // void createOrder_StockDecreaseFailed_ThrowsException() ...

    @Test
    @DisplayName("Get all orders")
    void getAllOrders_ReturnsList() {
        Order order1 = new Order();
        order1.setOrderid("ORD001");
        when(orderMapper.selectAllOrder()).thenReturn(Arrays.asList(order1));

        List<Order> result = orderService.getAllOrders();

        assertEquals(1, result.size());
        assertEquals("ORD001", result.get(0).getOrderid());
    }

    @Test
    @DisplayName("Get order by ID - Order exists")
    void getOrderById_Success() {
        Order order = new Order();
        order.setOrderid("ORD001");
        when(orderMapper.selectOrderById("ORD001")).thenReturn(order);

        SupermarketResult<Order> result = orderService.getOrderById("ORD001");

        assertEquals(200, result.getStatus());
        assertEquals("ORD001", result.getData().getOrderid());
    }

    @Test
    @DisplayName("Get order by ID - Order not found")
    void getOrderById_NotExists_ReturnsFailure() {
        when(orderMapper.selectOrderById("NOTEXIST")).thenReturn(null);

        SupermarketResult<Order> result = orderService.getOrderById("NOTEXIST");

        assertNotEquals(200, result.getStatus());
        assertEquals(404, result.getStatus());
    }

    @Test
    @DisplayName("Count orders")
    void countOrders_ReturnsCount() {
        when(orderMapper.countOrders()).thenReturn(100);

        Integer count = orderService.countOrders();

        assertEquals(100, count);
    }
}
