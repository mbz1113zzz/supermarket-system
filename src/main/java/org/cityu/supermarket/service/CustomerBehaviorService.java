package org.cityu.supermarket.service;

import org.cityu.supermarket.entity.CustomerBehavior;

import java.util.List;
import java.util.Map;

/**
 * Customer behavior analytics service interface.
 */
public interface CustomerBehaviorService {

   /**
    * Fetch customer behavior records with optional filters.
    */
   List<CustomerBehavior> getCustomerBehaviors(String memberId, Integer behaviorType,
                                    String productCategory, String startDate, String endDate);

   /**
    * Aggregate counts by behavior type.
    */
   List<Map<String, Object>> countBehaviorsByType(String startDate, String endDate);

   /**
    * Aggregate purchases grouped by product category.
    */
   List<Map<String, Object>> countPurchasesByCategory(String startDate, String endDate);

   /**
    * Retrieve hot product ranking.
    */
   List<Map<String, Object>> selectHotProducts(String startDate, String endDate, Integer limit);

   /**
    * Retrieve members ranked by consumption.
    */
   List<Map<String, Object>> selectMemberConsumption(String startDate, String endDate, Integer limit);

   /**
    * Record a customer behavior entry.
    */
   void recordCustomerBehavior(CustomerBehavior customerBehavior);
}