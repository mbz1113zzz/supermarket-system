package org.cityu.supermarket.service.impl;

import org.cityu.supermarket.entity.Coupon;
import org.cityu.supermarket.mapper.CouponMapper;
import org.cityu.supermarket.service.CouponService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.annotation.Resource;

import java.util.List;

/**
 * Coupon service implementation with transactional support
 *
 * @version 0.0.2
 * @date 2025-12-01
 */
@Service
public class CouponServiceImpl implements CouponService {

    @Resource
    private CouponMapper couponMapper;

    @Override
    public List<Coupon> getCouponsByCondition(String couponName, Integer couponType, Integer status) {
        return couponMapper.selectCouponsByCondition(couponName, couponType, status);
    }

    @Override
    public Coupon getCouponById(String couponId) {
        return couponMapper.selectCouponById(couponId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addCoupon(Coupon coupon) {
        couponMapper.insertCoupon(coupon);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateCoupon(Coupon coupon) {
        return couponMapper.updateCoupon(coupon);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteCoupon(String couponId) {
        return couponMapper.deleteCoupon(couponId);
    }
}