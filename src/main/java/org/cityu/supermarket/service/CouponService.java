package org.cityu.supermarket.service;

import org.cityu.supermarket.entity.Coupon;

import java.util.List;

/**
 * Coupon service interface.
 */
public interface CouponService {

      /**
       * Query coupon list by optional filters.
       */
      List<Coupon> getCouponsByCondition(String couponName, Integer couponType, Integer status);

      /**
       * Retrieve coupon details by id.
       */
      Coupon getCouponById(String couponId);

      /**
       * Create a new coupon definition.
       */
      void addCoupon(Coupon coupon);

      /**
       * Update coupon configuration.
       */
      int updateCoupon(Coupon coupon);

      /**
       * Delete coupon by id.
       */
      int deleteCoupon(String couponId);
}