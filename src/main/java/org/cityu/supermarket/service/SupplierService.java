package org.cityu.supermarket.service;

import org.cityu.supermarket.entity.Supplier;

import java.util.List;

/**
 * Supplier service interface.
 */
public interface SupplierService {

    /**
     * Query suppliers via optional filters.
     */
    List<Supplier> getSuppliersByCondition(String supplierName, Integer status, String contactName);

    /**
     * Fetch supplier by id.
     */
    Supplier getSupplierById(String supplierId);

    /**
     * Create supplier record.
     */
    void addSupplier(Supplier supplier);

    /**
     * Update supplier info.
     */
    int updateSupplier(Supplier supplier);

    /**
     * Delete supplier by id.
     */
    int deleteSupplier(String supplierId);

    /**
     * Update supplier status.
     */
    int updateSupplierStatus(String supplierId, Integer status);
}