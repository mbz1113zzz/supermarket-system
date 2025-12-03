package org.cityu.supermarket.mapper;

import org.cityu.supermarket.entity.Promotion;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Promotion mapper interface
 */
@Mapper
public interface PromotionMapper {

    /**
     * Get all promotions
     */
    List<Promotion> selectAllPromotions();

    /**
     * Query promotions by filters
     */
    List<Promotion> selectPromotionsByCondition(@Param("promotionName") String promotionName,
                                             @Param("promotionType") Integer promotionType,
                                             @Param("status") Integer status);

    /**
     * Get active promotions
     */
    List<Promotion> selectActivePromotions();

    /**
     * Get promotion by ID
     */
    Promotion selectPromotionById(@Param("promotionId") String promotionId);

    /**
     * Insert promotion
     */
    int insertPromotion(Promotion promotion);

    /**
     * Update promotion
     */
    int updatePromotion(Promotion promotion);

    /**
     * Delete promotion
     */
    int deletePromotion(@Param("promotionId") String promotionId);

    /**
     * Count promotions
     */
    int countPromotions(@Param("status") Integer status);
}