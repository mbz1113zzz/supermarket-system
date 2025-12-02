package org.cityu.supermarket.controller;

import org.cityu.supermarket.common.vo.SupermarketResult;
import org.cityu.supermarket.entity.Supplier;
import org.cityu.supermarket.dto.request.procurement.SupplierCreateRequest;
import org.cityu.supermarket.dto.request.procurement.SupplierUpdateRequest;
import org.cityu.supermarket.service.SupplierService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Supplier controller
 *
 * @version 0.0.1
 * @date 2025-12-01
 */
@Tag(name = "Supplier Module", description = "Supplier management APIs")
@CrossOrigin
@RestController
@RequestMapping("/suppliers")
public class SupplierController {

    @Resource
    private SupplierService supplierService;

    /**
     * list suppliers
     */
    @Operation(summary = "List suppliers")
    @GetMapping
    public SupermarketResult<?> getSuppliers(
            @RequestParam(required = false) String supplierName,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) String contactName) {
        try {
            List<Supplier> suppliers = supplierService.getSuppliersByCondition(supplierName, status, contactName);
            return SupermarketResult.success(suppliers);
        } catch (Exception e) {
            return SupermarketResult.failure(400, "Failed to fetch supplier list", e.getMessage());
        }
    }

    /**
     * get supplier by id
     */
    @Operation(summary = "Get supplier by ID")
    @GetMapping("/{supplierId}")
    public SupermarketResult<?> getSupplier(@PathVariable String supplierId) {
        try {
            Supplier supplier = supplierService.getSupplierById(supplierId);
            if (supplier == null) {
                return SupermarketResult.failure(404, "Supplier not found");
            }
            return SupermarketResult.success(supplier);
        } catch (Exception e) {
            return SupermarketResult.failure(400, "Failed to fetch supplier details", e.getMessage());
        }
    }

    /**
     * create supplier
     */
    @Operation(summary = "Create supplier")
    @PostMapping
    public SupermarketResult<?> addSupplier(@Valid @RequestBody SupplierCreateRequest request) {
        try {
            Supplier supplier = new Supplier();
            BeanUtils.copyProperties(request, supplier);
            supplier.setSupplierId("SUP" + UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase());
            supplierService.addSupplier(supplier);
            return SupermarketResult.success("Supplier created successfully");
        } catch (Exception e) {
            return SupermarketResult.failure(400, "Failed to create supplier", e.getMessage());
        }
    }

    /**
     * update supplier
     */
    @Operation(summary = "Update supplier")
    @PutMapping("/{supplierId}")
    public SupermarketResult<?> updateSupplier(
            @PathVariable String supplierId,
            @Valid @RequestBody SupplierUpdateRequest request) {
        try {
            Supplier supplier = new Supplier();
            BeanUtils.copyProperties(request, supplier);
            supplier.setSupplierId(supplierId);
            int result = supplierService.updateSupplier(supplier);
            if (result > 0) {
                return SupermarketResult.success("Supplier updated successfully");
            } else {
                return SupermarketResult.failure(400, "Supplier not found or update failed");
            }
        } catch (Exception e) {
            return SupermarketResult.failure(400, "Failed to update supplier", e.getMessage());
        }
    }

    /**
     * delete supplier
     */
    @Operation(summary = "Delete supplier")
    @DeleteMapping("/{supplierId}")
    public SupermarketResult<?> deleteSupplier(@PathVariable String supplierId) {
        try {
            int result = supplierService.deleteSupplier(supplierId);
            if (result > 0) {
                return SupermarketResult.success("Supplier deleted successfully");
            } else {
                return SupermarketResult.failure(400, "Supplier not found or delete failed");
            }
        } catch (Exception e) {
            return SupermarketResult.failure(400, "Failed to delete supplier", e.getMessage());
        }
    }

    /**
     * update supplier status
     */
    @Operation(summary = "Update supplier status")
    @PatchMapping("/{supplierId}/status")
    public SupermarketResult<?> updateSupplierStatus(
            @PathVariable String supplierId,
            @RequestBody Map<String, Integer> body) {
        try {
            Integer status = body.get("status");
            if (status == null) {
                return SupermarketResult.failure(400, "Status is required");
            }
            int result = supplierService.updateSupplierStatus(supplierId, status);
            if (result > 0) {
                return SupermarketResult.success("Supplier status updated successfully");
            } else {
                return SupermarketResult.failure(400, "Supplier not found or update failed");
            }
        } catch (Exception e) {
            return SupermarketResult.failure(400, "Failed to update supplier status", e.getMessage());
        }
    }
}
