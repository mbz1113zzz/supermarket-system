package org.cityu.supermarket.service.impl;

import org.cityu.supermarket.entity.Supplier;
import org.cityu.supermarket.mapper.SupplierMapper;
import org.cityu.supermarket.service.SupplierService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.annotation.Resource;

import java.util.List;

/**
 * Supplier service implementation with transactional safeguards
 *
 * @version 0.0.1
 * @date 2025-12-01
 */
@Service
public class SupplierServiceImpl implements SupplierService {

    @Resource
    private SupplierMapper supplierMapper;

    @Override
    public List<Supplier> getSuppliersByCondition(String supplierName, Integer status, String contactName) {
        return supplierMapper.selectSuppliersByCondition(supplierName, status, contactName);
    }

    @Override
    public Supplier getSupplierById(String supplierId) {
        return supplierMapper.selectSupplierById(supplierId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addSupplier(Supplier supplier) {
        supplierMapper.insertSupplier(supplier);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateSupplier(Supplier supplier) {
        return supplierMapper.updateSupplier(supplier);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteSupplier(String supplierId) {
        return supplierMapper.deleteSupplier(supplierId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateSupplierStatus(String supplierId, Integer status) {
        return supplierMapper.updateSupplierStatus(supplierId, status);
    }
}