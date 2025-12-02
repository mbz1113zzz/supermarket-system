package org.cityu.supermarket.controller;

import org.cityu.supermarket.common.constants.ResultCode;
import org.cityu.supermarket.common.vo.SupermarketResult;
import org.cityu.supermarket.entity.Manager;
import org.cityu.supermarket.service.ManagerService;
import org.cityu.supermarket.utils.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * Manager controller
 * 
 * @version 0.0.1
 * @date 2025-11-29
 * @updated added JWT token support
 */
@Tag(name = "Administrator Module", description = "Administrator authentication and profile management")
@CrossOrigin
@RestController
public class ManagerController {
    
    @Resource
    ManagerService managerService;
    
    @Resource
    JwtUtil jwtUtil;

    /**
     * get manager data
     */
        @Operation(summary = "Get administrator information", description = "Get administrator details by ID")
    @GetMapping("getManagerData")
    public SupermarketResult<Manager> getManagerData(
            @Parameter(description = "Administrator ID") @RequestParam String managerId) {
        Manager manager = managerService.selectByManagerId(managerId);
        if (manager != null) {
            // Don't expose password field
            manager.setPassword(null);
        }
        return SupermarketResult.success(manager);
    }

    /**
     * admin login
     * returns JWT Token on success
     */
        @Operation(summary = "Administrator login", description = "Verify administrator credentials and return a JWT token")
    @PostMapping("managerLogin")
    public SupermarketResult<Map<String, Object>> managerLogin(
            @Parameter(description = "Administrator ID") @RequestParam String managerId,
            @Parameter(description = "Password") @RequestParam String password) {
        
        // 1. Validate credentials
        Manager manager = managerService.managerLogin(managerId, password);
        
        if (manager == null) {
            return SupermarketResult.failure(ResultCode.UNAUTHORIZED, "Login failed. Invalid username or password.");
        }
        
        // 2. Generate JWT Token
        String token = jwtUtil.generateToken(manager.getManagerId(), manager.getRole());
        
        // 3. Return login info and Token
        Map<String, Object> data = new HashMap<>();
        data.put("token", token);
        data.put("managerId", manager.getManagerId());
        data.put("name", manager.getName());
        data.put("role", manager.getRole());
        
        return SupermarketResult.success("Login successful", data);
    }
}
