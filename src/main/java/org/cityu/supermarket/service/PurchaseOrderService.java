package org.cityu.supermarket.service;

import org.cityu.supermarket.entity.PurchaseOrder;

import java.util.List;

/**
 * Purchase order service API.
 */
public interface PurchaseOrderService {

    /**
     * Query purchase orders by optional filters.
     */
    List<PurchaseOrder> getPurchaseOrdersByCondition(String orderNumber, String supplierId, Integer status, String startDate, String endDate);

    /**
     * Fetch purchase order by id.
     */
    PurchaseOrder getPurchaseOrderById(String orderId);

    /**
     * Create a new purchase order.
     */
    void addPurchaseOrder(PurchaseOrder purchaseOrder);

    /**
     * Update an existing purchase order.
     */
    int updatePurchaseOrder(PurchaseOrder purchaseOrder);

    /**
     * Remove purchase order by id.
     */
    int deletePurchaseOrder(String orderId);

    /**
     * Update purchase order status.
     */
    int updatePurchaseOrderStatus(String orderId, Integer status);
}