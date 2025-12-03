package org.cityu.supermarket.controller;

import org.cityu.supermarket.common.vo.SupermarketResult;
import org.cityu.supermarket.entity.Coupon;
import org.cityu.supermarket.dto.request.coupon.CouponCreateRequest;
import org.cityu.supermarket.dto.request.coupon.CouponUpdateRequest;
import org.cityu.supermarket.service.CouponService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import java.util.List;
import java.util.UUID;

/**
 * Coupon controller
 *
 * @version 0.0.2
 * @date 2025-12-01
 */
@Tag(name = "Coupon Module", description = "Coupon management APIs")
@CrossOrigin
@RestController
@RequestMapping("/coupons")
public class CouponController {

    @Resource
    private CouponService couponService;

    /**
     * list coupons
     */
    @Operation(summary = "List coupons")
    @GetMapping
    public SupermarketResult<?> getCoupons(
            @RequestParam(required = false) String couponName,
            @RequestParam(required = false) Integer couponType,
            @RequestParam(required = false) Integer status) {
        try {
            List<Coupon> coupons = couponService.getCouponsByCondition(couponName, couponType, status);
            return SupermarketResult.success(coupons);
        } catch (Exception e) {
            return SupermarketResult.failure(400, "Failed to fetch coupon list", e.getMessage());
        }
    }

    /**
     * get coupon by id
     */
    @Operation(summary = "Get coupon by ID")
    @GetMapping("/{couponId}")
    public SupermarketResult<?> getCoupon(@PathVariable String couponId) {
        try {
            Coupon coupon = couponService.getCouponById(couponId);
            if (coupon == null) {
                return SupermarketResult.failure(404, "Coupon not found");
            }
            return SupermarketResult.success(coupon);
        } catch (Exception e) {
            return SupermarketResult.failure(400, "Failed to fetch coupon details", e.getMessage());
        }
    }

    /**
     * create coupon
     */
    @Operation(summary = "Create coupon")
    @PostMapping
    public SupermarketResult<?> addCoupon(@Valid @RequestBody CouponCreateRequest request) {
        try {
            Coupon coupon = new Coupon();
            BeanUtils.copyProperties(request, coupon);
            coupon.setCouponId("COUP" + UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase());
            couponService.addCoupon(coupon);
            return SupermarketResult.success("Coupon created successfully");
        } catch (Exception e) {
            return SupermarketResult.failure(400, "Failed to create coupon", e.getMessage());
        }
    }

    /**
     * update coupon
     */
    @Operation(summary = "Update coupon")
    @PutMapping("/{couponId}")
    public SupermarketResult<?> updateCoupon(
            @PathVariable String couponId,
            @Valid @RequestBody CouponUpdateRequest request) {
        try {
            Coupon coupon = new Coupon();
            BeanUtils.copyProperties(request, coupon);
            coupon.setCouponId(couponId);
            int result = couponService.updateCoupon(coupon);
            if (result > 0) {
                return SupermarketResult.success("Coupon updated successfully");
            } else {
                return SupermarketResult.failure(400, "Coupon not found or update failed");
            }
        } catch (Exception e) {
            return SupermarketResult.failure(400, "Failed to update coupon", e.getMessage());
        }
    }

    /**
     * delete coupon
     */
    @Operation(summary = "Delete coupon")
    @DeleteMapping("/{couponId}")
    public SupermarketResult<?> deleteCoupon(@PathVariable String couponId) {
        try {
            int result = couponService.deleteCoupon(couponId);
            if (result > 0) {
                return SupermarketResult.success("Coupon deleted successfully");
            } else {
                return SupermarketResult.failure(400, "Coupon not found or delete failed");
            }
        } catch (Exception e) {
            return SupermarketResult.failure(400, "Failed to delete coupon", e.getMessage());
        }
    }
}
