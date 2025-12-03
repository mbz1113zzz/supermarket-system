package org.cityu.supermarket.service.impl;

import org.cityu.supermarket.entity.CustomerBehavior;
import org.cityu.supermarket.mapper.CustomerBehaviorMapper;
import org.cityu.supermarket.service.CustomerBehaviorService;
import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;

import java.util.List;
import java.util.Map;

/**
 * Customer behavior analytics service implementation
 *
 * @version 0.0.2
 * @date 2025-11-14
 */
@Service
public class CustomerBehaviorServiceImpl implements CustomerBehaviorService {

    @Resource
    private CustomerBehaviorMapper customerBehaviorMapper;

    @Override
    public List<CustomerBehavior> getCustomerBehaviors(String memberId, Integer behaviorType,
                                                      String productCategory, String startDate, String endDate) {
        return customerBehaviorMapper.selectCustomerBehaviors(memberId, behaviorType, productCategory, startDate, endDate);
    }

    @Override
    public List<Map<String, Object>> countBehaviorsByType(String startDate, String endDate) {
        return customerBehaviorMapper.countBehaviorsByType(startDate, endDate);
    }

    @Override
    public List<Map<String, Object>> countPurchasesByCategory(String startDate, String endDate) {
        return customerBehaviorMapper.countPurchasesByCategory(startDate, endDate);
    }

    @Override
    public List<Map<String, Object>> selectHotProducts(String startDate, String endDate, Integer limit) {
        return customerBehaviorMapper.selectHotProducts(startDate, endDate, limit);
    }

    @Override
    public List<Map<String, Object>> selectMemberConsumption(String startDate, String endDate, Integer limit) {
        return customerBehaviorMapper.selectMemberConsumption(startDate, endDate, limit);
    }

    @Override
    public void recordCustomerBehavior(CustomerBehavior customerBehavior) {
        customerBehaviorMapper.insertCustomerBehavior(customerBehavior);
    }
}