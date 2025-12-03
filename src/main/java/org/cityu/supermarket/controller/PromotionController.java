package org.cityu.supermarket.controller;

import org.cityu.supermarket.common.vo.SupermarketResult;
import org.cityu.supermarket.entity.Promotion;
import org.cityu.supermarket.dto.request.promotion.PromotionCreateRequest;
import org.cityu.supermarket.dto.request.promotion.PromotionUpdateRequest;
import org.cityu.supermarket.service.PromotionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import java.util.List;
import java.util.UUID;

/**
 * Promotion controller
 *
 * @version 0.0.2
 * @date 2025-12-01
 */
@Tag(name = "Promotion Module", description = "Promotion management APIs")
@CrossOrigin
@RestController
@RequestMapping("/promotions")
public class PromotionController {

    @Resource
    private PromotionService promotionService;

    /**
     * list promotions
     */
    @Operation(summary = "List promotions")
    @GetMapping
    public SupermarketResult<?> getPromotions(
            @RequestParam(required = false) String promotionName,
            @RequestParam(required = false) Integer promotionType,
            @RequestParam(required = false) Integer status) {
        try {
            List<Promotion> promotions = promotionService.getPromotionsByCondition(promotionName, promotionType, status);
            return SupermarketResult.success(promotions);
        } catch (Exception e) {
            return SupermarketResult.failure(400, "Failed to fetch promotion list", e.getMessage());
        }
    }

    /**
     * get active promotions
     */
    @Operation(summary = "List active promotions")
    @GetMapping("/active")
    public SupermarketResult<?> getActivePromotions() {
        try {
            List<Promotion> promotions = promotionService.getActivePromotions();
            return SupermarketResult.success(promotions);
        } catch (Exception e) {
            return SupermarketResult.failure(400, "Failed to fetch active promotions", e.getMessage());
        }
    }

    /**
     * get promotion by id
     */
    @Operation(summary = "Get promotion by ID")
    @GetMapping("/{promotionId}")
    public SupermarketResult<?> getPromotion(@PathVariable String promotionId) {
        try {
            Promotion promotion = promotionService.getPromotionById(promotionId);
            if (promotion == null) {
                return SupermarketResult.failure(404, "Promotion not found");
            }
            return SupermarketResult.success(promotion);
        } catch (Exception e) {
            return SupermarketResult.failure(400, "Failed to fetch promotion details", e.getMessage());
        }
    }

    /**
     * create promotion
     */
    @Operation(summary = "Create promotion")
    @PostMapping
    public SupermarketResult<?> addPromotion(@Valid @RequestBody PromotionCreateRequest request) {
        try {
            Promotion promotion = new Promotion();
            BeanUtils.copyProperties(request, promotion);
            promotion.setPromotionId("PRO" + UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase());
            promotionService.addPromotion(promotion);
            return SupermarketResult.success("Promotion created successfully");
        } catch (Exception e) {
            return SupermarketResult.failure(400, "Failed to create promotion", e.getMessage());
        }
    }

    /**
     * update promotion
     */
    @Operation(summary = "Update promotion")
    @PutMapping("/{promotionId}")
    public SupermarketResult<?> updatePromotion(
            @PathVariable String promotionId,
            @Valid @RequestBody PromotionUpdateRequest request) {
        try {
            Promotion promotion = new Promotion();
            BeanUtils.copyProperties(request, promotion);
            promotion.setPromotionId(promotionId);
            int result = promotionService.updatePromotion(promotion);
            if (result > 0) {
                return SupermarketResult.success("Promotion updated successfully");
            } else {
                return SupermarketResult.failure(400, "Promotion not found or update failed");
            }
        } catch (Exception e) {
            return SupermarketResult.failure(400, "Failed to update promotion", e.getMessage());
        }
    }

    /**
     * delete promotion
     */
    @Operation(summary = "Delete promotion")
    @DeleteMapping("/{promotionId}")
    public SupermarketResult<?> deletePromotion(@PathVariable String promotionId) {
        try {
            int result = promotionService.deletePromotion(promotionId);
            if (result > 0) {
                return SupermarketResult.success("Promotion deleted successfully");
            } else {
                return SupermarketResult.failure(400, "Promotion not found or delete failed");
            }
        } catch (Exception e) {
            return SupermarketResult.failure(400, "Failed to delete promotion", e.getMessage());
        }
    }
}
