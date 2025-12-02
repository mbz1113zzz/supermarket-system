package org.cityu.supermarket.mapper;

import org.cityu.supermarket.entity.Card;

import java.util.List;

/**
 * Card mapper interface
 *
 * @version 0.0.1
 * @date 2025-11-01
 */
public interface CardMapper {
    /**
     * Get all cards for a member by member ID
     * @param memberId
     * @return
     */
    List<Card> selectCardByMemberId(String memberId);

    /**
     * Insert member card
     * @param card
     */
    void insertCard(Card card);

    /**
     * Get card by card ID
     * @param cardId
     * @return
     */
    Card selectCardById(String cardId);

    /**
     * Delete card by card ID
     * @param cardId
     */
    void deleteCardById(String cardId);

    /**
     * Update card by card ID
     * @param card
     */
    void updateCardById(Card card);

    /**
     * Get total card count
     * @return
     */
    Long selectCardCount();

    List<String> selectCardIdByFuzzyQuery(String memberId,String cardId);

    /**
     * Get member points ranking
     * @param limit number of records to fetch
     * @return points ranking list (memberId, totalIntegral)
     */
    List<java.util.Map<String, Object>> selectMemberIntegralRanking(@org.apache.ibatis.annotations.Param("limit") int limit);

    /**
     * Get member balance distribution stats
     * @return member count for each balance range
     */
    List<java.util.Map<String, Object>> selectBalanceDistribution();

}