package org.cityu.supermarket.service;

import org.cityu.supermarket.entity.Promotion;

import java.util.List;

/**
 * Promotion service interface.
 */
public interface PromotionService {

    /**
     * Query promotions via optional filters.
     */
    List<Promotion> getPromotionsByCondition(String promotionName, Integer promotionType, Integer status);

    /**
     * Retrieve currently active promotions.
     */
    List<Promotion> getActivePromotions();

    /**
     * Fetch promotion by id.
     */
    Promotion getPromotionById(String promotionId);

    /**
     * Create a new promotion.
     */
    void addPromotion(Promotion promotion);

    /**
     * Update promotion.
     */
    int updatePromotion(Promotion promotion);

    /**
     * Delete promotion by id.
     */
    int deletePromotion(String promotionId);
}