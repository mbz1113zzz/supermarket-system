package org.cityu.supermarket.controller;

import org.cityu.supermarket.common.vo.SupermarketResult;
import org.cityu.supermarket.entity.CustomerBehavior;
import org.cityu.supermarket.service.CustomerBehaviorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Analytics controller
 *
 * @version 0.0.2
 * @date 2025-12-01
 */
@Tag(name = "Analytics Module", description = "Customer behavior analytics APIs")
@CrossOrigin
@RestController
@RequestMapping("/analytics")
public class AnalyticsController {

    @Resource
    private CustomerBehaviorService customerBehaviorService;

    /**
     * list customer behaviors
     */
    @Operation(summary = "List customer behaviors")
    @GetMapping("/behaviors")
    public SupermarketResult<?> getCustomerBehaviors(
            @RequestParam(required = false) String memberId,
            @RequestParam(required = false) Integer behaviorType,
            @RequestParam(required = false) String productCategory,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        try {
            List<CustomerBehavior> behaviors = customerBehaviorService.getCustomerBehaviors(memberId, behaviorType, productCategory, startDate, endDate);
            return SupermarketResult.success(behaviors);
        } catch (Exception e) {
            return SupermarketResult.failure(400, "Failed to fetch customer behavior data", e.getMessage());
        }
    }

    /**
     * behavior type stats
     */
    @Operation(summary = "Aggregate behavior types")
    @GetMapping("/behaviors/stats")
    public SupermarketResult<?> getBehaviorStats(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        try {
            List<Map<String, Object>> stats = customerBehaviorService.countBehaviorsByType(startDate, endDate);
            return SupermarketResult.success(stats);
        } catch (Exception e) {
            return SupermarketResult.failure(400, "Failed to aggregate behavior data", e.getMessage());
        }
    }

    /**
     * category purchase stats
     */
    @Operation(summary = "Aggregate purchases by category")
    @GetMapping("/categories/stats")
    public SupermarketResult<?> getCategoryStats(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        try {
            List<Map<String, Object>> stats = customerBehaviorService.countPurchasesByCategory(startDate, endDate);
            return SupermarketResult.success(stats);
        } catch (Exception e) {
            return SupermarketResult.failure(400, "Failed to aggregate category data", e.getMessage());
        }
    }

    /**
     * hot products ranking
     */
    @Operation(summary = "Get hot product ranking")
    @GetMapping("/products/hot")
    public SupermarketResult<?> getHotProducts(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false, defaultValue = "10") Integer limit) {
        try {
            List<Map<String, Object>> hotProducts = customerBehaviorService.selectHotProducts(startDate, endDate, limit);
            return SupermarketResult.success(hotProducts);
        } catch (Exception e) {
            return SupermarketResult.failure(400, "Failed to fetch hot products", e.getMessage());
        }
    }

    /**
     * member consumption ranking
     */
    @Operation(summary = "Get member consumption ranking")
    @GetMapping("/members/consumption")
    public SupermarketResult<?> getMemberConsumption(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false, defaultValue = "10") Integer limit) {
        try {
            List<Map<String, Object>> memberConsumption = customerBehaviorService.selectMemberConsumption(startDate, endDate, limit);
            return SupermarketResult.success(memberConsumption);
        } catch (Exception e) {
            return SupermarketResult.failure(400, "Failed to fetch member consumption ranking", e.getMessage());
        }
    }

    /**
     * record customer behavior
     */
    @Operation(summary = "Record customer behavior")
    @PostMapping("/behaviors")
    public SupermarketResult<?> recordCustomerBehavior(@RequestBody CustomerBehavior customerBehavior) {
        try {
            customerBehaviorService.recordCustomerBehavior(customerBehavior);
            return SupermarketResult.success("Customer behavior recorded successfully");
        } catch (Exception e) {
            return SupermarketResult.failure(400, "Failed to record customer behavior", e.getMessage());
        }
    }
}
