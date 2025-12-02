package org.cityu.supermarket.dto.request.procurement;

import jakarta.validation.constraints.*;
import lombok.Data;

/**
 * DTO for creating a supplier
 * 
 * @version 0.0.1
 * @date 2025-12-01
 */
@Data
public class SupplierCreateRequest {
    
    @NotBlank(message = "Supplier name must not be empty")
    @Size(max = 100, message = "Supplier name must not exceed 100 characters")
    private String supplierName;
    
    @NotBlank(message = "Contact person must not be empty")
    @Size(max = 50, message = "Contact name must not exceed 50 characters")
    private String contactPerson;
    
    @NotBlank(message = "Contact phone must not be empty")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "Contact phone format is invalid")
    private String contactPhone;
    
    @Size(max = 200, message = "Address must not exceed 200 characters")
    private String address;
    
    @Email(message = "Email format is invalid")
    @Size(max = 100, message = "Email must not exceed 100 characters")
    private String email;
    
    @Size(max = 500, message = "Remark must not exceed 500 characters")
    private String remark;
}
