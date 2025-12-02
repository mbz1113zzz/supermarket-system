package org.cityu.supermarket.controller;

import org.cityu.supermarket.common.vo.SupermarketResult;
import org.cityu.supermarket.entity.PurchaseOrder;
import org.cityu.supermarket.dto.request.procurement.PurchaseOrderCreateRequest;
import org.cityu.supermarket.dto.request.procurement.PurchaseOrderUpdateRequest;
import org.cityu.supermarket.service.PurchaseOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Purchase order controller
 *
 * @version 0.0.1
 * @date 2025-12-01
 */
@Tag(name = "Purchase Order Module", description = "Purchase order management APIs")
@CrossOrigin
@RestController
@RequestMapping("/purchase-orders")
public class PurchaseOrderController {

    @Resource
    private PurchaseOrderService purchaseOrderService;

    /**
     * list purchase orders
     */
    @Operation(summary = "List purchase orders")
    @GetMapping
    public SupermarketResult<?> getPurchaseOrders(
            @RequestParam(required = false) String orderNumber,
            @RequestParam(required = false) String supplierId,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        try {
            List<PurchaseOrder> orders = purchaseOrderService.getPurchaseOrdersByCondition(orderNumber, supplierId, status, startDate, endDate);
            return SupermarketResult.success(orders);
        } catch (Exception e) {
            return SupermarketResult.failure(400, "Failed to fetch purchase order list", e.getMessage());
        }
    }

    /**
     * get purchase order by id
     */
    @Operation(summary = "Get purchase order by ID")
    @GetMapping("/{orderId}")
    public SupermarketResult<?> getPurchaseOrder(@PathVariable String orderId) {
        try {
            PurchaseOrder order = purchaseOrderService.getPurchaseOrderById(orderId);
            if (order == null) {
                return SupermarketResult.failure(404, "Purchase order not found");
            }
            return SupermarketResult.success(order);
        } catch (Exception e) {
            return SupermarketResult.failure(400, "Failed to fetch purchase order details", e.getMessage());
        }
    }

    /**
     * create purchase order
     */
    @Operation(summary = "Create purchase order")
    @PostMapping
    public SupermarketResult<?> addPurchaseOrder(@Valid @RequestBody PurchaseOrderCreateRequest request) {
        try {
            PurchaseOrder purchaseOrder = new PurchaseOrder();
            purchaseOrder.setSupplierId(String.valueOf(request.getSupplierId()));
            purchaseOrder.setRemark(request.getRemark());
            purchaseOrder.setPurchaseId("PO" + System.currentTimeMillis());
            purchaseOrderService.addPurchaseOrder(purchaseOrder);
            return SupermarketResult.success("Purchase order created successfully");
        } catch (Exception e) {
            return SupermarketResult.failure(400, "Failed to create purchase order", e.getMessage());
        }
    }

    /**
     * update purchase order
     */
    @Operation(summary = "Update purchase order")
    @PutMapping("/{orderId}")
    public SupermarketResult<?> updatePurchaseOrder(
            @PathVariable String orderId,
            @Valid @RequestBody PurchaseOrderUpdateRequest request) {
        try {
            int result = purchaseOrderService.updatePurchaseOrderStatus(orderId, request.getStatus());
            if (result > 0) {
                return SupermarketResult.success("Purchase order updated successfully");
            } else {
                return SupermarketResult.failure(400, "Purchase order not found or update failed");
            }
        } catch (Exception e) {
            return SupermarketResult.failure(400, "Failed to update purchase order", e.getMessage());
        }
    }

    /**
     * delete purchase order
     */
    @Operation(summary = "Delete purchase order")
    @DeleteMapping("/{orderId}")
    public SupermarketResult<?> deletePurchaseOrder(@PathVariable String orderId) {
        try {
            int result = purchaseOrderService.deletePurchaseOrder(orderId);
            if (result > 0) {
                return SupermarketResult.success("Purchase order deleted successfully");
            } else {
                return SupermarketResult.failure(400, "Purchase order not found or delete failed");
            }
        } catch (Exception e) {
            return SupermarketResult.failure(400, "Failed to delete purchase order", e.getMessage());
        }
    }

    /**
     * update purchase order status
     */
    @Operation(summary = "Update purchase order status")
    @PatchMapping("/{orderId}/status")
    public SupermarketResult<?> updateOrderStatus(
            @PathVariable String orderId,
            @RequestBody Map<String, Integer> body) {
        try {
            Integer status = body.get("status");
            if (status == null) {
                return SupermarketResult.failure(400, "Status is required");
            }
            int result = purchaseOrderService.updatePurchaseOrderStatus(orderId, status);
            if (result > 0) {
                return SupermarketResult.success("Order status updated successfully");
            } else {
                return SupermarketResult.failure(400, "Order not found or update failed");
            }
        } catch (Exception e) {
            return SupermarketResult.failure(400, "Failed to update order status", e.getMessage());
        }
    }
}
