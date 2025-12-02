package org.cityu.supermarket.mapper;

import org.cityu.supermarket.entity.Supplier;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Supplier mapper interface
 */
@Mapper
public interface SupplierMapper {

    /**
     * Get all suppliers
     */
    List<Supplier> selectAllSuppliers();

    /**
     * Query suppliers by filters
     */
    List<Supplier> selectSuppliersByCondition(@Param("supplierName") String supplierName,
                                           @Param("status") Integer status,
                                           @Param("contactName") String contactName);

    /**
     * Get supplier by ID
     */
    Supplier selectSupplierById(@Param("supplierId") String supplierId);

    /**
     * Insert supplier
     */
    int insertSupplier(Supplier supplier);

    /**
     * Update supplier
     */
    int updateSupplier(Supplier supplier);

    /**
     * Delete supplier
     */
    int deleteSupplier(@Param("supplierId") String supplierId);

    /**
     * Count suppliers
     */
    int countSuppliers(@Param("status") Integer status);

    /**
     * Update supplier status
     */
    int updateSupplierStatus(@Param("supplierId") String supplierId, @Param("status") Integer status);
}