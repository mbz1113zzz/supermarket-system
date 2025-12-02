package org.cityu.supermarket.dto.request.procurement;

import jakarta.validation.constraints.*;
import lombok.Data;

/**
 * DTO for updating a supplier
 * 
 * @version 0.0.1
 * @date 2025-12-01
 */
@Data
public class SupplierUpdateRequest {
    
    @NotNull(message = "Supplier ID must not be null")
    @Min(value = 1, message = "Supplier ID is invalid")
    private Integer supplierId;
    
    @Size(max = 100, message = "Supplier name must not exceed 100 characters")
    private String supplierName;
    
    @Size(max = 50, message = "Contact name must not exceed 50 characters")
    private String contactPerson;
    
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "Contact phone format is invalid")
    private String contactPhone;
    
    @Size(max = 200, message = "Address must not exceed 200 characters")
    private String address;
    
    @Email(message = "Email format is invalid")
    @Size(max = 100, message = "Email must not exceed 100 characters")
    private String email;
    
    @Size(max = 500, message = "Remark must not exceed 500 characters")
    private String remark;
    
    @Min(value = 0, message = "Status value is invalid")
    @Max(value = 1, message = "Status value is invalid")
    private Integer status;
}
