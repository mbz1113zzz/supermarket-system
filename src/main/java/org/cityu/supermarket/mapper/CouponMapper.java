package org.cityu.supermarket.mapper;

import org.cityu.supermarket.entity.Coupon;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Coupon mapper interface
 */
@Mapper
public interface CouponMapper {

    /**
     * Query coupons by filters
     */
    List<Coupon> selectCouponsByCondition(@Param("couponName") String couponName,
                                         @Param("couponType") Integer couponType,
                                         @Param("status") Integer status);

    /**
     * Get coupon by ID
     */
    Coupon selectCouponById(@Param("couponId") String couponId);

    /**
     * Insert a coupon
     */
    int insertCoupon(Coupon coupon);

    /**
     * Update a coupon
     */
    int updateCoupon(Coupon coupon);

    /**
     * Delete a coupon
     */
    int deleteCoupon(@Param("couponId") String couponId);
}