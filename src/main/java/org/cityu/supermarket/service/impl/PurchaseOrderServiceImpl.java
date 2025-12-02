package org.cityu.supermarket.service.impl;

import org.cityu.supermarket.entity.PurchaseOrder;
import org.cityu.supermarket.mapper.PurchaseOrderMapper;
import org.cityu.supermarket.service.PurchaseOrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.annotation.Resource;

import java.util.List;

/**
 * Purchase order service implementation with transactional safeguards
 *
 * @version 0.0.1
 * @date 2025-12-01
 */
@Service
public class PurchaseOrderServiceImpl implements PurchaseOrderService {

    @Resource
    private PurchaseOrderMapper purchaseOrderMapper;

    @Override
    public List<PurchaseOrder> getPurchaseOrdersByCondition(String orderNumber, String supplierId, Integer status, String startDate, String endDate) {
        return purchaseOrderMapper.selectPurchaseOrdersByCondition(orderNumber, supplierId, status, startDate, endDate);
    }

    @Override
    public PurchaseOrder getPurchaseOrderById(String orderId) {
        return purchaseOrderMapper.selectPurchaseOrderById(orderId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addPurchaseOrder(PurchaseOrder purchaseOrder) {
        purchaseOrderMapper.insertPurchaseOrder(purchaseOrder);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updatePurchaseOrder(PurchaseOrder purchaseOrder) {
        return purchaseOrderMapper.updatePurchaseOrder(purchaseOrder);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deletePurchaseOrder(String orderId) {
        return purchaseOrderMapper.deletePurchaseOrder(orderId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updatePurchaseOrderStatus(String orderId, Integer status) {
        return purchaseOrderMapper.updatePurchaseOrderStatus(orderId, status);
    }
}