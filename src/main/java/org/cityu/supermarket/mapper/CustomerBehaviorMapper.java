package org.cityu.supermarket.mapper;

import org.cityu.supermarket.entity.CustomerBehavior;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Customer behavior analytics mapper
 */
@Mapper
public interface CustomerBehaviorMapper {

    /**
     * Paginated query for customer behavior data
     */
    List<CustomerBehavior> selectCustomerBehaviors(@Param("memberId") String memberId,
                                                   @Param("behaviorType") Integer behaviorType,
                                                   @Param("productCategory") String productCategory,
                                                   @Param("startDate") String startDate,
                                                   @Param("endDate") String endDate);

    /**
     * Count customer behaviors by type
     */
    List<Map<String, Object>> countBehaviorsByType(@Param("startDate") String startDate,
                                                    @Param("endDate") String endDate);

    /**
     * Count purchases by product category
     */
    List<Map<String, Object>> countPurchasesByCategory(@Param("startDate") String startDate,
                                                        @Param("endDate") String endDate);

    /**
     * Get hot products
     */
    List<Map<String, Object>> selectHotProducts(@Param("startDate") String startDate,
                                                @Param("endDate") String endDate,
                                                @Param("limit") Integer limit);

    /**
     * Get member consumption stats
     */
    List<Map<String, Object>> selectMemberConsumption(@Param("startDate") String startDate,
                                                      @Param("endDate") String endDate,
                                                      @Param("limit") Integer limit);

    /**
     * Insert a customer behavior record
     */
    int insertCustomerBehavior(CustomerBehavior customerBehavior);
}