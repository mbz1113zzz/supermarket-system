package org.cityu.supermarket.service.impl;

import org.cityu.supermarket.entity.Promotion;
import org.cityu.supermarket.mapper.PromotionMapper;
import org.cityu.supermarket.service.PromotionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.annotation.Resource;

import java.util.List;

/**
 * Promotion service implementation with transactional support
 *
 * @version 0.0.2
 * @date 2025-12-01
 */
@Service
public class PromotionServiceImpl implements PromotionService {

    @Resource
    private PromotionMapper promotionMapper;

    @Override
    public List<Promotion> getPromotionsByCondition(String promotionName, Integer promotionType, Integer status) {
        return promotionMapper.selectPromotionsByCondition(promotionName, promotionType, status);
    }

    @Override
    public List<Promotion> getActivePromotions() {
        return promotionMapper.selectActivePromotions();
    }

    @Override
    public Promotion getPromotionById(String promotionId) {
        return promotionMapper.selectPromotionById(promotionId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addPromotion(Promotion promotion) {
        promotionMapper.insertPromotion(promotion);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updatePromotion(Promotion promotion) {
        return promotionMapper.updatePromotion(promotion);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deletePromotion(String promotionId) {
        return promotionMapper.deletePromotion(promotionId);
    }
}