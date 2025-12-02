package org.cityu.supermarket.mapper;

import org.cityu.supermarket.entity.PurchaseOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Purchase order mapper interface
 */
@Mapper
public interface PurchaseOrderMapper {

    /**
     * Query purchase orders by filters
     */
    List<PurchaseOrder> selectPurchaseOrdersByCondition(@Param("orderNumber") String orderNumber,
                                                       @Param("supplierId") String supplierId,
                                                       @Param("status") Integer status,
                                                       @Param("startDate") String startDate,
                                                       @Param("endDate") String endDate);

    /**
     * Get purchase order by ID
     */
    PurchaseOrder selectPurchaseOrderById(@Param("orderId") String orderId);

    /**
     * Insert purchase order
     */
    int insertPurchaseOrder(PurchaseOrder purchaseOrder);

    /**
     * Update purchase order
     */
    int updatePurchaseOrder(PurchaseOrder purchaseOrder);

    /**
     * Delete purchase order
     */
    int deletePurchaseOrder(@Param("orderId") String orderId);

    /**
     * Count purchase orders
     */
    int countPurchaseOrders(@Param("status") Integer status);

    /**
     * Update purchase order status
     */
    int updateOrderStatus(@Param("purchaseId") String purchaseId,
                           @Param("status") Integer status,
                           @Param("operator") String operator);

    /**
     * Update purchase order status (simplified version)
     */
    int updatePurchaseOrderStatus(@Param("orderId") String orderId, @Param("status") Integer status);
}