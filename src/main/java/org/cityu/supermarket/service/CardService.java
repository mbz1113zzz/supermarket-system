package org.cityu.supermarket.service;

import org.cityu.supermarket.entity.Card;

import java.util.List;

/**
 * Card service interface
 *
 * @version 0.0.1
 * @date 2025-11-01
 */
public interface CardService {
    /**
     * Fetch every card belonging to the specified member.
     * @param memberId member identifier
     * @return cards bound to the member
     */
    List<Card> getCardData(String memberId);

    /**
     * Retrieve details of a specific membership card.
     * @param cardId card identifier
     * @return card metadata
     */
    Card getCardDataById(String cardId);

    /**
     * Register a new membership card.
     * @param cardId card identifier
     */
    void registerCard(String cardId);

    /**
     * Remove an existing membership card.
     * @param cardId card identifier
     */
    void deleteCardById(String cardId);

    /**
     * Reissue card by moving balance/points to a newly created card and deleting the old one.
     * @param cardId original card id
     * @return new card id
     */
    String reissueCard(String cardId);

    /**
     * Report a card as lost.
     * @param cardId card identifier
     */
    void loseCard(String cardId);

    /**
     * Remove the lost status from a card.
     * @param cardId card identifier
     */
    void cancelCard(String cardId);

    /**
     * Recharge card balance.
     * @param cardId card identifier
     * @param parseInt recharge amount
     */
    void rechargeCard(String cardId, int parseInt);

    /**
     * Deduct balance for a purchase and grant points.
     * @param cardId card number
     * @param price purchase amount
     * @param integral points to add
     */
    void consumeCard(String cardId, int price, int integral);

    /**
     * Redeem member points.
     * @param memberId member identifier
     * @param parseInt points to deduct
     */
    void exchangeIntegral(String memberId, int parseInt);

    /**
     * Count all registered membership cards.
     * @return number of cards
     */
    Long getCardNum();

    /**
     * Perform fuzzy search by card id and member id.
     * @param memberId member id
     * @param cardId partial card number
     * @return matching card ids
     */
    List<String> getCardIdByFuzzyQuery(String memberId,String cardId);

    /**
     * Find membership card info by card id.
     * @param cardId card number
     * @return card info
     */
    Card getCardByCardId(String cardId);

    /**
     * Get member integral leaderboard.
     * @param limit max number of records
     * @return ranking result
     */
    java.util.List<java.util.Map<String, Object>> getMemberIntegralRanking(int limit);

    /**
     * Get membership balance distribution statistics.
     * @return member count within predefined balance buckets
     */
    java.util.List<java.util.Map<String, Object>> getBalanceDistribution();
}
